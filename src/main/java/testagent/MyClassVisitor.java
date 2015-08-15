package testagent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class MyClassVisitor extends ClassVisitor {
	
	public MyClassVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}
	
//	public void visitSource(String file, String debug) {
//		// TODO
//		// I am not sure if I should keep it or not.
//		// Maybe I can use the file parameter and class FQN to find the source file
//		// Actual source file name might be useful for handling inner classes.
//		super.visitSource(file, debug);
//	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {                               
	    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
	    return new MyMethodVisitor(methodVisitor);
	}

}