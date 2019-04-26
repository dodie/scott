package hu.advancedweb.scott;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class ReturnRecordingTest {
	
	Configuration config = new Configuration.Builder()
			.setTrackerClass(TestScottRuntime.class.getCanonicalName())
			.build();
	
	@Test
	public void recordReturnFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("simpleReturn");
			
			// Simple tracking happens in the lambda, and in the containing method as well.
			verify(testRuntime, times(2)).trackReturn(anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveTrueFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("primitiveBooleanTrue");
			verify(testRuntime, times(1)).trackReturn(eq(true), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveFalseFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("primitiveBooleanFalse");
			verify(testRuntime, times(1)).trackReturn(eq(false), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveIntLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("primitiveInt");
			verify(testRuntime, times(1)).trackReturn(eq(2147483647), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveDoubleLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("primitiveDouble");
			verify(testRuntime, times(1)).trackReturn(eq(1.7976931348623157E308D), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnPrimitiveLongLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("primitiveLong");
			verify(testRuntime, times(1)).trackReturn(eq(9223372036854775807L), anyInt(), any(), any());
		});
	}
	
	@Test
	public void recordReturnObjectLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithLambdas", config).invokeMethod("object");
			verify(testRuntime, times(1)).trackReturn(eq("Hello world"), anyInt(), any(), any());
		});
	}
	
}
