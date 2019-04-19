package hu.advancedweb.scott;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import hu.advancedweb.scott.helper.CustomClassLoader;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class ReturnRecordingTest {
	
	@Test
	public void recordReturnFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "simpleReturn");
			
			// Simple tracking happens in the lambda, and in the containing method as well.
			verify(testRuntime, times(2)).trackReturn(anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveTrueFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "primitiveBooleanTrue");
			
			verify(testRuntime, times(1)).trackReturn(eq(true), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveFalseFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "primitiveBooleanFalse");
			
			verify(testRuntime, times(1)).trackReturn(eq(false), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveIntLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "primitiveInt");
			
			verify(testRuntime, times(1)).trackReturn(eq(2147483647), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveDoubleLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "primitiveDouble");
			
			verify(testRuntime, times(1)).trackReturn(eq(1.7976931348623157E308D), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveLongLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "primitiveLong");
			
			verify(testRuntime, times(1)).trackReturn(eq(9223372036854775807L), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnObjectLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Class<?> clazz = instrumentExampleClass();
			callInstanceMethod(clazz, "object");
			
			verify(testRuntime, times(1)).trackReturn(eq("Hello world"), anyInt(), any(), any());
		});
	}
	
	private Class<?> instrumentExampleClass() {
		Configuration config = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		Class<?> clazz = CustomClassLoader.loadAndTransform("hu.advancedweb.scott.examples.ClassWithLambdas", config);
		return clazz;
	}
	
	private void callInstanceMethod(Class<?> clazz, String methodName) throws Exception{
		Object obj = clazz.getDeclaredConstructor().newInstance();
		clazz.getDeclaredMethod(methodName).invoke(obj);
	}
	
}
