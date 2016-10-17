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
			assertThat(TestHelper.getLastRecordedStateFor("input"), equalTo(input));
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
			return inner;
		};
		assertThat(TestHelper.getLastRecordedStateFor("lambda"), equalTo(lambda.toString()));
		
		String outer = "outer";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
		String result = lambda.apply(outer);
		assertThat(TestHelper.getLastRecordedStateFor("result"), equalTo(result));
	}

}
