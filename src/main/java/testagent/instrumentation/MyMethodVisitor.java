package testagent.instrumentation;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Instrument the method if it is a test-case.
 * @author David Csakvari
 */
public class MyMethodVisitor extends MethodVisitor {

	boolean isTestCase;
	int lineNumber;
	Map<Integer, VariableType> variables = new HashMap<Integer, VariableType>();

	public MyMethodVisitor(MethodVisitor mv) {
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
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		VariableType variableType = VariableType.getByStoreOpCode(opcode);
		if (variableType != null) {
			variables.put(var, variableType);
			instument(var);
		}
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		instument(var);
	}
	
	private void instument(int var) {
		if (isTestCase) {
			super.visitVarInsn(variables.get(var).loadOpcode, var);
			super.visitLdcInsn(lineNumber);
	        super.visitMethodInsn(Opcodes.INVOKESTATIC, "runlistener/EventRepository", "track", "(" + variables.get(var).signature + "I)V", false);
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