package runlistener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventRepository {
	private static List<Event> EVENTS = new ArrayList<Event>();

	public static void clear() {
		EVENTS.clear();
	}
	
	public static List<Event> getEvents() {
		return Collections.unmodifiableList(EVENTS);
	}
	
	public static void track(Object value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, value.toString()));
	}

	public static void track(byte value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Byte.toString(value)));
	}

	public static void track(short value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Short.toString(value)));
	}

	public static void track(int value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Integer.toString(value)));
	}

	public static void track(long value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Long.toString(value)));
	}

	public static void track(float value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Float.toString(value)));
	}

	public static void track(double value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Double.toString(value)));
	}

	public static void track(boolean value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Boolean.toString(value)));
	}

	public static void track(char value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, Character.toString(value)));
	}
}
