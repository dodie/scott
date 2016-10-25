package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import hu.advancedweb.scott.runtime.track.StateRegistry;

public class MethodNameAndClassTrackingTest {
	
	@Test
	public void test1() throws Exception {
		assertThat(StateRegistry.getTestClassType(), equalTo("hu/advancedweb/scott/MethodNameAndClassTrackingTest"));
		assertThat(StateRegistry.getTestMethodName(), equalTo("test1"));
	}

	@Test
	public void test2() throws Exception {
		assertThat(StateRegistry.getTestClassType(), equalTo("hu/advancedweb/scott/MethodNameAndClassTrackingTest"));
		assertThat(StateRegistry.getTestMethodName(), equalTo("test2"));
	}

}
