package hu.advancedweb.scott.runtime.javasource;

import hu.advancedweb.scott.runtime.javasource.MethodBoundaryExtractor.Bounderies;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

class MethodBoundaryExtractor extends VoidVisitorAdapter<Bounderies> {

	private final String methodName;

	public MethodBoundaryExtractor(String methodName) {
		this.methodName = methodName;
	}

	public void visit(MethodDeclaration methodDeclaration, Bounderies boundaries) {
		if (methodDeclaration.getName().equals(methodName)) {
			boundaries.beginLine = methodDeclaration.getBeginLine();
			boundaries.endLine = methodDeclaration.getEndLine();
		}
	}
	
	public final static class Bounderies {
		int beginLine;
		int endLine;
	}
	
}
