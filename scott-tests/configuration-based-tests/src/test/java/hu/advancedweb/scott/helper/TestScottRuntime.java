package hu.advancedweb.scott.helper;

public class TestScottRuntime {
	
	private static ThreadLocal<TestScottRuntimeVerifier> INSTANCE = new ThreadLocal<TestScottRuntimeVerifier>();
	
	@FunctionalInterface
	public static interface ExceptionToleratingSupplier {
		public void run(TestScottRuntimeVerifier testScottRuntimeVerifier) throws Exception;
	}
	
	public static void verify(TestScottRuntimeVerifier testScottRuntimeVerifier, ExceptionToleratingSupplier supplier) {
		try {
			INSTANCE.set(testScottRuntimeVerifier);
			supplier.run(testScottRuntimeVerifier);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void trackMethodStart(int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackMethodStart(lineNumber, methodName, clazz);
	}
	
	public static void trackEndOfArgumentsAtMethodStart(int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackEndOfArgumentsAtMethodStart(lineNumber, methodName, clazz);
	}
	
	public static void trackLocalVariableState(Object value, String name, int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackLocalVariableState(value, name, lineNumber, methodName, clazz);
	}
	
	public static void trackLocalVariableState(byte value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Byte.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(short value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Short.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(int value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Integer.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(long value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Long.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(float value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Float.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(double value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Double.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(boolean value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Boolean.valueOf(value), name, lineNumber, methodName, clazz);
	}

	public static void trackLocalVariableState(char value, String name, int lineNumber, String methodName, Class<?> clazz) {
		trackLocalVariableState(Character.valueOf(value), name, lineNumber, methodName, clazz);
	}
	
	public static void trackUnhandledException(Throwable throwable, int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackUnhandledException(throwable, lineNumber, methodName, clazz);
	}
	
	public static void trackReturn(int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackReturn(lineNumber, methodName, clazz);
	}
	
	public static void trackReturn(Object value, int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackReturn(value, lineNumber, methodName, clazz);
	}

	public static void trackReturn(byte value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Byte.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(short value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Short.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(int value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Integer.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(long value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Long.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(float value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Float.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(double value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Double.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(boolean value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Boolean.valueOf(value), lineNumber, methodName, clazz);
	}

	public static void trackReturn(char value, int lineNumber, String methodName, Class<?> clazz) {
		trackReturn(Character.valueOf(value), lineNumber, methodName, clazz);
	}
	
	public static void trackLambdaDefinition(int lineNumber, String methodName, Class<?> clazz) {
		INSTANCE.get().trackLambdaDefinition(lineNumber, methodName, clazz);
	}
	
	public static void trackFieldState(Object value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		INSTANCE.get().trackFieldState(value, name, lineNumber, methodName, clazz, isStatic, owner);
	}
	
	public static void trackFieldState(byte value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Byte.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
	
	public static void trackFieldState(short value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Short.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
	
	public static void trackFieldState(int value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Integer.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
	
	public static void trackFieldState(long value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Long.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
	
	public static void trackFieldState(float value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Float.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
		
	public static void trackFieldState(double value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Double.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
		
	public static void trackFieldState(boolean value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Boolean.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
		
	public static void trackFieldState(char value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner) {
		trackFieldState(Character.valueOf(value), name, lineNumber, methodName, clazz, isStatic, owner);
	}
		
}

