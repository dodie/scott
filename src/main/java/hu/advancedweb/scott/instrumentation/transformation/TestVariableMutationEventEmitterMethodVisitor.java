package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Instrument the method if it is a test-case.
 * @author David Csakvari
 */
public class TestVariableMutationEventEmitterMethodVisitor extends MethodVisitor {

	boolean isTestCase;
	int lineNumber;
	Map<Integer, VariableType> variables = new HashMap<Integer, VariableType>();

	public TestVariableMutationEventEmitterMethodVisitor(MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

	@Override
	public void visitLineNumber(int lineNumber, Label label) {
		this.lineNumber = lineNumber;
		super.visitLineNumber(lineNumber, label);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		isTestCase = "Lorg/junit/Test;".equals(desc);
		
		if (isTestCase) {
			resetState();
		}
		
		return super.visitAnnotation(desc, visible);
	}

	private void resetState() {
		variables.clear();
	}
	
	@Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		
		if (isTestCase) {
			// track every variable state after method calls
			for (Integer var : variables.keySet()) {
				for (LocalVariableRange localVariableRange : localVariableScopes) {
					if (localVariableRange.var == var &&
							localVariableRange.start <= lineNumber &&
							localVariableRange.end >= lineNumber) {
						instument(var);
						break;
					}
				}
			}
		}
    }
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		if (isTestCase) {
			VariableType variableType = VariableType.getByStoreOpCode(opcode);
			if (variableType != null) {
				variables.put(var, variableType);
				instument(var);
			}
		}
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		if (isTestCase) {
			instument(var);
		}
	}
	
	private void instument(int var) {
		super.visitVarInsn(variables.get(var).loadOpcode, var);
		super.visitLdcInsn(lineNumber);
        super.visitLdcInsn(var);
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/event/EventStore", "track", "(" + variables.get(var).signature + "II)V", false);
	}
	
	public void resetLocalVariableScopes() {
		localVariableScopes.clear();
	}
	
	public void addLocalVariableScope(int var, int start, int end) {
		localVariableScopes.add(new LocalVariableRange(var, start, end));
	}
	
	List<LocalVariableRange> localVariableScopes = new ArrayList<>();
	
	private static class LocalVariableRange {
		final int var;
		final int start;
		final int end;
		
		public LocalVariableRange(int var, int start, int end) {
			this.var = var;
			this.start = start;
			this.end = end;
		}
	}
	
	private static enum VariableType {
		INTEGER(Opcodes.ILOAD, Opcodes.ISTORE, "I"),
		LONG(Opcodes.LLOAD, Opcodes.LSTORE, "J"),
		FLOAT(Opcodes.FLOAD, Opcodes.FSTORE, "F"),
		DOUBLE(Opcodes.DLOAD, Opcodes.DSTORE, "D"),
		REFERENCE(Opcodes.ALOAD, Opcodes.ASTORE, "Ljava/lang/Object;");

		final int loadOpcode;
		final int storeOpcode;
		final String signature;
		
		VariableType(int loadOpcode, int storeOpcode, String signature) {
			this.loadOpcode = loadOpcode;
			this.storeOpcode = storeOpcode;
			this.signature = signature;
		}
		
		static VariableType getByStoreOpCode(final int opcode) {
			for (int i = 0; i < VariableType.values().length; i++) {
				VariableType variableType = VariableType.values()[i];
				if (variableType.storeOpcode == opcode) {
					return variableType;
				}
			}
			return null;
		}
	}

}