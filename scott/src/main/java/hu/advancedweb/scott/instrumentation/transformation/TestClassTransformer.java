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
 * Transform classes for detailed failure test reports.
 * 
 * @author David Csakvari
 */
public class TestClassTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
	    TransformationParameters.Builder transformationParameters = new TransformationParameters.Builder();
	    ClassVisitor discoveryClassVisitor = new DiscoveryClassVisitor(transformationParameters);
	    new ClassReader(classfileBuffer).accept(discoveryClassVisitor, 0);
	    
	    ClassReader classReader = new ClassReader(classfileBuffer);
	    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
	    ClassVisitor localVariableStateEmitterTestClassVisitor = new LocalVariableStateEmitterTestClassVisitor(classWriter, transformationParameters.build());
	    classReader.accept(localVariableStateEmitterTestClassVisitor, 0);
	    return classWriter.toByteArray();
	}

}
