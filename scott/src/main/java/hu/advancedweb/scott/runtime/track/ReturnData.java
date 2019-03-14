package hu.advancedweb.scott.runtime.track;

public class ReturnData extends ContextualData {
	
	public ReturnData(int lineNumber, String methodName) {
		super(lineNumber, methodName);
	}

	@Override
	public String toString() {
		return "ReturnData [lineNumber=" + lineNumber + ", methodName=" + methodName + "]";
	}
	
}
