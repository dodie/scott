package hu.advancedweb.scott;

import static hu.advancedweb.scott.helper.TestHelper.wrapped;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import org.junit.jupiter.api.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class AssumptionsTest {

	@Test
	void testOnlyOnCiServer() {
		assumeTrue("CI".equals(env()));
		String s = "value";
		assertEquals("value", s);
		assertEquals(wrapped("value"), TestHelper.getLastRecordedStateForVariable("s"));
	}

	@Test
	void testInAllEnvironments() {
		assumingThat("CI".equals(env()), () -> {
			String s = "value";
			assertEquals("value", s);
			assertEquals(wrapped("value"), TestHelper.getLastRecordedStateForVariable("s"));
		});

		String s = "value";
		assertEquals("value", s);
		assertEquals(wrapped("value"), TestHelper.getLastRecordedStateForVariable("s"));
	}

	public String env() {
		return "CI";
	}

}