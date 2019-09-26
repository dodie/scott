package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hu.advancedweb.scott.instrumentation.transformation.param.InstrumentationActions;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Instruments test methods to call Scott Runtime to track variable states.
 * 
 * @author David Csakvari
 */
public class StateTrackingMethodVisitor extends MethodVisitor {

	private int lineNumber;
	
	private int lineNumberForMethodCallTrack;

	private Set<Label> visitedLabels = new HashSet<>();

	private Label startFinally = new Label();

	private Set<Integer> localVariables = new HashSet<>();

	private List<LocalVariableScope> localVariableScopes = new ArrayList<>();

	private Set<AccessedField> accessedFields;
	
	private InstrumentationActions instrumentationActions;

	private String methodName;

	private String className;
	
	private String desc;
	
	private Logger logger;


	StateTrackingMethodVisitor(MethodVisitor mv, InstrumentationActions instrumentationActions, String className, String methodName, String desc) {
		super(Opcodes.ASM7, mv);
		
		this.logger = new Logger(instrumentationActions.verboseLogging);
		
		logger.log("Visiting: " + className + "." + methodName);

		this.instrumentationActions = instrumentationActions;
		this.className = className;
		this.methodName = methodName;
		this.desc = desc;
	}
	
	@Override
	public void visitCode() {
		super.visitCode();

		if (instrumentationActions.trackUnhandledException) {
			instrumentToAddOpeningLabelForEnclosingTry();
		}
		
		if (instrumentationActions.trackMethodStart) {
			// track method start
			instrumentToTrackMethodStart();

			if (instrumentationActions.trackFieldsAfterEveryMethodCall || instrumentationActions.trackFieldAssignments) {
				// track initial field states
				for (AccessedField accessedField : accessedFields) {
					instrumentToTrackFieldState(accessedField, lineNumber);
				}
			}

			// track method arguments
			for (LocalVariableScope localVariableScope : localVariableScopes) {
				if (localVariableScope.start == 0) {
					instrumentToTrackVariableState(localVariableScope, lineNumber);
				}
			}

			// signal that there are no more argument and initial field state tracks,
			// so the runtime can transform them into a single event if required, see Issue #70.
			instrumentToTrackEndOfArgumentsAtMethodStart();
		}
	}
	
	@Override
	public void visitLabel(Label label) {
		super.visitLabel(label);
		this.visitedLabels.add(label);
	}
	
	@Override
	public void visitLineNumber(int lineNumber, Label label) {
		this.lineNumberForMethodCallTrack = this.lineNumber;
		this.lineNumber = lineNumber;
		super.visitLineNumber(lineNumber, label);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		localVariables.clear();
		visitedLabels.clear();
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		/*
		 * Track where lambda expressions are defined.
		 */
		if (instrumentationActions.anyLambdaMarkedForTracking()) {
			if ("java/lang/invoke/LambdaMetafactory".equals(bsm.getOwner()) && bsmArgs[1] instanceof Handle) {
				Handle handle = (Handle)bsmArgs[1];
				if (handle.getName().startsWith("lambda$")) {
					instrumentToTrackLambdaDefinition(handle.getName(), lineNumber);
				}
			}
		}
		
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}
	
	@Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (this.lineNumberForMethodCallTrack == 0) {
			this.lineNumberForMethodCallTrack = this.lineNumber;
		}
		
		if (!owner.startsWith("org/mockito")) {
			if (instrumentationActions.trackLocalVariablesAfterEveryMethodCall) {
				// track every variable state after method calls
				for (LocalVariableScope localVariableScope : localVariableScopes) {
					if (!localVariables.contains(localVariableScope.var))
						continue;

					if (isVariableInScope(localVariableScope.var)) {
						instrumentToTrackVariableState(localVariableScope, lineNumberForMethodCallTrack);
					}
				}
			}

			if (instrumentationActions.trackFieldsAfterEveryMethodCall) {
				// track every field state after method calls
				for (AccessedField accessedField : accessedFields) {
					instrumentToTrackFieldState(accessedField, lineNumberForMethodCallTrack);
				}
			}
		}
		
		this.lineNumberForMethodCallTrack = this.lineNumber;
		
