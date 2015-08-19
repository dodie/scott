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
 * Instruments test methods to call Scott Runtime to track variable states.
 * 
 * @author David Csakvari
 */
public class LocalVariableStateEmitterTestMethodVisitor extends MethodVisitor {

	/** True if the current method is a test case to instrument. */
	private boolean isTestCase;
	
	/** The current line number to determine variables in scope. */
	private int lineNumber;
	
	/** Current variable to type map. */
	private Map<Integer, VariableType> localVariables = new HashMap<Integer, VariableType>();
	
	/** Variable scopes in the method. */
	private List<LocalVariableScope> localVariableScopes = new ArrayList<>();

	public LocalVariableStateEmitterTestMethodVisitor(MethodVisitor mv) {
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
		localVariables.clear();
	}
	
	@Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		
		if (isTestCase) {
			// track every in-scope variable state after method calls
			for (Integer var : localVariables.keySet()) {
				if (isVariableInScope(var)) {
					instumentToTrackVariableState(var);
				}
			}
		}
    }
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		
		if (isTestCase) {
			// Track variable state and name at variable stores. (Typical variable assignments.)
			VariableType variableType = VariableType.getByStoreOpCode(opcode);
			if (variableType != null) {
				localVariables.put(var, variableType);
				instrumentToTrackVariableName(var);
				instumentToTrackVariableState(var);
			}
		}
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		
		if (isTestCase) {
			// Track variable state and name at variable stores. (At variable increases.)
			instrumentToTrackVariableName(var);
			instumentToTrackVariableState(var);
		}
	}
	
	private void instumentToTrackVariableState(int var) {
		super.visitVarInsn(localVariables.get(var).loadOpcode, var);
		super.visitLdcInsn(lineNumber);
        super.visitLdcInsn(var);
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackLocalVariableState", "(" + localVariables.get(var).signature + "II)V", false);
	}
	
	private void instrumentToTrackVariableName(int var) {
		super.visitLdcInsn(var);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(getVariableNameInCurrentScope(var));
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackVariableName", "(IILjava/lang/String;)V", false);
	}
	
	private boolean isVariableInScope(int var) {
		return getVariableNameInCurrentScope(var) != null;
	}
	
	private String getVariableNameInCurrentScope(int var) {
		for (LocalVariableScope localVariableRange : localVariableScopes) {
			if (localVariableRange.var == var &&
					localVariableRange.start <= lineNumber &&
					localVariableRange.end >= lineNumber) {
				return localVariableRange.name;
			}
		}
		return null;
	}

	public void setLocalVariableScopes(List<LocalVariableScope> localVariableScopes) {
		this.localVariableScopes = localVariableScopes;
	}
	
	static class LocalVariableScope {
		final int var;
		final String name;
		final int start;
		final int end;
		
		LocalVariableScope(int var, String name, int start, int end) {
			this.var = var;
			this.name = name;
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