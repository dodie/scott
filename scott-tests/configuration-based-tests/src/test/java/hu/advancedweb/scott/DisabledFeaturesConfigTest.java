package hu.advancedweb.scott;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import hu.advancedweb.scott.helper.CustomClassLoader;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class DisabledFeaturesConfigTest {
	
	@Test
	public void withDisabledConfigNoInstrumentationHappens() throws Exception {
		Configuration disabledConfig = new Configuration.Builder()
				.setTrackFieldStateChanges(false)
				.setTrackLocalVariableAssignments(false)
				.setIncludeLambdas(false)
				.setTrackLocalVariableIncrements(false)
				.setTrackLocalVariablesAfterEveryMethodCall(false)
				.setTrackFieldStateChanges(false)
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
		Class<?> clazz = CustomClassLoader
				.loadAndTransform(name, configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("ii").invoke(obj);
			verifyZeroInteractions(testRuntime);
		});
	}

}
