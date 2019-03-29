package hu.advancedweb.scott.instrumentation;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;

/**
 * Transformer that enables directly transforming class files in the given folder.
 * It makes possible possible to integrate Scott as a post build step.
 *
 * When using it, make sure to have all runtime dependencies of the code available
 * on the classpath. For more information, see Issue #79
 *
 * @author David Csakvari
 */
public class ScottClassFileTransformer {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Exactly one argument is required.");
		}
		final String rootPath = args[0];
		final PathMatcher classMatcher = FileSystems.getDefault().getPathMatcher("glob:**.class");

		Files.walkFileTree(Paths.get(rootPath), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				if (attrs.isRegularFile() && classMatcher.matches(path)) {
					transformClass(path);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static void transformClass(Path path) {
		try {
			byte[] originalClass = Files.readAllBytes(path);
			byte[] instrumentedClass = new ScottClassTransformer().transform(originalClass, ScottConfigurer.getConfiguration());
			Files.write(path, instrumentedClass);
		} catch (Exception e) {
			System.err.println("Could not instrument " + path);
			e.printStackTrace();
		}
	}

}
