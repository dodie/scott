package hu.advancedweb.scott.runtime.track;

public class ReturnValueData extends ReturnData {
	
	public final String value;
	
	public ReturnValueData(int lineNumber, String methodName, String value) {
		super(lineNumber, methodName);
		this.value = value;
	}

	@Override
	public String toString() {
		return "ReturnValueData [lineNumber=" + lineNumber + ", methodName=" + methodName + ", value=" + value + "]";
	}
	
}
