package hu.advancedweb.scott.runtime.track;

/**
 * Represents a data point collected at a given line number 
 * that contains a local variable state and an unique identifier for the variable.
 * 
 * @author David Csakvari
 */
public class LocalVariableState {

	/** Line number where the data is collected. */
	public final int lineNumber;

	/** Unique identifier for the variable. Because how we track variables in lambdas, it's composed of the variable index and the name of the containing method. */
	public final String key;
	
	/** Value of the variable. */
	public final String value;
	
	public LocalVariableState(int lineNumber, String value, String key) {
		this.lineNumber = lineNumber;
		this.value = value;
		this.key = key;
	}

	@Override
	public String toString() {
		return "LocalVariableState [lineNumber=" + lineNumber + ", key=" + key + ", value=" + value + "]";
	}
	
}
