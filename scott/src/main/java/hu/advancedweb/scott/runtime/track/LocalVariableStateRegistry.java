package hu.advancedweb.scott.runtime.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Track local variable changes.
 * This class is called by the instrumented test methods.
 * 
 * @author David Csakvari
 */
public class LocalVariableStateRegistry {
	/*
	 *  TODO: provide track methods for computational types only.
	 *  Eg. track(boolean) will not be called from the instrumented code. see jvms8, page29
	 */
	
	private static List<LocalVariableState> LOCAL_VARIABLE_STATES = new ArrayList<LocalVariableState>();
	
	private static List<LocalVariableName> LOCAL_VARIABLE_NAMES = new ArrayList<LocalVariableName>();
	
	public static void clear() {
		LOCAL_VARIABLE_STATES.clear();
		LOCAL_VARIABLE_NAMES.clear();
	}
	
	public static List<LocalVariableState> getLocalVariableStates() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_STATES);
	}
	
	public static String getLocalVariableName(int var, int lineNumber) {
		String name = null;
		for (LocalVariableName localVariableName : LOCAL_VARIABLE_NAMES) {
			if (var == localVariableName.var) {
				if (localVariableName.lineNumber > lineNumber) {
					break;
				} else {
					name = localVariableName.name;
				}	
			}
		}
		
		if (name == null) {
			throw new IllegalStateException("Name is null!");
		}

		return name;
	}

	public static void trackVariableName(int var, int lineNumber, String name) {
		LOCAL_VARIABLE_NAMES.add(new LocalVariableName(lineNumber, name, var));
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
		if (value == null) {
			return "null";
		} else if (value instanceof Object[]) {
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
