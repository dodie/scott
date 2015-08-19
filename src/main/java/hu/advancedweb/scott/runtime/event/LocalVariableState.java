package hu.advancedweb.scott.runtime.event;

public class LocalVariableState {
	public final int lineNumber;
	public final int var;
	public final String value;
	
	public LocalVariableState(int lineNumber, String value, int var) {
		this.lineNumber = lineNumber;
		this.value = value;
		this.var = var;
	}
}
