package hu.advancedweb.scott.runtime.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EventStore {
	private static List<Event> EVENTS = new ArrayList<Event>();

	public static void clear() {
		EVENTS.clear();
	}
	
	public static List<Event> getEvents() {
		return Collections.unmodifiableList(EVENTS);
	}
	
	public static void track(Object value, int lineNumber) {
		EVENTS.add(new Event(lineNumber, objectToString(value)));
	}

	private static String objectToString(Object value) {
		if (value instanceof Object[]) {
			return Arrays.toString((Object[])value);
		} else if (value instanceof boolean[]) {
			return Arrays.toString((boolean[])value);
		} else if (value instanceof byte[]) {
			return Arrays.toString((byte[])value);
		} else if (value instanceof short[]) {
			return Arrays.toString((short[])value);
		} else if (value instanceof char[]) {
			return Arrays.toString((char[])value);
		} else if (value instanceof int[]) {
			return Arrays.toString((int[])value);
		} else if (value instanceof long[]) {
			return Arrays.toString((long[])value);
		} else if (value instanceof float[]) {
			return Arrays.toString((float[])value);
		} else if (value instanceof double[]) {
			return Arrays.toString((double[])value);
		} else {
			return value.toString();
		}	
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
