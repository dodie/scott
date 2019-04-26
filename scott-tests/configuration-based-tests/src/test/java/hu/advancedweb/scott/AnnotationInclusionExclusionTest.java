package hu.advancedweb.scott;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;

public class AnnotationInclusionExclusionTest {
	
	@Test
	public void excludeAll() throws Exception {
		Configuration config = new Configuration.Builder()
				.setExcludeByAnnotation(Arrays.asList("hu.advancedweb.scott.examples.AnnotationA", "hu.advancedweb.scott.examples.AnnotationB"))
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.ClassA",
				config);
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.somepackage.ClassB",
				config);
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.otherpackage.ClassC",
				config);
		
	}

	@Test
	public void includeAll() throws Exception {
		Configuration config = new Configuration.Builder()
				.setIncludeByAnnotation(Arrays.asList("hu.advancedweb.scott.examples.AnnotationA", "hu.advancedweb.scott.examples.AnnotationB"))
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		assertTracking(
				"hu.advancedweb.scott.examples.ClassA",
				config);
		assertTracking(
				"hu.advancedweb.scott.examples.somepackage.ClassB",
				config);
		assertTracking(
				"hu.advancedweb.scott.examples.otherpackage.ClassC",
				config);
	}
	
	@Test
	public void includeSome() throws Exception {
		Configuration config = new Configuration.Builder()
				.setIncludeByAnnotation(Arrays.asList("hu.advancedweb.scott.examples.AnnotationA"))
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		assertTracking(
				"hu.advancedweb.scott.examples.ClassA",
				config);
		assertTracking(
				"hu.advancedweb.scott.examples.somepackage.ClassB",
				config);
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.otherpackage.ClassC",
				config);
	}
	
	@Test
	public void excludeSome() throws Exception {
		Configuration config = new Configuration.Builder()
				.setExcludeByAnnotation(Arrays.asList("hu.advancedweb.scott.examples.AnnotationA"))
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();
		
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.ClassA",
				config);
		assertNoTrackingMethodInvoked(
				"hu.advancedweb.scott.examples.somepackage.ClassB",
				config);
		assertTracking(
				"hu.advancedweb.scott.examples.otherpackage.ClassC",
				config);
	}
	
	private void assertNoTrackingMethodInvoked(String name, Configuration configuration) {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create(name, configuration).invokeMethod("hello");
			
			verifyZeroInteractions(testRuntime);
		});
	}
	
	private void assertTracking(String name, Configuration configuration) {
		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			InstrumentedObject.create(name, configuration).invokeMethod("hello");
			
			verify(testRuntime).trackMethodStart(anyInt(), eq("hello"), any());
		});
	}

}
