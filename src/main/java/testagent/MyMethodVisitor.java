package testagent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Instrument the method if it is a test-case.
 * @author David Csakvari
 */
public class MyMethodVisitor extends MethodVisitor {

	// TODO
	// For now, we are going to igonre before and after metods, augment just pure junit test methods
	
	boolean isTestCase;
	int lineNumber;

	public MyMethodVisitor(MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

	@Override
	public void visitLineNumber(int lineNumber, Label label) {
		this.lineNumber = lineNumber;
		super.visitLineNumber(lineNumber, label);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		isTestCase = "Lorg/junit/Test;".equals(desc);
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		
		boolean isStoreOperation = opcode == Opcodes.ISTORE ||
				opcode == Opcodes.LSTORE ||
				opcode == Opcodes.FSTORE ||
				opcode == Opcodes.DSTORE ||
				opcode == Opcodes.ASTORE;
		
		if (isStoreOperation) {
			instument(var);
		}
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		instument(var);
	}
	
	private void instument(int var) {
		if (isTestCase) {
			// TODO
			// instrument code to store assignment and assertion values (a string representation)
			// along with the related source-code line
			// augment the test code to make junit produce more detailed infos based on the collected information
			// and the sourcecode of the test
			
			// TODO
			// It seems that Junit does not provide infrastructure for this kind of job, so Im gona use a static helper class 
			// to store the collected data, and a run listener to process it
			
			// helpful article:
			// http://www.geekyarticles.com/2011/10/manipulating-java-class-files-with-asm.html
		
			super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
	        super.visitLdcInsn("Assigning to variable ( " + var + " ) in line: " + lineNumber);
	        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		}	
	}
	
}