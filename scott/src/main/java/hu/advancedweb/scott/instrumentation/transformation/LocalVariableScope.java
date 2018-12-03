package hu.advancedweb.scott.instrumentation.transformation;

import java.util.List;

/**
 * Metadata of a local variable declared in a test case.
 * 
 * @author David Csakvari
 */
class LocalVariableScope {

	final int var;
	final String name;
	final VariableType variableType;
	final int start;
	final int end;
	final int startIndex;
	final int endIndex;
	final List<Integer> additionalIndexes;
	
	LocalVariableScope(int var, String name, VariableType variableType, int start, int end, int startIndex, int endIndex, List<Integer> additionalIndexes) {
		this.var = var;
		this.name = name;
		this.variableType = variableType;
		this.start = start;
		this.end = end;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.additionalIndexes = additionalIndexes;
	}

	@Override
	public String toString() {
		return "LocalVariableScope [var=" + var + ", name=" + name + ", variableType=" + variableType + ", start=" + start + ", end=" + end + ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", additionalIndexes=" + additionalIndexes + "]";
	}

}
