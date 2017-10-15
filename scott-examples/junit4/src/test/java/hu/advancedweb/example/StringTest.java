package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringTest {

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

	@Test
	public void test_3() {
		String empty = "";
		String first = "1";
		String last = "2";

		String concatenated = empty + first + last;

		assertEquals(12, concatenated);
	}

}
