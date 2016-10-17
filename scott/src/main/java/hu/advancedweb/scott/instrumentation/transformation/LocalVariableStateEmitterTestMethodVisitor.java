package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

	private int lineNumber;
	
	private Set<Integer> localVariables = new HashSet<>();
	
	private List<LocalVariableScope> localVariableScopes = new ArrayList<>();

	private Set<AccessedField> accessedFields;

	private String methodName;

	private String className;

	private boolean clearTrackedDataAtStart;
	
	private boolean methodStartTracked;


	public LocalVariableStateEmitterTestMethodVisitor(MethodVisitor mv, String className, String methodName, boolean clearTrackedDataAtStart) {
		super(Opcodes.ASM5, mv);
		this.className = className;
		this.methodName = methodName;
		this.clearTrackedDataAtStart = clearTrackedDataAtStart;
	}
	
	@Override
	public void visitCode() {
		super.visitCode();
		
		// clear previously tracked data
		if (clearTrackedDataAtStart) {
			instrumentToClearTrackedDataAndSignalStartOfRecording();
		}
		
		// track initial field states
		for (AccessedField accessedField : accessedFields) {
			instrumentToTrackFieldState(accessedField);
		}
		
		// track method arguments
		for (LocalVariableScope localVariableScope : localVariableScopes) {
			if (localVariableScope.start == 0) {
				instrumentToTrackVariableName(localVariableScope);
				instrumentToTrackVariableState(localVariableScope);
			}
		}
	}
	
	@Override
	public void visitLineNumber(int lineNumber, Label label) {
		this.lineNumber = lineNumber;
		
		if (!methodStartTracked) {
			methodStartTracked = true;
			instrumentToTrackMethodStart(lineNumber);
		}
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

		for (LocalVariableScope localVariableScope : localVariableScopes) {
			if (!localVariables.contains(localVariableScope.var)) continue;
			
			if (isVariableInScope(localVariableScope.var)) {
				instrumentToTrackVariableState(localVariableScope);
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
		if (VariableType.isStoreOperation(opcode)) {
			localVariables.add(var);
			
			LocalVariableScope lvs = getLocalVariableScope(var);
			if (lvs != null) {
				/* 
				 * This null-check is the workaround for issue #15:
				 * If a variable declaration is the last statement in a code block,
				 * then the variable name is not present in the compiled bytecode.
				 * With this workaround Scott can still track the assigned value to such variables.
				 */
				instrumentToTrackVariableName(lvs);
				instrumentToTrackVariableState(lvs);
			}
		}
	}
	
	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		
		// Track variable state and name at variable stores. (At variable increases.)
		LocalVariableScope lvs = getLocalVariableScope(var);
		instrumentToTrackVariableName(lvs);
		instrumentToTrackVariableState(lvs);
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
	
	private void instrumentToTrackVariableState(LocalVariableScope localVariableScope) {
		super.visitVarInsn(localVariableScope.variableType.loadOpcode, localVariableScope.var);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(localVariableScope.var);
		super.visitLdcInsn(methodName);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackLocalVariableState", "(" + localVariableScope.variableType.desc + "IILjava/lang/String;)V", false);
	}
	
	private void instrumentToTrackMethodStart(int lineNumber) {
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(methodName);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackMethodStart", "(ILjava/lang/String;)V", false);
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
			desc = VariableType.REFERENCE.desc;
		}
		
		super.visitFieldInsn(opcode, accessedField.owner, accessedField.name, accessedField.desc);
		super.visitLdcInsn(accessedField.name);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(accessedField.isStatic);
		super.visitLdcInsn(accessedField.owner);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackFieldState", "(" + desc + "Ljava/lang/String;IZLjava/lang/String;)V", false);
	}
	
	private void instrumentToTrackVariableName(LocalVariableScope localVariableScope) {
		super.visitLdcInsn(localVariableScope.name);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(localVariableScope.var);
		super.visitLdcInsn(methodName);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "hu/advancedweb/scott/runtime/track/LocalVariableStateRegistry", "trackVariableName", "(Ljava/lang/String;IILjava/lang/String;)V", false);
	}
	
	private boolean isVariableInScope(int var) {
		return getLocalVariableScope(var) != null;
	}
	
	private LocalVariableScope getLocalVariableScope(int var) {
		// check the scopes in reverse order in case of multiple var declarations on the same line
		List<LocalVariableScope> localVariableScopesReversed = new ArrayList<>(localVariableScopes);
		Collections.reverse(localVariableScopesReversed);
		
		for (LocalVariableScope localVariableScope : localVariableScopes) {
			if (localVariableScope.var == var &&
					localVariableScope.start <= lineNumber &&
					localVariableScope.end >= lineNumber) {
				return localVariableScope;
			}
		}
		return null;
	}
	
	public void setLocalVariableScopes(List<LocalVariableScope> localVariableScopes) {
		for (Iterator<LocalVariableScope> iterator = localVariableScopes.iterator(); iterator.hasNext();) {
			LocalVariableScope localVariableScope = iterator.next();
			
			if (localVariableScope.name.equals("this")) {
				iterator.remove();
			}
		}
		
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
		final VariableType variableType;
		final int start;
		final int end;
		
		LocalVariableScope(int var, String name, VariableType variableType, int start, int end) {
			this.var = var;
			this.name = name;
			this.variableType = variableType;
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return "LocalVariableScope [var=" + var + ", name=" + name + ", start=" + start + ", end=" + end + "]";
		}
	}
	
}
