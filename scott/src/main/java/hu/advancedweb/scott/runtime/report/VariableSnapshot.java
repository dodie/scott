package hu.advancedweb.scott.runtime.report;

/**
 * Represents a variable name - value pair for a 
 * @author David Csakvari
 *
 */
class VariableSnapshot {
	
	final String name;
	final String value;
	
	public VariableSnapshot(String name, String value) {
		this.name = name;
		this.value = value;
	}

}
