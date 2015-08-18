package hu.advancedweb.scott.runtime.event;

public class Event {
	public final int lineNumber;
	public final int var;
	public final String value;
	
	public Event(int lineNumber, String value, int var) {
		this.lineNumber = lineNumber;
		this.value = value;
		this.var = var;
	}
}
