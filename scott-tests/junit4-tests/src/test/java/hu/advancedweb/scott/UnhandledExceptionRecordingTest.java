package hu.advancedweb.scott;

import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Test;

public class UnhandledExceptionRecordingTest {
	
	@Test
	public void recordUnhandledExceptionsInLambda() throws Exception {
		Consumer<String> unsafeConsumer = (s) -> {
			if (s == null) {
				throw new RuntimeException("Something went wrong.");
			}
			s.length();
		};
		
		try {
			unsafeConsumer.accept(null);
		} catch (Exception e) {
			// Don't care.
		}
		
		assertTrue(TestHelper.containsUnhandledException(RuntimeException.class, "Something went wrong."));
	}
	
}
