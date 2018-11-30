package hu.advancedweb.scott;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.util.function.Function;


public class VarTest {

	@Test
	public void testWithMessageSupplier() {
		var first = "Hello";
		var last = "World";

		var concatenated = first + " " + last;

		assertEquals("Goodbye World", concatenated, 
				() -> "Incorrect message.");
	}
	

}
