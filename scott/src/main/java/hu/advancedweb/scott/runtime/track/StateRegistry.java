package hu.advancedweb.scott.runtime.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Track state changes in a test case.
 * This class is called by the instrumented test methods to record stuff,
 * and queried by the test reporter.
 * 
 * @author David Csakvari
 */
public class StateRegistry {
	
	/*
	 * Variable data recording and method start line recording is based on the fact that
	 * in a single test case there can be multiple lambdas, thus multiple methods.
	 */
	
	private static List<StateData> LOCAL_VARIABLE_STATES = new ArrayList<StateData>();
	
	private static List<StateData> FIELD_STATES = new ArrayList<StateData>();
	
	private static List<ReturnData> RETURN_VALUES = new ArrayList<ReturnData>();

	private static List<ExceptionData> UNHANDLED_EXCEPTIONS = new ArrayList<ExceptionData>();

	private static String METHOD_NAME;

	private static String CLASS_NAME;
	
	private static Map<String, Integer> METHOD_START_LINES = new HashMap<String, Integer>();
	
	public static List<StateData> getLocalVariableStates() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_STATES);
	}
	
	public static List<ExceptionData> getUnhandledExceptions() {
		return Collections.unmodifiableList(UNHANDLED_EXCEPTIONS);
	}
	
	public static List<StateData> getFieldStates() {
		return Collections.unmodifiableList(FIELD_STATES);
	}
	
	public static List<ReturnData> getRetrunValues() {
		return Collections.unmodifiableList(RETURN_VALUES);
	}
	
	public static String getTestClassType() {
		return CLASS_NAME;
	}
	
	public static String getTestMethodName() {
		return METHOD_NAME;
	}
	
	public static Map<String, Integer> getMethodStartLine() {
		return METHOD_START_LINES;
	}
	
	public static void trackMethodStart(String methodName, Class<?> clazz) {
		CLASS_NAME = classToTypeName(clazz);
		METHOD_NAME = methodName;

		if (!methodName.startsWith("lambda$")) {
			LOCAL_VARIABLE_STATES.clear();
			FIELD_STATES.clear();
			RETURN_VALUES.clear();
			UNHANDLED_EXCEPTIONS.clear();
		}
	}
	
	public static void trackEndOfArgumentsAtMethodStart(String methodName, Class<?> clazz) {
		// No-op.
	}
	
	public static void trackLambdaDefinition(int lineNumber, String methodName, Class<?> clazz) {
		METHOD_START_LINES.put(methodName, lineNumber);
	}
	
	public static void trackFieldState(String value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), value));
	}
	
	public static void trackFieldState(byte value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Byte.toString(value)));
	}
	
	public static void trackFieldState(short value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Short.toString(value)));
	}
	
	public static void trackFieldState(int value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Integer.toString(value)));
	}
	
	public static void trackFieldState(long value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Long.toString(value)));
	}
	
	public static void trackFieldState(float value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Float.toString(value)));
	}
	
	public static void trackFieldState(double value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Double.toString(value)));
	}
	
	public static void trackFieldState(boolean value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Boolean.toString(value)));
	}
	
	public static void trackFieldState(char value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), Character.toString(value)));
	}
	
	public static void trackFieldState(Object value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		
		if (stringValue != null) {
			FIELD_STATES.add(new StateData(lineNumber, methodName, niceFieldName(name, isStatic, owner, clazz), stringValue));
		}
	}
	
	private static String niceFieldName(String name, boolean isStatic, String owner, Class<?> clazz) {
		final String key;
		if (isStatic) {
			key = owner.substring(owner.lastIndexOf("/") + 1) + "." + name;
		} else {
			final String prefix;
			if (classToTypeName(clazz).equals(owner)) {
				prefix = "this.";
			} else if (owner.contains("$")) {
				prefix = "(in enclosing " + owner.substring(owner.lastIndexOf("$") + 1) + ") ";
			} else if (owner.contains("/")) {
				prefix = "(in enclosing " + owner.substring(owner.lastIndexOf("/") + 1) + ") ";
			} else {
				prefix = "(in enclosing " + owner + ") ";
			}
			
			key = prefix + name;
		}
		return key;
	}
	
	public static void trackLocalVariableState(byte value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Byte.toString(value)));
	}

	public static void trackLocalVariableState(short value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Short.toString(value)));
	}

	public static void trackLocalVariableState(int value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Integer.toString(value)));
	}

	public static void trackLocalVariableState(long value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Long.toString(value)));
	}

	public static void trackLocalVariableState(float value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Float.toString(value)));
	}

	public static void trackLocalVariableState(double value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Double.toString(value)));
	}

	public static void trackLocalVariableState(boolean value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Boolean.toString(value)));
	}

	public static void trackLocalVariableState(char value, String name, int lineNumber, String methodName, Class<?> clazz) {
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, Character.toString(value)));
	}
	
	public static void trackLocalVariableState(Object value, String name, int lineNumber, String methodName, Class<?> clazz) {
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		
		if (stringValue != null) {
			LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, methodName, name, stringValue));
		}
	}
	
	public static void trackUnhandledException(Throwable throwable, int lineNumber, String methodName, Class<?> clazz) {
		UNHANDLED_EXCEPTIONS.add(new ExceptionData(lineNumber, methodName, throwable));
	}
	
	public static void trackReturn(int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnData(lineNumber, methodName));
	}

	public static void trackReturn(byte value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Byte.toString(value)));
	}

	public static void trackReturn(short value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Short.toString(value)));
	}

	public static void trackReturn(int value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Integer.toString(value)));
	}

	public static void trackReturn(long value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Long.toString(value)));
	}

	public static void trackReturn(float value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Float.toString(value)));
	}

	public static void trackReturn(double value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Double.toString(value)));
	}

	public static void trackReturn(boolean value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Boolean.toString(value)));
	}

	public static void trackReturn(char value, int lineNumber, String methodName, Class<?> clazz) {
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, Character.toString(value)));
	}

	public static void trackReturn(Object value, int lineNumber, String methodName, Class<?> clazz) {
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		RETURN_VALUES.add(new ReturnValueData(lineNumber, methodName, stringValue));
	}
	
	private static String objectToStringIgnoreMockitoExceptions(Object value) {
		try {
			return objectToString(value);
		} catch (Throwable t) {
			if (t.getClass().getName().startsWith("org.mockito")) {
				/*
				 * Calling toString on mocks might result in a MockitoException.
				 * Under normal circumstances it might happen when we try
				 * to verify toString (see: https://github.com/mockito/mockito/wiki/FAQ):
				 * verify(foo, times(1)).toString();
				 * 
				 * Due to Scott's bytecode instrumentation, the tests
				 * might accidentally call toString on mocks during the construction
				 * of normal verify() as well.
				 * See Issue #25.
				 */
				return null;
			} else {
				throw t;
			}
		}
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
			return Arrays.toString((double[]) value);
		} else if (value instanceof String) {
			return wrapped(value.toString(), '"');
		} else {
			return value.toString();
		}
	}

	private static String wrapped(String original, char wrappingChar) {
		return new StringBuilder().append(wrappingChar).append(original).append(wrappingChar).toString();
	}
	
	private static String classToTypeName(Class<?> clazz) {
		return clazz.getName().replace('.', '/');
	}

}
