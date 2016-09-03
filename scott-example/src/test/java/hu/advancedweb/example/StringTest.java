package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import hu.advancedweb.scott.runtime.ScottReportingRule;

public class StringTest {
	
	@Rule public ScottReportingRule rule = new ScottReportingRule();

	@Test
	public void test_1() {
		String first = "Hello";
		String last = "World";

		String concatenated = first + " " + last;

		assertEquals("Goodbye World", concatenated);
	}

	@Test
	public void test_2() {
		String hello = "Hello World";

		int indexOfSpace = hello.indexOf(" ");
		String lastPart = hello.substring(indexOfSpace);

		assertEquals("World", lastPart);
	}

}
