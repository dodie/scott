package hu.advancedweb.scott.instrumentation.transformation.param;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Determines what classes and methods to instrument.
 * 
 * @author David Csakvari
 */
public class DiscoveryClassVisitor extends ClassVisitor {
	
	private TransformationParameters.Builder transformationParameters;

	public DiscoveryClassVisitor(TransformationParameters.Builder transformationParameters) {
		super(Opcodes.ASM5);
		this.transformationParameters = transformationParameters;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.startsWith("lambda$")) {
			transformationParameters.markLambdaForTracking(name, desc, signature);
		}
	
		MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor testDiscoveryMethodVisitor = new TestDiscoveryMethodVisitor(methodVisitor, transformationParameters, name, desc, signature);
		return testDiscoveryMethodVisitor;
	}
	
}
