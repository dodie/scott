package hu.advancedweb.scott;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test with JUnit 5 assertions")
public class JUnit5DemoTest {

	@Test
	public void testWithMessageSupplier() {
		String first = "Hello";
		String last = "World";

		String concatenated = first + " " + last;

		assertEquals("Goodbye World", concatenated, 
				() -> "Incorrect message.");
	}
	
	@Test
	@DisplayName("This is a test with multiple assertions.")
	public void testWithGroupedAssertions() {
		Integer[] myArray = new Integer[] { 1, 4, 2, 4 };
		List<Integer> myList = Arrays.asList(myArray);
		
		Set<Integer> mySet = new HashSet<>(myList);
		mySet.remove(4);

		assertAll("mySet is too small", 
				() -> assertTrue(mySet.size() > 2, "It does not contain a whole lot of numbers!"),
				() -> assertTrue(mySet.contains(4), "It does not even contain 4!"));
	}

	@Test
	@DisplayName("😱")
	void exceptionTesting() {
		assertThrows(NullPointerException.class, () -> {
			List<String> set = new ArrayList<>();
			set.add("I");
			set.add("will");
			set.add("not");
			set.add("explode!");
			assertTrue(set.size() > 1);
		});
	}

	@Test
	void timeoutExceedingTest() {
		assertTimeout(ofMillis(2), () -> {
			String calculate = "slow";
			calculate += "operation";
			Thread.sleep(1000L);
		});
	}

	@Test
	void timeoutNotExceededWithResult() {
		String actualResult = assertTimeout(ofMinutes(2), () -> {
			return "result";
		});
		assertEquals("no result", actualResult);
	}
}