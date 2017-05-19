package hu.advancedweb.scott;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import hu.advancedweb.scott.helper.TestHelper;

class NestedClassTest {

	String value;

	@BeforeEach
	void reset() {
		value = "1";
	}

	@Test
	void test() {
		String dot = ".";
		value += dot;
		assertEquals(dot, TestHelper.getLastRecordedStateForVariable("dot"));
		assertEquals(value, TestHelper.getLastRecordedStateForField("this.value"));
		assertEquals("1.", value);
	}

	@Nested
	class OuterNested {

		String outerNestedValue = "a";

		@BeforeEach
		void reset() {
			value = value + "2";
		}

		@Test
		void test() {
			String dot = ".";
			outerNestedValue += dot;
			value += dot + outerNestedValue;

			assertEquals(dot, TestHelper.getLastRecordedStateForVariable("dot"));
			assertEquals(value, TestHelper.getLastRecordedStateForField("(in enclosing NestedClassTest) value"));
			assertEquals(outerNestedValue, TestHelper.getLastRecordedStateForField("this.outerNestedValue"));
			assertEquals("12.a.", value);
		}

		@Nested
		class MiddleNested {
			String middleNestedValue = "I";

			@BeforeEach
			void reset() {
				value = value + "3";
			}

			@Test
			void test() {
				String dot = ".";
				middleNestedValue += dot;
				outerNestedValue += dot;
				value += dot + outerNestedValue + middleNestedValue;

				assertEquals(dot, TestHelper.getLastRecordedStateForVariable("dot"));
				assertEquals(value, TestHelper.getLastRecordedStateForField("(in enclosing NestedClassTest) value"));
				assertEquals(outerNestedValue, TestHelper.getLastRecordedStateForField("(in enclosing OuterNested) outerNestedValue"));
				assertEquals(middleNestedValue, TestHelper.getLastRecordedStateForField("this.middleNestedValue"));
				assertEquals("123.a.I.", value);
			}
			
			@Nested
			class InnerNested {
				String innerNestedValue = "x";
				
				@BeforeEach
				void reset() {
					value = value + "4";
				}

				@Test
				void test() {
					String dot = ".";
					innerNestedValue += dot;
					middleNestedValue += dot;
					outerNestedValue += dot;
					value += dot + outerNestedValue + middleNestedValue + innerNestedValue;

					assertEquals(dot, TestHelper.getLastRecordedStateForVariable("dot"));
					assertEquals(value, TestHelper.getLastRecordedStateForField("(in enclosing NestedClassTest) value"));
					assertEquals(outerNestedValue, TestHelper.getLastRecordedStateForField("(in enclosing OuterNested) outerNestedValue"));
					assertEquals(middleNestedValue, TestHelper.getLastRecordedStateForField("(in enclosing MiddleNested) middleNestedValue"));
					assertEquals(innerNestedValue, TestHelper.getLastRecordedStateForField("this.innerNestedValue"));
					assertEquals("1234.a.I.x.", value);
				}
			}
		}
	}
}