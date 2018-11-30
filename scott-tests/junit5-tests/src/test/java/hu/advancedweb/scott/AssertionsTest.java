package hu.advancedweb.scott;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests with assertions provided by JUnit 5 to check they don't crash Scott.
 */
public class AssertionsTest {

	@Test
	void standardAssertions() {
		assertEquals(2, 2);
		assertEquals(4, 4, "With message.");
		assertTrue(true, () -> "With message.");
	}

	@Test
	void groupedAssertions() {
		assertAll("User name", 
				() -> assertEquals("John", "John"),
				() -> assertEquals("Doe", "Doe"));
	}

	@Test
	void exceptionTesting() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			throw new IllegalArgumentException("message");
		});
		assertEquals("message", exception.getMessage());
	}

	@Test
	void timeoutNotExceeded() {
		assertTimeout(ofMinutes(2), () -> {
			assertEquals(2, 2);
		});
	}

	@Test
	void timeoutExceededWithPreemptiveTermination() {
		assertTimeoutPreemptively(ofMillis(1000), () -> {
			assertEquals(2, 2);
		});
	}
	
	@Test
	void timeoutNotExceededWithResult() {
		String actualResult = assertTimeout(ofMinutes(2), () -> {
			return "result";
		});
		assertEquals("result", actualResult);
	}

	@Test
	void timeoutNotExceededWithMethod() {
		String actualGreeting = assertTimeout(ofMinutes(2), AssertionsTest::greeting);
		assertEquals("hello world!", actualGreeting);
	}

	private static String greeting() {
		return "hello world!";
	}

}