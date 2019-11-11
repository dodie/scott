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
	
	@Test
	public void testWithSwitchExpression() {
		Day day = Day.WEDNESDAY;
		int j = switch (day) {
			case MONDAY -> 0;
			case TUESDAY -> 1;
			default -> {
				int k = day.toString().length();
				assertEquals(Integer.toString(k), TestHelper.getLastRecordedStateForVariable("k"));
				assertEquals(9, k);
				yield k;
			}
		};
		
		assertEquals(Integer.toString(j), TestHelper.getLastRecordedStateForVariable("j"));
		assertEquals(9, j);
	}
	
	private enum Day {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}
	
}
