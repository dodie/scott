package hu.advancedweb.scott.instrumentation.transformation.param;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TestDiscoveryMethodVisitor extends MethodVisitor {

	private TransformationParameters.Builder transformationParameters;

	public TestDiscoveryMethodVisitor(MethodVisitor mv, TransformationParameters.Builder transformationParameters) {
		super(Opcodes.ASM5, mv);
		this.transformationParameters = transformationParameters;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		boolean isTestCase = "Lorg/junit/Test;".equals(desc);
		transformationParameters.isTestClass = transformationParameters.isTestClass || isTestCase;
		return super.visitAnnotation(desc, visible);
	}
	
}