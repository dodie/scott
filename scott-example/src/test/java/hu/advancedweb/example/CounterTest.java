package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;
import hu.advancedweb.example.Counter;

import org.junit.Test;

public class CounterTest {

	@Test
	public void test_1() {
		Counter calculator = new Counter();
		
		calculator.increase();
		calculator.increase();
		
		int state = calculator.get();
		
		assertEquals(state, 3);
	}

}
