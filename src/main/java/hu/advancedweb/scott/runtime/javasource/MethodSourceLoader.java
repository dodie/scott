package hu.advancedweb.scott.runtime.javasource;

import hu.advancedweb.scott.runtime.MethodSource;
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
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class MethodSourceLoader {
	
	public MethodSource loadMethodSource(String path, String methodName) {
		try {
			CompilationUnit cu = getCompilationUnit(path);
			
			MethodBoundaryExtractor visitor = new MethodBoundaryExtractor(methodName);
			final Bounderies boundary = new Bounderies();
			visitor.visit(cu, boundary);
			final MethodSource testMethodSource = new MethodSource(boundary.beginLine);
		
			List<String> lines = Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
			for (int i = boundary.beginLine - 1; i < boundary.endLine; i++) {
				testMethodSource.addLine(lines.get(i));
			}
			return testMethodSource;
		} catch (IOException e) {
			// Ignore.
		}
		return null;
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
