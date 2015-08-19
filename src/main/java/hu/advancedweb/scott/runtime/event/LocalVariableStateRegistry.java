package hu.advancedweb.scott.runtime.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: provide track methods for computational types only, eg. track(boolean) will not be called. see jvms8, page29
public class LocalVariableStateRegistry {
	
	private static List<LocalVariableState> LOCAL_VARIABLE_STATES = new ArrayList<LocalVariableState>();
	
	private static Map<Integer, Map<Integer, String>> LOCAL_VARIABLE_NAMES = new HashMap<>();

	public static void clear() {
		LOCAL_VARIABLE_STATES.clear();
		LOCAL_VARIABLE_NAMES.clear();
	}
	
	public static List<LocalVariableState> getLocalVariableStates() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_STATES);
	}
	
	public static String getLocalVariableName(int var, int lineNumber) {
		Map<Integer, String> lineNumberToName = LOCAL_VARIABLE_NAMES.get(var);
		
		String name = null;
		for (Map.Entry<Integer, String> entry : lineNumberToName.entrySet()) {
			if (entry.getKey() > lineNumber) {
				break;
			} else {
				name = entry.getValue();
			}
		}
		
		if (name == null) {
			throw new IllegalStateException("Name is null!");
		}

		return name;
	}

	public static void trackVariableName(int var, int lineNumber, String name) {
		if (!LOCAL_VARIABLE_NAMES.containsKey(var)) {
			LOCAL_VARIABLE_NAMES.put(var, new HashMap<Integer, String>());
		}
		
		Map<Integer, String> lineNumberToName = LOCAL_VARIABLE_NAMES.get(var);
		lineNumberToName.put(lineNumber, name);
	}
	
	public static void trackLocalVariableState(byte value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Byte.toString(value), var));
	}

	public static void trackLocalVariableState(short value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Short.toString(value), var));
	}

	public static void trackLocalVariableState(int value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Integer.toString(value), var));
	}

	public static void trackLocalVariableState(long value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Long.toString(value), var));
	}

	public static void trackLocalVariableState(float value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Float.toString(value), var));
	}

	public static void trackLocalVariableState(double value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Double.toString(value), var));
	}

	public static void trackLocalVariableState(boolean value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Boolean.toString(value), var));
	}

	public static void trackLocalVariableState(char value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Character.toString(value), var));
	}
	
	public static void trackLocalVariableState(Object value, int lineNumber, int var) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, objectToString(value), var));
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

}
