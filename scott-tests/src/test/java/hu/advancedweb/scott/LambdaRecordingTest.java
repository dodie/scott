package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class LambdaRecordingTest {
	
	@Test
	public void test_with_lambda() throws Exception {
		Function<String, String> lambda = input -> {
			assertThat(TestHelper.getLastRecordedStateForVariable("input"), equalTo(input));
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(inner));
			return inner;
		};
		assertThat(TestHelper.getLastRecordedStateForVariable("lambda"), equalTo(lambda.toString()));
		
		String outer = "outer";
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(outer));
		String result = lambda.apply(outer);
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(result));
	}

}
