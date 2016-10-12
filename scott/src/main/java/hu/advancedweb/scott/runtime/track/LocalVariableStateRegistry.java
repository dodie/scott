package hu.advancedweb.scott.runtime.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Track local variable changes.
 * This class is called by the instrumented test methods to record stuff,
 * and queried by the test reporter.
 * 
 * @author David Csakvari
 */
public class LocalVariableStateRegistry {
	
	private static List<LocalVariableState> LOCAL_VARIABLE_STATES = new ArrayList<LocalVariableState>();
	
	private static List<LocalVariableState> FIELD_STATES = new ArrayList<LocalVariableState>();
	
	private static List<LocalVariableName> LOCAL_VARIABLE_NAMES = new ArrayList<LocalVariableName>();

	private static String METHOD_NAME;

	private static String CLASS_NAME;
	
	public static void startTracking(String className, String methodName) {
		CLASS_NAME = className;
		METHOD_NAME = methodName;
		
		LOCAL_VARIABLE_STATES.clear();
		LOCAL_VARIABLE_NAMES.clear();
		FIELD_STATES.clear();
	}
	
	public static List<LocalVariableState> getLocalVariableStates() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_STATES);
	}
	
	public static List<LocalVariableName> getLocalVariableNames() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_NAMES);
	}
	
	public static List<LocalVariableState> getFieldStates() {
		return Collections.unmodifiableList(FIELD_STATES);
	}
	
	public static String getTestClassType() {
		return CLASS_NAME;
	}
	
	public static String getTestMethodName() {
		return METHOD_NAME;
	}
	
	public static String getLocalVariableName(String key, int lineNumber) {
		String name = null;
		for (LocalVariableName localVariableName : LOCAL_VARIABLE_NAMES) {
			if (key.equals(localVariableName.key)) {
				if (localVariableName.lineNumber > lineNumber) {
					break;
				} else {
					name = localVariableName.name;
				}	
			}
		}
		
		return name;
	}
	
	public static void trackFieldState(String value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, value, key));
	}
	
	public static void trackFieldState(byte value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Byte.toString(value), key));
	}
	
	public static void trackFieldState(short value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Short.toString(value), key));
	}
	
	public static void trackFieldState(int value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Integer.toString(value), key));
	}
	
	public static void trackFieldState(long value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Long.toString(value), key));
	}
	
	public static void trackFieldState(float value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Float.toString(value), key));
	}
	
	public static void trackFieldState(double value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Double.toString(value), key));
	}
	public static void trackFieldState(boolean value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Boolean.toString(value), key));
	}
	
	public static void trackFieldState(char value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, Character.toString(value), key));
	}
	
	public static void trackFieldState(Object value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new LocalVariableState(lineNumber, objectToString(value), key));
	}

	private static String getFieldKey(String name, boolean isStatic, String owner) {
		final String key;
		if (isStatic) {
			key = owner.substring(owner.lastIndexOf("/") + 1) + "." + name;
		} else {
			key = "this." + name;
		}
		return key;
	}

	public static void trackVariableName(String name, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_NAMES.add(new LocalVariableName(lineNumber, name, var + methodName));
	}
	
	public static void trackLocalVariableState(byte value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Byte.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(short value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Short.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(int value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Integer.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(long value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Long.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(float value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Float.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(double value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Double.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(boolean value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Boolean.toString(value), var + methodName));
	}

	public static void trackLocalVariableState(char value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, Character.toString(value), var + methodName));
	}
	
	public static void trackLocalVariableState(Object value, int lineNumber, int var, String methodName) {
		LOCAL_VARIABLE_STATES.add(new LocalVariableState(lineNumber, objectToString(value), var + methodName));
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
