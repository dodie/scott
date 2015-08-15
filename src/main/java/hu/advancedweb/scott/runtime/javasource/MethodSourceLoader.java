package hu.advancedweb.scott.runtime.javasource;

import hu.advancedweb.scott.runtime.javasource.MethodBoundaryExtractor.Bounderies;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

// TODO: clean exception handling
public class MethodSourceLoader {
	
	public MethodSource loadMethodSource(String path, String methodName) {
		CompilationUnit cu = getCompilationUnit(path);
		
		MethodBoundaryExtractor visitor = new MethodBoundaryExtractor(methodName);
		final Bounderies boundary = new Bounderies();
		visitor.visit(cu, boundary);
		final MethodSource testMethodSource = new MethodSource(boundary.beginLine);
		
		try {
			List<String> lines = Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
			for (int i = boundary.beginLine - 1; i < boundary.endLine; i++) {
				testMethodSource.addLine(lines.get(i));
			}
		} catch (IOException e) {
			// TODO: later this printStackTrace should be removed, as it is valid scenario to not find the source code
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
			// TODO: later this printStackTrace should be removed, as it is valid scenario to not find the source code
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// Ignore.
			}
		}
		
		return cu;
	}


}
