package hu.advancedweb.scott.debug.runner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hu.advancedweb.scott.instrumentation.transformation.TestClassTransformer;

public class TestClassTransformerRunner {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Incorrect number of arguments. Usage:");
			System.err.println(" java TestClassTransformer <source_class_path> <output_path>");
			System.exit(1);
		}
		Path sourceClass = Paths.get(args[0]);
		
		byte[] originalClass = Files.readAllBytes(sourceClass);
		byte[] instrumentedClass = new TestClassTransformer().transform(TestClassTransformer.class.getClassLoader(), "the class specified", null, null, originalClass);

		Path output = Paths.get(args[1]);
		Files.write(output, instrumentedClass);
	}
	
}
