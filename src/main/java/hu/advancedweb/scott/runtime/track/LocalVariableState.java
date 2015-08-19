package hu.advancedweb.scott.runtime.track;

/**
 * Represents a local variable state for a variable index and line number.
 * 
 * @author David Csakvari
 */
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
