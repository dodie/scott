package hu.advancedweb.scott;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import hu.advancedweb.scott.helper.TestHelper;

public class TestInfoTest {

	@Test
	@DisplayName("TEST 1")
	@Tag("my-tag")
	void test1(TestInfo testInfo) {
		assertEquals("TEST 1", testInfo.getDisplayName());
		assertTrue(testInfo.getTags().contains("my-tag"));
		assertTrue(TestHelper.getLastRecordedStateForVariable("testInfo") != null);
	}

}
