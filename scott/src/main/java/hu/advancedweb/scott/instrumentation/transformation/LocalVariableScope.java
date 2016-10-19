package hu.advancedweb.scott.instrumentation.transformation;

class LocalVariableScope {

	final int var;
	final String name;
	final VariableType variableType;
	final int start;
	final int end;
	
	LocalVariableScope(int var, String name, VariableType variableType, int start, int end) {
		this.var = var;
		this.name = name;
		this.variableType = variableType;
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return "LocalVariableScope [var=" + var + ", name=" + name + ", start=" + start + ", end=" + end + "]";
	}

}
