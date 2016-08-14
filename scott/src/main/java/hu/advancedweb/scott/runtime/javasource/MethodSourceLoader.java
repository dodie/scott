package hu.advancedweb.scott.runtime.javasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

import hu.advancedweb.scott.runtime.ScottReport;
import hu.advancedweb.scott.runtime.javasource.MethodBoundaryExtractor.Bounderies;

public class MethodSourceLoader {
	
	private final String path;
	private final String methodName;

	
	public MethodSourceLoader(String path, String methodName) {
		this.path = path;
		this.methodName = methodName;
	}
	
	public void loadMethodSource(ScottReport testMethodSource) {
		try {
			CompilationUnit cu = getCompilationUnit(path);
			
			MethodBoundaryExtractor visitor = new MethodBoundaryExtractor(methodName);
			final Bounderies boundary = new Bounderies();
			visitor.visit(cu, boundary);
			
			testMethodSource.setBeginLine(boundary.beginLine);
			
			List<String> lines = Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
			for (int i = boundary.beginLine - 1; i < boundary.endLine; i++) {
				testMethodSource.addLine(lines.get(i));
			}
		} catch (IOException e) {
			// Ignore.
		}
	}
	
	private CompilationUnit getCompilationUnit(String testSourcePath) throws IOException {
		InputStream in = null;
		CompilationUnit cu = null;
		try {
			in = new FileInputStream(new File(testSourcePath));
			cu = JavaParser.parse(in);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IOException(e);
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
