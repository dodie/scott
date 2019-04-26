package hu.advancedweb.scott.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class ClassFileStructurePrinter {

	public static void viewByteCode(byte[] bytecode) {
		ClassReader classReader = new ClassReader(bytecode);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		final List<MethodNode> methodNodes = classNode.methods;
		Printer printer = new Textifier();
		TraceMethodVisitor traceMethodVisitor = new TraceMethodVisitor(printer);
		
		for (MethodNode methodNode : methodNodes) {
			InsnList insnList = methodNode.instructions;
			System.out.println(methodNode.name);
			for (int i = 0; i < insnList.size(); i++) {
				insnList.get(i).accept(traceMethodVisitor);
				StringWriter sw = new StringWriter();
				printer.print(new PrintWriter(sw));
				printer.getText().clear();
				System.out.print(sw.toString());
			}
		}
	}

}
