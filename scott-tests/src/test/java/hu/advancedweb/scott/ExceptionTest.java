package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class ExceptionTest {
	
	@SuppressWarnings("null")
	@Test
	public void recordExceptions() throws Exception {
		String o = null;
		
		try {
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecorderStateFor("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecorderStateFor("o"), equalTo(o));
		}
	}
	
}
