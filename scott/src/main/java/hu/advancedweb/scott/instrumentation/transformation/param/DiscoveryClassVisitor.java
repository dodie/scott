package hu.advancedweb.scott.instrumentation.transformation.param;


import java.util.ArrayList;
import java.util.List;

import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Determines what classes and methods to instrument.
 * 
 * @author David Csakvari
 */
public class DiscoveryClassVisitor extends ClassVisitor {

	private Configuration configuration;

	private InstrumentationActions.Builder transformationParameters;

	private String classFqn;
	private List<String> annotationFqns = new ArrayList<>();


	public DiscoveryClassVisitor(Configuration configuration) {
		super(Opcodes.ASM7);
		this.configuration = configuration;
		this.transformationParameters = new InstrumentationActions.Builder();
	}

	public InstrumentationActions.Builder getTransformationParameters() {
		return transformationParameters;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.classFqn = Type.getType("L" + name + ";").getClassName();
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		annotationFqns.add(Type.getType(descriptor).getClassName());
		return super.visitAnnotation(descriptor, visible);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
		return new DiscoveryMethodVisitor(methodVisitor, transformationParameters, configuration, name, desc, signature, annotationFqns);
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
		transformationParameters.setTrackerClass(configuration.getTrackerClass().replace('.', '/'));
		transformationParameters.includeClass(configuration.isClassInstrumentationAllowed(classFqn, annotationFqns));
		transformationParameters.trackMethodStart(configuration.isTrackMethodStart());
		transformationParameters.trackReturn(configuration.isTrackReturn());
		transformationParameters.trackUnhandledException(configuration.isTrackUnhandledException());
		transformationParameters.trackLocalVariableAssignments(configuration.isTrackLocalVariableAssignments());
		transformationParameters.trackLocalVariableIncrements(configuration.isTrackLocalVariableIncrements());
		transformationParameters.trackLocalVariablesAfterEveryMethodCall(configuration.isTrackLocalVariablesAfterEveryMethodCall());
		transformationParameters.trackFieldAssignments(configuration.isTrackFieldAssignments());
		transformationParameters.trackFieldsAfterEveryMethodCall(configuration.isTrackFieldsAfterEveryMethodCall());
		transformationParameters.verboseLogging(configuration.isVerboseLoggingEnabled());

		if (!transformationParameters.anyMethodMarkedForTracking() &&
				configuration.isLambdaInstrumentationAllowedWhenOtherInstrumentationIsInPlace()) {
			transformationParameters.clearTrackedLambdas();
		}
	}

}
