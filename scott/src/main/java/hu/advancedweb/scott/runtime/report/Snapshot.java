package hu.advancedweb.scott.runtime.report;

/**
 * Represents a variable name - value pair of a tracked data.
 * 
 * @author David Csakvari
 */
class Snapshot {
	
	final String name;
	final String value;
	
	public Snapshot(String name, String value) {
		this.name = name;
		this.value = value;
	}

}
