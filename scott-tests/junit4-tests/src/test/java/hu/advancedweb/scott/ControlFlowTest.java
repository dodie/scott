package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ControlFlowTest {
	
	@Test
	public void for_test() throws Exception {
		for (int i = 0; i < 10; i++) {
			int j = i * 2;
			assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
			assertThat(TestHelper.getLastRecordedStateForVariable("j"), equalTo(Integer.toString(j)));
		}
	}

}
