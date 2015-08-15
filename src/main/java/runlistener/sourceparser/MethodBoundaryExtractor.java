package runlistener.sourceparser;

import runlistener.sourceparser.MethodBoundaryExtractor.Bounderies;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

class MethodBoundaryExtractor extends VoidVisitorAdapter<Bounderies> {

	private final String methodName;

	public MethodBoundaryExtractor(String methodName) {
		this.methodName = methodName;
	}

	public void visit(MethodDeclaration n, Bounderies boundaries) {
		if (n.getName().equals(methodName)) {
			boundaries.beginLine = n.getBeginLine();
			boundaries.endLine = n.getEndLine();
		}
	}
	
	public final static class Bounderies {
		int beginLine;
		int endLine;
	}
	
}
