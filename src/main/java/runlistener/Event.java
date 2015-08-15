package runlistener;

public class Event {
	final int lineNumber;
	final String value;
	
	public Event(int lineNumber, String value) {
		this.lineNumber = lineNumber;
		this.value = value;
	}
}
