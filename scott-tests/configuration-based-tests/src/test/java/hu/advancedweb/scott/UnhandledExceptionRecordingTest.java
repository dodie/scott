package hu.advancedweb.scott;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class UnhandledExceptionRecordingTest {
	
	@Test
	public void recordUnhandledExceptions() throws Exception {
		Configuration config = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			try {
				InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithUnhandledException", config).invokeMethod("boom");
			} catch (Exception e) {
				// Ignore
			}
			
			// Tracking happens 2 times: one for the lambda in the method, and one for the method itself.
			verify(testRuntime, times(2)).trackUnhandledException(any(), anyInt(), any(), any());
		});
	}
	
}
