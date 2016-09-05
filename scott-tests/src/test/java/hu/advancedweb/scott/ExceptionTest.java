package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
			assertThat(TestHelper.getLastRecordedStateFor("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateFor("o"), equalTo(o));
		}
	}
	
	@SuppressWarnings("null")
	@Test
	public void recordExceptionsWithVariablesInTheTryScope() {
		String o = null;
		
		try {
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateFor("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateFor("o"), equalTo(o));
		}	
	}
	
}
