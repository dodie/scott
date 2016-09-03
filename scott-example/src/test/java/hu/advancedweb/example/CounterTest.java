package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import hu.advancedweb.scott.runtime.ScottReportingRule;

public class CounterTest {
	
	@Rule public ScottReportingRule rule = new ScottReportingRule();
	
	@Test
	public void test_1() {
		Counter counter = new Counter();
		
		counter.increase();
		counter.increase();
		
		int state = counter.get();
		
		assertEquals(state, 3);
	}

}
