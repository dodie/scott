package hu.advancedweb.example;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

public class LambdaTest {

	// FIXME: No data reported for the body of the lambda. See Issue #11.
	@Test
	public void test_with_lambda() throws Exception {
		Function<String, String> generatePalindrome = input -> {
			StringBuilder sb = new StringBuilder();
			sb.append(input);
			sb.reverse();
			
			String reversed = sb.toString();
			
			String palindrome = input + reversed;
			return palindrome;
		};
		
		String word = "cat";
		
		String palindromized = generatePalindrome.apply(word);
		
		assertEquals("Hello World!", "Hello World!!!");
		
		assertThat(palindromized, equalTo(word + word));
	}
	
}
