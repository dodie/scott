package hu.advancedweb.scott;

import static hu.advancedweb.scott.helper.TestHelper.wrapped;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.advancedweb.scott.helper.TestHelper;
import org.junit.jupiter.api.Test;


public class Java16Test {

	@Test
	public void testWithRecord() {

		record X(String y) { }

		var x = new X("hello");

		var y = x.y();

		assertEquals(wrapped("hello"), TestHelper.getLastRecordedStateForVariable("y"));
	}

}
