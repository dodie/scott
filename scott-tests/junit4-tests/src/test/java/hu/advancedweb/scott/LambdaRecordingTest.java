package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

public class LambdaRecordingTest {
	
	@Test
	public void test_with_lambda() throws Exception {
		Function<String, String> lambda = input -> {
			assertThat(TestHelper.getLastRecordedStateForVariable("input"), equalTo(wrapped(input)));
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
			return inner;
		};
		assertThat(TestHelper.getLastRecordedStateForVariable("lambda"), equalTo(lambda.toString()));
		
		String outer = "outer";
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped(outer)));
		String result = lambda.apply(outer);
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped(result)));
	}
	
	@Test
	public void lambda_with_multiple_parameters() throws Exception {
		Function2<String, String, String> lambda = (a, b) -> {
			assertThat(TestHelper.getLastRecordedStateForVariable("a"), equalTo(wrapped(a)));
			assertThat(TestHelper.getLastRecordedStateForVariable("b"), equalTo(wrapped(b)));
			return a + b;
		};
		String result = lambda.apply("1", "2");
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped(result)));
	}
	
	@FunctionalInterface
	interface Function2 <A, B, R> {
        public R apply (A a, B b);
    }
	
	@Test
	public void lambda_with_single_expression() throws Exception {
		Function<String, String> lambda = a -> a + a;
		String result = lambda.apply("1");
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped(result)));
	}
	
	@Test
	public void lambda_in_lambda() throws Exception {
		Function<String, String> lambda = input -> {
			assertThat(TestHelper.getLastRecordedStateForVariable("input"), equalTo(wrapped(input)));
			String inner = "inner" + input;
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
			
			Function<String, String> lambdaInner = inputInner -> {
				assertThat(TestHelper.getLastRecordedStateForVariable("inputInner"), equalTo(wrapped(inputInner)));
				String innerInner = "inner" + inputInner + input;
				assertThat(TestHelper.getLastRecordedStateForVariable("innerInner"), equalTo(wrapped(innerInner)));
				return innerInner;
			};
			
			return lambdaInner.apply(inner);
		};
		assertThat(TestHelper.getLastRecordedStateForVariable("lambda"), equalTo(lambda.toString()));
		
		String outer = "outer";
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped(outer)));
		String result = lambda.apply(outer);
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped(result)));
	}
	
	@Test
	public void static_method_reference() throws Exception {
		Function<String, String> fun = LambdaRecordingTest::myStaticMethod;
		String result = fun.apply("1");
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped(result)));
	}
	
	static String myStaticMethod(String s) {
		return s + s;
	}
	
	@Test
	public void method_reference() throws Exception {
		Function<String, String> fun = this::myMethod;
		String result = fun.apply("1");
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped(result)));
	}
	
	String myMethod(String s) {
		return s + s;
	}
	
	@Test
	public void constructor_reference() throws Exception {
		Function<String, String> constructor = String::new;
		String newString = constructor.apply("1");
		assertThat(TestHelper.getLastRecordedStateForVariable("newString"), equalTo(wrapped(newString)));
	}

}
