package hu.advancedweb.scott;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class MultiInstrumentationTest {
	
	@Test
	public void recordReturnFromLambda() throws Exception {
		byte[] originalClass  = InstrumentedObject.getBytes("hu.advancedweb.scott.examples.ClassWithTrys");
		byte[] instrumentedClass = new ScottClassTransformer().transform(originalClass, config());
		byte[] doubleInstrumentedClass = new ScottClassTransformer().transform(instrumentedClass, config());
		
		assertNotEquals(originalClass.length, instrumentedClass.length);
		assertEquals(instrumentedClass.length, doubleInstrumentedClass.length);
	}
	
	private Configuration config() {
		Configuration config = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName())
				.build();

		return config;
	}
	
	
}
