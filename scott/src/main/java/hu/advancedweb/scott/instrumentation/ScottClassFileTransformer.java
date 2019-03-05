package hu.advancedweb.scott.instrumentation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;

/**
 * Transformer that enables directly transforming class files.
 * It makes possible possible to integrate Scott as a post build step.
 * 
 * @author David Csakvari
 */
public class ScottClassFileTransformer {
	
	public static void main(String[] args) throws Exception {
		if (args.length == 1) {
			transformClass(args[0]);
		} else if (args.length == 2) {
			transformClass(args[0], args[1]);
		} else {
			displayErrorAndExit();
		}
	}

	public static void transformClass(String sourcePath) {
		transformClass(sourcePath, sourcePath);
	}

	public static void transformClass(String sourcePath, String targetPath) {
		try {
			Path sourceClass = Paths.get(sourcePath);

			byte[] originalClass = Files.readAllBytes(sourceClass);
			byte[] instrumentedClass = new ScottClassTransformer().transform(originalClass);

			Path output = Paths.get(targetPath);
			Files.write(output, instrumentedClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void displayErrorAndExit() {
		System.err.println("Incorrect number of arguments. Usage:");
		System.err.println("  java TestClassTransformer <source_class_path>");
		System.err.println("  java TestClassTransformer <source_class_path> <output_path>");
		System.exit(1);
	}
	
}
