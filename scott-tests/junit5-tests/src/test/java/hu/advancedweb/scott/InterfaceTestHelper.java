package hu.advancedweb.scott;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public interface InterfaceTestHelper {

	@BeforeAll
	static void beforeAllTests() {
	}

	@AfterAll
	static void afterAllTests() {
	}

	@BeforeEach
	default void beforeEachTest(TestInfo testInfo) {
	}

	@AfterEach
	default void afterEachTest(TestInfo testInfo) {
	}

}
