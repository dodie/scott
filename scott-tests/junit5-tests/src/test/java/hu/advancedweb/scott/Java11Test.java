package hu.advancedweb.scott;

import static hu.advancedweb.scott.helper.TestHelper.wrapped;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.advancedweb.scott.helper.TestHelper;
import org.junit.jupiter.api.Test;
import java.util.function.Function;


public class Java11Test {

	@Test
	void varTest() {
		var dot = ".";
		assertEquals(wrapped(dot), TestHelper.getLastRecordedStateForVariable("dot"));
		assertEquals(".", dot);
	}

	@Test
	public void lambdaVarTest() throws Exception {
		Function<String, String> lambda = (var a) -> {
			assertEquals(wrapped(a), TestHelper.getLastRecordedStateForVariable("a"));
			return a;
		};
		String result = lambda.apply("1");
		assertEquals(wrapped(result), TestHelper.getLastRecordedStateForVariable("result"));
	}


}
