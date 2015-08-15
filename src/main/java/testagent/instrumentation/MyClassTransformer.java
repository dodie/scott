package testagent.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class MyClassTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// TODO: remove this later or refine to detect test classes
//		if (!className.contains("testclient")) {
//			return null;
//		}
		
	    ClassReader classReader = new ClassReader(classfileBuffer);
	    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
	    ClassVisitor classVisitor = new MyClassVisitor(classWriter);
	    classReader.accept(classVisitor, 0);
	    return classWriter.toByteArray();
	}

}
