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

	private InstrumentationActions.Builder transformationParameters;
	private Configuration configuration;
	private String methodName;
	private String methodDesc;
	private String methodSignature;

	private List<String> methodAnnotationFqns = new ArrayList<String>();
	private List<String> classAnnotationFqns;
	private Set<Integer> lineNumbers = new HashSet<Integer>();

	DiscoveryMethodVisitor(MethodVisitor mv, InstrumentationActions.Builder transformationParameters, Configuration
			configuration, String name, String desc, String signature, List<String> classAnnotationFqns) {
		super(Opcodes.ASM7, mv);
		this.transformationParameters = transformationParameters;
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
	public void visitEnd() {
		boolean isLambda = methodName.startsWith("lambda$");
		if (isLambda) {
			if (configuration.isLambdaInstrumentationAllowed(getMethodLoc())) {
				transformationParameters.markLambdaForTracking(methodName, methodDesc, methodSignature);
			}
		} else if (configuration.isMethodInstrumentationAllowed(methodName, getMethodLoc(), methodAnnotationFqns, classAnnotationFqns)) {
			transformationParameters.markMethodForTracking(methodName, methodDesc, methodSignature);
		}

		if (configuration.isJUnit4RuleInjectionRequired(methodAnnotationFqns)) {
			transformationParameters.markClassForJUnit4RuleInjection();
		}

		if (configuration.isJUnit5ExtensionInjectionRequired(methodAnnotationFqns)) {
			transformationParameters.markClassForJUnit5ExtensionInjection();
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
