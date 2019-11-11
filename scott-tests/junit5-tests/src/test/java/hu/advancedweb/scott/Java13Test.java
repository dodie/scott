package hu.advancedweb.scott;

import static hu.advancedweb.scott.helper.TestHelper.wrapped;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.advancedweb.scott.helper.TestHelper;
import org.junit.jupiter.api.Test;
import java.util.function.Function;


public class Java13Test {

	@Test
	public void testWithTextBlock() {
		String textBlock = """
              line1
              line2""";

		assertEquals(wrapped(textBlock), TestHelper.getLastRecordedStateForVariable("textBlock"));
		assertEquals("line1\nline2", textBlock);
	}
	
}
