package runlistener.sourceparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import runlistener.sourceparser.MethodBoundaryExtractor.Bounderies;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

// TODO: clean exception handling
public class SourceParser {
	
	public TestMethodSource loadTestMethodSource(String path, String methodName) {
		CompilationUnit cu = getCompilationUnit(path);
		
		MethodBoundaryExtractor visitor = new MethodBoundaryExtractor(methodName);
		final Bounderies boundary = new Bounderies();
		visitor.visit(cu, boundary);
		
		final TestMethodSource testMethodSource = new TestMethodSource();
		try (Stream<String> lines = Files.lines(Paths.get(path))) { // TODO: remove hackery
			lines.skip(boundary.beginLine - 1).forEach(new Consumer<String>() {
				@Override
				public void accept(String sourceLine) {
					if (boundary.beginLine <= boundary.endLine)
						testMethodSource.addLine(boundary.beginLine, sourceLine);
					boundary.beginLine++;
				}
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return testMethodSource;
	}
	
	private CompilationUnit getCompilationUnit(String testSourcePath) {
		InputStream in = null;
		CompilationUnit cu = null;
		try {
			in = new FileInputStream(new File(testSourcePath));
			cu = JavaParser.parse(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return cu;
	}


}
