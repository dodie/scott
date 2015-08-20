package hu.advancedweb.scott.instrumentation.transformation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


/**
 * Transform classes for detailed failure test reports.
 * 
 * @author David Csakvari
 */
public class TestClassTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
	    ClassReader classReader = new ClassReader(classfileBuffer);
	    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
	    ClassVisitor classVisitor = new LocalVariableStateEmitterTestClassVisitor(classWriter);
	    classReader.accept(classVisitor, 0);
	    return classWriter.toByteArray();
	}

}
