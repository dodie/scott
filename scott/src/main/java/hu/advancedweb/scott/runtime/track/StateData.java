package hu.advancedweb.scott.runtime.track;

/**
 * Represents a data point collected at a given line number.
 * 
 * @author David Csakvari
 */
public class StateData {

	/** Line number where the data is collected. */
	public final int lineNumber;
	
	/** Method where the data is collected. */
	public final String methodName;

	/** Name of the variable or field. */
	public final String name;
	
	/** Recorded value. */
	public final String value;
	
	public StateData(int lineNumber, String methodName, String name, String value) {
		this.lineNumber = lineNumber;
		this.methodName = methodName;
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "StateData [lineNumber=" + lineNumber + ", methodName=" + methodName + ", name=" + name + ", value=" + value + "]";
	}

}
