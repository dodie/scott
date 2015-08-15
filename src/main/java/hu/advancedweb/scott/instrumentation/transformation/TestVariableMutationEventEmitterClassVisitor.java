package hu.advancedweb.scott.instrumentation.transformation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class TestVariableMutationEventEmitterClassVisitor extends ClassVisitor {
	
	public TestVariableMutationEventEmitterClassVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {                               
	    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
	    return new TestVariableMutationEventEmitterMethodVisitor(methodVisitor);
	}

}