package hu.advancedweb.scott.helper;

public interface TestScottRuntimeVerifier {
	
	public void trackMethodStart(int lineNumber, String methodName, Class<?> clazz);
	
	public void trackEndOfArgumentsAtMethodStart(int lineNumber, String methodName, Class<?> clazz);
	
	public void trackLocalVariableState(Object value, String name, int lineNumber, String methodName, Class<?> clazz);
	
	public void trackUnhandledException(Throwable throwable, int lineNumber, String methodName, Class<?> clazz);
	
	public void trackReturn(int lineNumber, String methodName, Class<?> clazz);
	
	public void trackReturn(Object value, int lineNumber, String methodName, Class<?> clazz);

	public void trackLambdaDefinition(int lineNumber, String methodName, Class<?> clazz);
		
	public void trackFieldState(Object value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner);
	
}

