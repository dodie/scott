package hu.advancedweb.scott.runtime.track;

/**
 * Represents a data point collected at a given line number.
 * 
 * @author David Csakvari
 */
public class ContextualData {
	
	/** Line number where the data is collected. */
	public final int lineNumber;
	
	/** Method where the data is collected. */
	public final String methodName;

	public ContextualData(int lineNumber, String methodName) {
		this.lineNumber = lineNumber;
		this.methodName = methodName;
	}

	@Override
	public String toString() {
		return "ContextualData [lineNumber=" + lineNumber + ", methodName=" + methodName + "]";
	}
	
}
