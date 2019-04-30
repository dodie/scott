package hu.advancedweb.scott.runtime.report.javasource;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs Java source lookup.
 * 
 * @author David Csakvari
 */
public class SourcePathResolver {
	
	public String getSourcePath(final String fqn) throws IOException {
		final String currentDir = System.getProperty("user.dir").replace("\\", "/");
		final String relativeFilePath = fqn.replace(".", "/") + ".java";
		
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + currentDir + "/**/" + relativeFilePath);
		final List<String> result = new ArrayList<String>();
		Files.walkFileTree(Paths.get(currentDir), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
				if (matcher.matches(path)) {
					result.add(path.toString());
					return FileVisitResult.TERMINATE;
				} else {
					return FileVisitResult.CONTINUE;
				}
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		if (result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
	}
	
}
