package hu.advancedweb.scott.runtime.track;

public class StateData extends ContextualData {

	/** Name of the variable or field. */
	public final String name;
	
	/** Recorded value. */
	public final String value;
	
	public StateData(int lineNumber, String methodName, String name, String value) {
		super(lineNumber, methodName);
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "StateData [lineNumber=" + lineNumber + ", methodName=" + methodName + ", name=" + name + ", value=" + value + "]";
	}

}
