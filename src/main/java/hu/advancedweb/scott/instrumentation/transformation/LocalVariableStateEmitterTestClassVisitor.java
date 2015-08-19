package hu.advancedweb.scott.instrumentation.transformation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Manitpulates the class through the appropriate MethodVisitors.
 * @author David Csakvari
 */
public class LocalVariableStateEmitterTestClassVisitor extends ClassVisitor {
	
	public LocalVariableStateEmitterTestClassVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {                               
	    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
	    LocalVariableStateEmitterTestMethodVisitor variableMutationEventEmitter = new LocalVariableStateEmitterTestMethodVisitor(methodVisitor);
	    MethodVisitor variableExtractor = new LocalVariableScopeExtractorTestMethodVisitor(variableMutationEventEmitter, access, name, desc, signature, exceptions);
	    return variableExtractor;
	}

}