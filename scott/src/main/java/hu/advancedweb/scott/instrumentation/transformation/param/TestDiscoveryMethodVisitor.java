package hu.advancedweb.scott.instrumentation.transformation.param;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Determines the instrumentation rules based on method properties.
 * 
 * @author David Csakvari
 */
public class TestDiscoveryMethodVisitor extends MethodVisitor {

	private TransformationParameters.Builder transformationParameters;
	private String methodName;
	private String methodDesc;
	private String methodSignature;

	private List<String> annotations = new ArrayList<>();

	public TestDiscoveryMethodVisitor(MethodVisitor mv, TransformationParameters.Builder transformationParameters, String name, String desc, String signature) {
		super(Opcodes.ASM5, mv);
		this.transformationParameters = transformationParameters;
		this.methodName = name;
		this.methodDesc = desc;
		this.methodSignature = signature;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		annotations.add(desc);
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitEnd() {
		for (String annotationDesc : annotations) {
			if (AnnotationMatcher.match(annotationDesc, "scott.track.method_annotation", new String[] {"org.junit.Test", "org.junit.jupiter.api.Test", "org.junit.jupiter.api.TestFactory", "cucumber.api.java.*"})) {
				transformationParameters.markMethodForTracking(methodName, methodDesc, methodSignature);
				transformationParameters.markMethodForClearingTrackedData(methodName, methodDesc, methodSignature);
			}
			
			if (AnnotationMatcher.match(annotationDesc, "scott.inject_junit4_rule.method_annotation", new String[] {"org.junit.Test"})) {
				transformationParameters.markClassForJUnit4RuleInjection();
			}
			
			if (AnnotationMatcher.match(annotationDesc, "scott.inject_junit5_extension.method_annotation", new String[] {"org.junit.jupiter.api.Test", "org.junit.jupiter.api.TestFactory"})) {
				transformationParameters.markClassForJUnit5ExtensionInjection();
			}
		}
		super.visitEnd();
	}
	
}
