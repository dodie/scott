package hu.advancedweb.example;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

public class LambdaTest {

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
		
		assertThat(palindromized, equalTo(word + word));
	}
	
	@Test
	public void lambda_with_single_expression() throws Exception {
		Function<String, String> lambda = a -> a + a;
		String result = lambda.apply("1");
		assertThat(result, equalTo("2"));
	}
	
	@Test
	public void lambda_with_single_fun_expression() throws Exception {
		Function<String, Function<String, String>> lambda = a -> b -> a + b;
		String result = lambda.apply("1").apply("2");
		assertThat(result, equalTo("3"));
	}
	
	@Test
	public void lambda_with_single_expression_2() throws Exception {
		Function<String, String> lambda = a -> {return a + a;};
		String result = lambda.apply("1");
		assertThat(result, equalTo("2"));
	}
	
	@Test
	public void lambda_with_single_expression_3() throws Exception {
		Function<String, String> lambda = a -> 
			a + a;
		String result = lambda.apply("1");
		assertThat(result, equalTo("2"));
	}
	
	
	@Test
	public void lambda_with_single_expression_4() throws Exception {
		Function<String, String> lambda = a -> {
			return a;
		};
		String result = lambda.apply("1");
		assertThat(result, equalTo("2"));
	}
	
}
