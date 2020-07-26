package hu.advancedweb.scott;

import static hu.advancedweb.scott.helper.TestHelper.wrapped;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.advancedweb.scott.helper.TestHelper;
import org.junit.jupiter.api.Test;


public class Java14Test {

	@Test
	public void testWithTextBlock() {
		Object o = "hello";

		final String result;
		if (o instanceof String s) {
			result = s + " world";
		} else {
			result = "not a string";
		}

		assertEquals(wrapped("hello"), TestHelper.getLastRecordedStateForVariable("o"));
		assertEquals(wrapped("hello"), TestHelper.getLastRecordedStateForVariable("s"));
		assertEquals("hello world", result);
	}

}
