package hu.advancedweb.scott.instrumentation.transformation;


import java.lang.instrument.UnmodifiableClassException;

import hu.advancedweb.scott.runtime.ScottReportingRule;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

/**
 * Transformes the constructor to instantiate the `scottReportingRule` field.
 * 
 * @author David Csakvari
 */
public class ConstructorTransformerMethodVisitor extends MethodNode {

	private String className;
	private MethodVisitor next;

	public ConstructorTransformerMethodVisitor(MethodVisitor next, final int access, final String name, final String desc, final String signature, final String[] exceptions, String className) {
		super(Opcodes.ASM7, access, name, desc, signature, exceptions);
		this.next = next;
		this.className = className;
	}
	
	@Override
	public void visitInsn(int opcode) {
		if ((opcode == Opcodes.ARETURN) || (opcode == Opcodes.IRETURN)
				|| (opcode == Opcodes.LRETURN)
				|| (opcode == Opcodes.FRETURN)
				|| (opcode == Opcodes.DRETURN)) {
			throw new RuntimeException(new UnmodifiableClassException("Constructors are supposed to return void"));
		}
		if (opcode == Opcodes.RETURN) {
			super.visitVarInsn(Opcodes.ALOAD, 0);
			super.visitTypeInsn(Opcodes.NEW, Type.getInternalName(ScottReportingRule.class));
			super.visitInsn(Opcodes.DUP);
			super.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(ScottReportingRule.class), "<init>", "()V", false);

			super.visitFieldInsn(Opcodes.PUTFIELD,
					className, "scottReportingRule",
					Type.getDescriptor(ScottReportingRule.class));
		}
		
		super.visitInsn(opcode);
	}
	
	@Override
	public void visitEnd() {
		super.visitEnd();
		accept(next);
	}
	
}
