package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	/** The current line number to determine variables in scope. */
	private int lineNumber;
	
	/** Current variable to type map. */
	private Map<Integer, VariableType> localVariables = new HashMap<Integer, VariableType>();
	
	/** Variable scopes in the method. */
	private List<LocalVariableScope> localVariableScopes = new ArrayList<>();

	private Set<AccessedField> accessedFields;

	private String methodName;

	private String className;

	private boolean clearTrackedDataAtStart;


	public LocalVariableStateEmitterTestMethodVisitor(MethodVisitor mv, String className, String methodName, boolean clearTrackedDataAtStart) {
		super(Opcodes.ASM5, mv);
		this.className = className;
		this.methodName = methodName;
		this.clearTrackedDataAtStart = clearTrackedDataAtStart;
	}
	
	@Override
	public void visitCode() {
		super.visitCode();
		if (clearTrackedDataAtStart) {
			instrumentToClearTrackedDataAndSignalStartOfRecording();
		}
		
		// track initial field states
		for (AccessedField accessedField : accessedFields) {
			instrumentToTrackFieldState(accessedField);
		}
	}

	@Override
	public void visitLineNumber(int lineNumber, Label label) {
		this.lineNumber = lineNumber;
		super.visitLineNumber(lineNumber, label);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		localVariables.clear();
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);

		// track every in-scope variable state after method calls
		for (Integer var : localVariables.keySet()) {
			if (isVariableInScope(var)) {
				instrumentToTrackVariableState(var);
			}
		}
		
		// track every field state after method calls
		for (AccessedField accessedField : accessedFields) {
			instrumentToTrackFieldState(accessedField);
		}
    }
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		
		// Track variable state and name at variable stores. (Typical variable assignments.)
		VariableType variableType = VariableType.getByStoreOpCode(opcode);
		if (variableType != null) {
			localVariables.put(var, variableType);
			instrumentToTrackVariableName(var);
			instrumentToTrackVariableState(var);
		}
	}
	
	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		
		// Track variable state and name at variable stores. (At variable increases.)
		instrumentToTrackVariableName(var);
		instrumentToTrackVariableState(var);
	}
	
	private void instrumentToClearTrackedDataAndSignalStartOfRecording() {
		super.visitLdcInsn(className);
		super.visitLdcInsn(methodName);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "startTracking", "(Ljava/lang/String;Ljava/lang/String;)V", false);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);
		if (Opcodes.PUTFIELD == opcode|| Opcodes.PUTSTATIC == opcode) {
			for (AccessedField accessedField : accessedFields) {
				if (accessedField.name.equals(name)) {
					instrumentToTrackFieldState(accessedField);
					break;
				}
			}
		}
	}
	
	private void instrumentToTrackVariableState(int var) {
		super.visitVarInsn(localVariables.get(var).loadOpcode, var);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(var);
		super.visitLdcInsn(methodName);
		LocalVariableScope lvs = null;
		for (LocalVariableScope localVariableScope : localVariableScopes) {
			if (localVariableScope.var == var) {
				lvs = localVariableScope;
			}
		}
		
		if (lvs != null) {
			String desc = lvs.desc;
			if (desc.startsWith("L") || desc.startsWith("[")) {
				desc = VariableType.REFERENCE.signature;
			}
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackLocalVariableState", "(" + desc + "IILjava/lang/String;)V", false);
		} else {
			// If no variable declaration found for this variable, use the description from the load opcode.
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackLocalVariableState", "(" + localVariables.get(var).signature + "IILjava/lang/String;)V", false);
		}
	}
	
	private void instrumentToTrackFieldState(AccessedField accessedField) {
		final int opcode;
		if (accessedField.isStatic) {
			opcode = Opcodes.GETSTATIC;
		} else {
			opcode = Opcodes.GETFIELD;
			super.visitVarInsn(Opcodes.ALOAD, 0);
		}

		String desc = accessedField.desc;
		if (desc.startsWith("L") || desc.startsWith("[")) {
			desc = VariableType.REFERENCE.signature;
		}
		
		super.visitFieldInsn(opcode, accessedField.owner, accessedField.name, accessedField.desc);
		super.visitLdcInsn(accessedField.name);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(accessedField.isStatic);
		super.visitLdcInsn(accessedField.owner);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackFieldState", "(" + desc + "Ljava/lang/String;IZLjava/lang/String;)V", false);
	}
	
	private void instrumentToTrackVariableName(int var) {
		String variableName = getVariableNameInCurrentScope(var);
		if (variableName != null) { 
			/* 
			 * This null-check is the workaround for issue #15:
			 * If a variable declaration is the last statement in a code block,
			 * then the variable name is not present in the compiled bytecode.
			 * With this workaround Scott can still track the assigned value to such variables.
			 */
			
			super.visitLdcInsn(variableName);
			super.visitLdcInsn(lineNumber);
			super.visitLdcInsn(var);
			super.visitLdcInsn(methodName);
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackVariableName", "(Ljava/lang/String;IILjava/lang/String;)V", false);
		}
	}
	
	private boolean isVariableInScope(int var) {
		return getVariableNameInCurrentScope(var) != null;
	}
	
	private String getVariableNameInCurrentScope(int var) {
		// check the scopes in reverse order in case of multiple var declarations on the same line
		List<LocalVariableScope> localVariableScopesReversed = localVariableScopes;
		
		Collections.reverse(localVariableScopesReversed);
		
		for (LocalVariableScope localVariableRange : localVariableScopesReversed) {
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
	
	public void setAccessedFields(Set<AccessedField> accessedFields) {
		this.accessedFields = accessedFields;
	}
	
	static class AccessedField {
		final String owner;
		final String name;
		final String desc;
		final boolean isStatic;
		
		public AccessedField(String owner, String name, String desc, boolean isStatic) {
			this.owner = owner;
			this.name = name;
			this.desc = desc;
			this.isStatic = isStatic;
		}

		@Override
		public String toString() {
			return "AccessedField [owner=" + owner + ", name=" + name + ", desc=" + desc + ", isStatic=" + isStatic + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AccessedField other = (AccessedField) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}
	
	static class LocalVariableScope {
		final int var;
		final String name;
		final String desc;
		final int start;
		final int end;
		
		LocalVariableScope(int var, String name, String desc, int start, int end) {
			this.var = var;
			this.name = name;
			this.desc = desc;
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return "LocalVariableScope [var=" + var + ", name=" + name + ", start=" + start + ", end=" + end + "]";
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
