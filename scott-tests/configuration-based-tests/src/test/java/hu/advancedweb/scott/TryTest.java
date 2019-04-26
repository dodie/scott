package hu.advancedweb.scott;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class TryTest {
	
	@Test
	public void recordReturnFromLambda() throws Exception {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject instrumentedObject = instrumentExampleClass();
			
			Object obj = instrumentedObject.invokeMethod("hello");
			Object obj2 = instrumentedObject.invokeMethod("hello2");
			
			// This test is to verify that the instrumentation of these classes produce valid bytecode.
			verify(testRuntime).trackMethodStart(anyInt(), eq("hello"), any());
			verify(testRuntime).trackMethodStart(anyInt(), eq("hello2"), any());
		});
	}
	
	private InstrumentedObject instrumentExampleClass() {
		Configuration config = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.setIncludeLambdasOnlyWhenOtherInstrumentationIsInPlace(true)
				.setTrackLocalVariablesAfterEveryMethodCall(false)
				.setTrackFieldAssignments(false)
				.setTrackFieldsAfterEveryMethodCall(false)
				.setTrackReturn(false)
				.setTrackUnhandledException(false)
				.build();

		return InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithTrys", config);
	}
	
}
