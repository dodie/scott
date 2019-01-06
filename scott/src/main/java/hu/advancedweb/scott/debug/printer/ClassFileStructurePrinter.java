package hu.advancedweb.scott.debug.printer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class ClassFileStructurePrinter {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Incorrect number of arguments. Usage:");
			System.err.println(" java ClassFileStructurePrinter <source_class_path>");
			System.exit(1);
		}
		
		Path sourceClass = Paths.get(args[0]);
		byte[] bytecode = Files.readAllBytes(sourceClass);
		viewByteCode(bytecode);
	}
	
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
