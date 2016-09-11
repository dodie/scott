package hu.advancedweb.scott.instrumentation.transformation.param;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
			boolean isTestCase = "Lorg/junit/Test;".equals(annotationDesc);
			
			if (isTestCase) {
				transformationParameters.isRuleInjectionRequired = true;
				transformationParameters.markMethodForInstrumentation(methodName, methodDesc, methodSignature);
			}
		}
		super.visitEnd();
	}
	
}