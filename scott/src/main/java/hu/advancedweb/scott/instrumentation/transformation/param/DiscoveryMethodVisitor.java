package hu.advancedweb.scott.instrumentation.transformation.param;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Determines the instrumentation rules based on method properties.
 * 
 * @author David Csakvari
 */
public class DiscoveryMethodVisitor extends MethodVisitor {

	private InstrumentationActions.Builder instrumentationActions;
	private Configuration configuration;
	private String methodName;
	private String methodDesc;
	private String methodSignature;

	private List<String> methodAnnotationFqns = new ArrayList<>();
	private List<String> classAnnotationFqns;
	private Set<Integer> lineNumbers = new HashSet<>();

	DiscoveryMethodVisitor(MethodVisitor mv, InstrumentationActions.Builder instrumentationActions,
			Configuration configuration, String name, String desc, String signature, List<String> classAnnotationFqns) {
		super(Opcodes.ASM7, mv);
		this.instrumentationActions = instrumentationActions;
		this.configuration = configuration;
		this.methodName = name;
		this.methodDesc = desc;
		this.methodSignature = signature;
		this.classAnnotationFqns = classAnnotationFqns;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		lineNumbers.add(line);
		super.visitLineNumber(line, start);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		methodAnnotationFqns.add(Type.getType(descriptor).getClassName());
		return super.visitAnnotation(descriptor, visible);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		if (isScottInstrumentedTrackMethodInst(opcode, owner, name)) {
			instrumentationActions.alreadyInstrumented(true);
		}
		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
	}

	private boolean isScottInstrumentedTrackMethodInst(int opcode, String owner, String name) {
		return Opcodes.INVOKESTATIC == opcode && 
				configuration.getTrackerClass().replace('.', '/').equals(owner) &&
				name.startsWith("track");
	}
	
	@Override
	public void visitEnd() {
		boolean isLambda = methodName.startsWith("lambda$");
		if (isLambda) {
			if (configuration.isLambdaInstrumentationAllowed(getMethodLoc())) {
				instrumentationActions.markLambdaForTracking(methodName, methodDesc, methodSignature);
			}
		} else if (configuration.isMethodInstrumentationAllowed(methodName, getMethodLoc(), methodAnnotationFqns, classAnnotationFqns)) {
			instrumentationActions.markMethodForTracking(methodName, methodDesc, methodSignature);
		}

		if (configuration.isJUnit4RuleInjectionRequired(methodAnnotationFqns)) {
			instrumentationActions.markClassForJUnit4RuleInjection();
		}

		if (configuration.isJUnit5ExtensionInjectionRequired(methodAnnotationFqns)) {
			instrumentationActions.markClassForJUnit5ExtensionInjection();
		}

		super.visitEnd();
	}

	private int getMethodLoc() {
		if (lineNumbers.isEmpty()) {
			return 0;
		}

		int firstLine = Integer.MAX_VALUE;
		int lastLine = 0;

		for (int lineNumber: lineNumbers) {
			if (lineNumber < firstLine) {
				firstLine = lineNumber;
			}
			if (lastLine < lineNumber) {
				lastLine = lineNumber;
			}
		}

		return lastLine - firstLine + 1;
	}
	
}
