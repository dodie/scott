package hu.advancedweb.scott;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class NestedClassTest {

	String value;

	@BeforeEach
	void reset() {
		value = "1";
	}

	@Test
	void test() {
		String dot = ".";
		value += dot;
		assertEquals("1", value);
	}

	@Nested
	class NestedClass {

		String nestedValue = "a";

		@BeforeEach
		void reset() {
			value = value + "2";
		}

		@Test
		void test() {
			String dot = ".";
			nestedValue += dot;
			value += dot + nestedValue;

			assertEquals("12.a", value);
		}

	}
}