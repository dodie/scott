package hu.advancedweb.scott.runtime.report.javasource;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import hu.advancedweb.scott.runtime.report.javasource.MethodBoundaryExtractor.Bounderies;

/**
 * Extracts the begin and the end line for a method.
 * 
 * @author David Csakvari
 */
class MethodBoundaryExtractor extends VoidVisitorAdapter<Bounderies> {

	private final String methodName;
	private final String className;
	
	public MethodBoundaryExtractor(String className, String methodName) {
		this.methodName = methodName;
		this.className = className;
	}
	
	@Override
	public void visit(MethodDeclaration methodDeclaration, Bounderies boundaries) {
		if (methodDeclaration.getName().equals(methodName) && isInTheCorrectClass(methodDeclaration)) {
			boundaries.beginLine = methodDeclaration.getRange().begin.line;
			boundaries.endLine = methodDeclaration.getRange().end.line;
		}
	}
	
	private boolean isInTheCorrectClass(MethodDeclaration methodDeclaration) {
		Node n = methodDeclaration;
		
		String containingClassName = "";
		while (n != null) {
			if (n instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) n;
				containingClassName = c.getName() + "$" + containingClassName;
			}
			n = n.getParentNode();
		}
		
		containingClassName = containingClassName.substring(0, containingClassName.length() - 1);
		return containingClassName.equals(className);
	}

	public static final class Bounderies {
		int beginLine;
		int endLine;
	}
	
}