		/*
		 * Visit the method instruction after placing the tracking code
		 * because otherwise we might confuse Mockito, see Issue #25.
		 * Because of this, the tracking code has to book the values to the previous line
		 * for the first method call for every line.
		 */
		super.visitMethodInsn(opcode, owner, name, desc, itf);
    }
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);

		if (!instrumentationActions.trackLocalVariableAssignments) {
			return;
		}

		if (!VariableType.isStoreOperation(opcode)) {
			// Only track variable state and name at variable stores. (Typical variable assignments.)
			return;
		}

		localVariables.add(var);

		LocalVariableScope lvs = getLocalVariableScope(var);
		if (lvs != null) {
			/*
			 * This null-check is the workaround for issue #15:
			 * If a variable declaration is the last statement in a code block,
			 * then the variable name is not present in the compiled bytecode.
			 * With this workaround Scott can still track the assigned value to such variables.
			 */
			instrumentToTrackVariableState(lvs, lineNumber);
		}
	}
	
	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);

		if (instrumentationActions.trackLocalVariableIncrements) {
			// Track variable state at variable increases (e.g. i++).
			LocalVariableScope lvs = getLocalVariableScope(var);
			instrumentToTrackVariableState(lvs, lineNumber);
		}
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);

		if (!instrumentationActions.trackFieldAssignments) {
			return;
		}

		if (Opcodes.PUTFIELD != opcode && Opcodes.PUTSTATIC != opcode) {
			// Only track field state and name at stores (assignments).
			return;
		}

		for (AccessedField accessedField : accessedFields) {
			if (accessedField.name.equals(name)) {
				instrumentToTrackFieldState(accessedField, lineNumber);
				break;
			}
		}
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		if (instrumentationActions.trackUnhandledException) {
			instrumentToTrackUnhandledExceptions();
		}
		super.visitMaxs(maxStack, maxLocals);
	}
	
	@Override
	public void visitInsn(int opcode) {
		if (instrumentationActions.trackReturn) {
			instrumentToTrackReturn(opcode);
		}
		super.visitInsn(opcode);
	}

	private void instrumentToTrackMethodStart() {
		logger.log(" - instrumentToTrackMethodStart");
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(methodName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackMethodStart", "(ILjava/lang/String;Ljava/lang/Class;)V", false);
	}
	
	private void instrumentToTrackEndOfArgumentsAtMethodStart() {
		logger.log(" - instrumentToTrackEndOfArgumentsAtMethodStart");
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(methodName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackEndOfArgumentsAtMethodStart", "(ILjava/lang/String;Ljava/lang/Class;)V", false);
	}
	
	private void instrumentToTrackLambdaDefinition(String handleName, int lineNumber) {
		logger.log(" - instrumentToTrackLambdaDefinition of " + handleName + " at " + lineNumber);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(handleName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackLambdaDefinition", "(ILjava/lang/String;Ljava/lang/Class;)V", false);
	}
	
	private void instrumentToTrackVariableState(LocalVariableScope localVariableScope, int lineNumber) {
		logger.log(" - instrumentToTrackVariableState of variable at " + getLineNumberBoundedByScope(lineNumber, localVariableScope) + ": " + localVariableScope);
		super.visitVarInsn(localVariableScope.variableType.loadOpcode, localVariableScope.var);
		super.visitLdcInsn(localVariableScope.name);
		super.visitLdcInsn(getLineNumberBoundedByScope(lineNumber, localVariableScope));
		super.visitLdcInsn(methodName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackLocalVariableState", "(" + localVariableScope.variableType.desc + "Ljava/lang/String;ILjava/lang/String;Ljava/lang/Class;)V", false);
	}
	
	private int getLineNumberBoundedByScope(int lineNumber, LocalVariableScope localVariableScope) {
		return Math.min(localVariableScope.end, Math.max(lineNumber, localVariableScope.start));
	}
	
	private void instrumentToTrackFieldState(AccessedField accessedField, int lineNumber) {
		if (accessedField.isStatic) {
			instrumentToTrackStaticFieldState(accessedField, lineNumber);
		} else {
			instrumentToTrackInstanceFieldState(accessedField, lineNumber);
		}
	}
	
	private void instrumentToTrackInstanceFieldState(AccessedField accessedField, int lineNumber) {
		if (!isCurrentClassIsFieldOwnerOrInnerClassOfFieldOwner(accessedField.owner)) {
			return;
		}
		
		logger.log(" - instrumentToTrackFieldState at " + lineNumber + ": " + accessedField);
		
		/*
		 * Put owner to the stack.
		 * To do this, first put the current object ("this") to the stack.
		 * Then check if the field is owned by the class of this object. If it is, then we are done.
		 * Else, put the enclosing object to the stack, and check again.
		 * Repeat the last step until the owner is found and on the stack.
		 */
		super.visitVarInsn(Opcodes.ALOAD, 0);
		
		String currentNestedClassName = className;
		while(!currentNestedClassName.equals(accessedField.owner)) {
			String enclosingClassName = currentNestedClassName.substring(0, currentNestedClassName.lastIndexOf('$'));
			int nestingLevel = currentNestedClassName.length() - currentNestedClassName.replace("$", "").length() - 1;
			super.visitFieldInsn(Opcodes.GETFIELD, currentNestedClassName, "this$" + nestingLevel, "L" + enclosingClassName + ";");
			currentNestedClassName = enclosingClassName;
		}
		
		// Put field value to the stack
		super.visitFieldInsn(Opcodes.GETFIELD, accessedField.owner, accessedField.name, accessedField.desc);
		
		// Put other params to the stack
		super.visitLdcInsn(accessedField.name);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(methodName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitLdcInsn(accessedField.isStatic);
		super.visitLdcInsn(accessedField.owner);
		
		// Call tracking code
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackFieldState", "(" + getFieldDescriptor(accessedField) + "Ljava/lang/String;ILjava/lang/String;Ljava/lang/Class;ZLjava/lang/String;)V", false);
	}
	
	private boolean isCurrentClassIsFieldOwnerOrInnerClassOfFieldOwner(String owner) {
		return className.equals(owner) || className.startsWith(owner + "$");
	}

	private void instrumentToTrackStaticFieldState(AccessedField accessedField, int lineNumber) {
		logger.log(" - instrumentToTrackFieldState (static) at " + lineNumber + ": " + accessedField);
		
		// Put field value to the stack
		super.visitFieldInsn(Opcodes.GETSTATIC, accessedField.owner, accessedField.name, accessedField.desc);
		
		// Put other params to the stack
		super.visitLdcInsn(accessedField.name);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(methodName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitLdcInsn(accessedField.isStatic);
		super.visitLdcInsn(accessedField.owner);
		
		// Call tracking code
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackFieldState", "(" + getFieldDescriptor(accessedField) + "Ljava/lang/String;ILjava/lang/String;Ljava/lang/Class;ZLjava/lang/String;)V", false);
	}
	
	private void instrumentToAddOpeningLabelForEnclosingTry() {
		logger.log(" - instrumentToAddOpeningLabelForEnclosingTry");
		super.visitLabel(startFinally);
	}
	
	private void instrumentToTrackUnhandledExceptions() {
		// Based on: http://modularity.info/conference/2007/program/industry/I5-UsingASMFramework.pdf
		logger.log(" - instrumentToTrackUnhandledExceptions");

		Label endFinally = new Label();
		super.visitTryCatchBlock(startFinally, endFinally, endFinally, "java/lang/Throwable");
		super.visitLabel(endFinally);

		// Record exception
		super.visitInsn(Opcodes.DUP);
		super.visitLdcInsn(lineNumber);
		super.visitLdcInsn(methodName);
		super.visitLdcInsn(Type.getType("L" + className + ";"));
		super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackUnhandledException", "(Ljava/lang/Throwable;ILjava/lang/String;Ljava/lang/Class;)V", false);

		// Rethrow
		super.visitInsn(Opcodes.ATHROW);
	}
	
	private void instrumentToTrackReturn(int opcode) {
		if (!VariableType.isReturnOperation(opcode)) {
			return;
		}
		
		if (Opcodes.RETURN == opcode) {
			super.visitLdcInsn(lineNumber);
			super.visitLdcInsn(methodName);
			super.visitLdcInsn(Type.getType("L" + className + ";"));
			super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackReturn", "(ILjava/lang/String;Ljava/lang/Class;)V", false);
		} else {
			if (opcode == Opcodes.DRETURN || opcode == Opcodes.LRETURN) {
				super.visitInsn(Opcodes.DUP2);
			} else {
				super.visitInsn(Opcodes.DUP);
			}
			
			final VariableType variableType;
			if (opcode == Opcodes.IRETURN) {
				variableType = VariableType.getReturnTypeFromMethodDesc(desc);
			} else {
				variableType = VariableType.getByReturnOpCode(opcode);
			}
			
			super.visitLdcInsn(lineNumber);
			super.visitLdcInsn(methodName);
			super.visitLdcInsn(Type.getType("L" + className + ";"));
			super.visitMethodInsn(Opcodes.INVOKESTATIC, instrumentationActions.trackerClass, "trackReturn", "(" + variableType.desc + "ILjava/lang/String;Ljava/lang/Class;)V", false);
		}
	}
	
	private String getFieldDescriptor(AccessedField accessedField) {
		if (accessedField.desc.startsWith("L") || accessedField.desc.startsWith("[")) {
			return VariableType.REFERENCE.desc;
		} else {
			return accessedField.desc;
		}
	}
	
	private boolean isVariableInScope(int var) {
		return getLocalVariableScope(var) != null;
	}
	
	private LocalVariableScope getLocalVariableScope(int var) {
		for (LocalVariableScope localVariableScope : localVariableScopes) {
			if (localVariableScope.var == var) {
				boolean inScope = ((visitedLabels.size() - 1) >= localVariableScope.startIndex &&
						(visitedLabels.size() - 1) < localVariableScope.endIndex)
						||  localVariableScope.additionalIndexes.contains(visitedLabels.size() - 1);
				
				if (inScope) {
					return localVariableScope;
				}
			}
		}
		return null;
	}
	
	void setMethodStartLineNumber(Integer methodStartLineNumber) {
		this.lineNumber = methodStartLineNumber != null ? methodStartLineNumber : 0;
	}
	
	void setLocalVariableScopes(List<LocalVariableScope> localVariableScopes) {
		this.localVariableScopes = localVariableScopes;
	}
	
	void setAccessedFields(Set<AccessedField> accessedFields) {
		this.accessedFields = accessedFields;
	}
	
}
