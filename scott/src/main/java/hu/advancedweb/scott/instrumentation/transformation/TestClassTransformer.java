package hu.advancedweb.scott.instrumentation.transformation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import hu.advancedweb.scott.instrumentation.transformation.param.DiscoveryClassVisitor;
import hu.advancedweb.scott.instrumentation.transformation.param.TransformationParameters;


/**
 * Transforms test classes for detailed failure test reports.
 * 
 * @author David Csakvari
 */
public class TestClassTransformer implements ClassFileTransformer {
	
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (loader == null) {
			/*
			 * Leave the class alone, if it is being loaded by the Bootstrap classloader,
			 * as we don't want to do anything with JDK libs. See Issue #22.
			 */
			return classfileBuffer;
		} else {
			try {
				TransformationParameters transformationParameters = calculateTransformationParameters(classfileBuffer);
			    return transform(classfileBuffer, transformationParameters);
			} catch (Exception e) {
				System.err.println("Scott: test instrumentation failed for " + className + "!");
				e.printStackTrace();
				throw e;
			}
		}
	}

	private TransformationParameters calculateTransformationParameters(byte[] classfileBuffer) {
		TransformationParameters.Builder transformationParameters = new TransformationParameters.Builder();
	    ClassVisitor discoveryClassVisitor = new DiscoveryClassVisitor(transformationParameters);
	    new ClassReader(classfileBuffer).accept(discoveryClassVisitor, 0);
		return transformationParameters.build();
	}
	
	private byte[] transform(byte[] classfileBuffer, TransformationParameters transformationParameters) {
		ClassReader classReader = new ClassReader(classfileBuffer);
		ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
		ClassVisitor localVariableStateEmitterTestClassVisitor = new StateTrackingTestClassVisitor(classWriter, transformationParameters);
		classReader.accept(localVariableStateEmitterTestClassVisitor, 0);
		return classWriter.toByteArray();
	}

}
