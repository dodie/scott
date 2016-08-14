package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CounterTest {
	@Test
	public void test_1() {
		
		Counter counter = new Counter();
		
		counter.increase();
		counter.increase();
		
		int state = counter.get();
		
		assertEquals(state, 3);
		
	}

}
