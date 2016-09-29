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
	
	// FIXME: if I uncomment this test, it kills the VM
//	@SuppressWarnings("null")
//	@Test
//	public void moreVariablesInTryBlockThanInCatchBlock() {
//		String o = null;
//		try {
//			String inner = "inner";
//			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
//			String inner2 = "inner2";
//			assertThat(TestHelper.getLastRecordedStateFor("inner2"), equalTo(inner2));
//			o.length();
//		} catch (Exception e) {
//			o = "fallback";
//			// the scope determination is wrong, so without this variable the following assertThat still going to be instrumented
//			// to query var=3, which is not existent
////			String x = "";
//			assertThat(TestHelper.getLastRecordedStateFor("e"), equalTo(e.toString()));
//			assertThat(TestHelper.getLastRecordedStateFor("o"), equalTo(o));
//		}
//	}
	
}
