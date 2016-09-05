package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;
import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

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
	
	// FIXME: "e" doesn't get recorded
	@Ignore
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
