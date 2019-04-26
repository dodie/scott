package hu.advancedweb.scott;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class DisabledFeaturesConfigTest {
	
	@Test
	public void withDisabledConfigNoInstrumentationHappens() throws Exception {
		Configuration disabledConfig = new Configuration.Builder()
				.setTrackLocalVariableAssignments(false)
				.setIncludeLambdas(false)
				.setTrackLocalVariableIncrements(false)
				.setTrackLocalVariablesAfterEveryMethodCall(false)
				.setTrackFieldAssignments(false)
				.setTrackFieldsAfterEveryMethodCall(false)
				.setTrackMethodStart(false)
				.setTrackReturn(false)
				.setTrackUnhandledException(false)
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.ClassWithFeatures",
				disabledConfig);
	}
	
	private void assertNoTrackingMethodInvoked(String name, Configuration configuration) {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create(name, configuration).invokeMethod("ii");
			verifyZeroInteractions(testRuntime);
		});
	}

}
