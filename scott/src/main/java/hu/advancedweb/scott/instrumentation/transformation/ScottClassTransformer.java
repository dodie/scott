package hu.advancedweb.scott.instrumentation.transformation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import hu.advancedweb.scott.instrumentation.transformation.param.DiscoveryClassVisitor;
import hu.advancedweb.scott.instrumentation.transformation.param.TransformationParameters;


/**
 * Entry point for Scott's ASM based bytecode instrumentation.
 * 
 * @author David Csakvari
 */
public class ScottClassTransformer {
	
	public byte[] transform(byte[] classfileBuffer) {
		return transform(classfileBuffer, calculateTransformationParameters(classfileBuffer));
	}

	private byte[] transform(byte[] classfileBuffer, TransformationParameters transformationParameters) {
		ClassReader classReader = new ClassReader(classfileBuffer);
		ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
		ClassVisitor localVariableStateEmitterTestClassVisitor = new StateTrackingTestClassVisitor(classWriter, transformationParameters);
		classReader.accept(localVariableStateEmitterTestClassVisitor, 0);
		return classWriter.toByteArray();
	}
	
	private TransformationParameters calculateTransformationParameters(byte[] classfileBuffer) {
		TransformationParameters.Builder transformationParameters = new TransformationParameters.Builder();
		ClassVisitor discoveryClassVisitor = new DiscoveryClassVisitor(transformationParameters);
		new ClassReader(classfileBuffer).accept(discoveryClassVisitor, 0);
		return transformationParameters.build();
	}

}
