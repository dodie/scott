package hu.advancedweb.scott.runtime.report.javasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

import hu.advancedweb.scott.runtime.report.javasource.MethodBoundaryExtractor.Bounderies;

/**
 * Contains the source code of a test method.
 * 
 * @author David Csakvari
 */
public class MethodSource {
	
	private final String path;
	private final String methodName;
	private int beginLine;
	private List<String> reportLines = new ArrayList<String>();
	private String className;

	
	public MethodSource(String path, String className, String methodName) throws IOException {
		this.path = path;
		this.className = className;
		this.methodName = methodName;
	
		CompilationUnit cu = getCompilationUnit(path);
		
		MethodBoundaryExtractor visitor = new MethodBoundaryExtractor(methodName);
		final Bounderies boundary = new Bounderies();
		visitor.visit(cu, boundary);
		
		beginLine = boundary.beginLine;
		
		List<String> lines = Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
		for (int i = boundary.beginLine - 1; i < boundary.endLine; i++) {
			reportLines.add(lines.get(i));
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
	
	public String getPath() {
		return path;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public int getBeginLine() {
		return beginLine;
	}
	
	public List<String> getReportLines() {
		return reportLines;
	}

}
