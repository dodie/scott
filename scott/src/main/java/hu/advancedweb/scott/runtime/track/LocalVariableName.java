package hu.advancedweb.scott.runtime.track;

/**
 * Represents a data point collected at a given line number 
 * that contains a local variable name and an unique identifier for the variable.
 * 
 * @author David Csakvari
 */
public class LocalVariableName {
	
	/** Line number where the data is collected. */
	public final int lineNumber;
	
	/** Unique identifier for the variable. Because how we track variables in lambdas, it's composed of the variable index and the name of the containing method. */
	public final String key;

	/** Variable name. */
	public final String name;
	
	public LocalVariableName(int lineNumber, String name, String key) {
		this.lineNumber = lineNumber;
		this.name = name;
		this.key = key;
	}

	@Override
	public String toString() {
		return "LocalVariableName [lineNumber=" + lineNumber + ", key=" + key + ", name=" + name + "]";
	}
	
}
