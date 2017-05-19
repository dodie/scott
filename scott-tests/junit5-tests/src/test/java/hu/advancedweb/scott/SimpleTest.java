package hu.advancedweb.scott;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class SimpleTest {
	
	@Test
	void test() {
		String dot = ".";
		assertEquals(dot, TestHelper.getLastRecordedStateForVariable("dot"));
		assertEquals(".", dot);
	}
	
}
