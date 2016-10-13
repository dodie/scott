package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class FieldRecordingInitialValuesTest {
	
	int a = 10;
	int b = 11;
	static int A = 12;
	static int B = 13;
	
	
	@Test
	public void recordFieldAccess() throws Exception {
		// track initial states of accessed fields
		assertThat(TestHelper.getLastRecordedStateForField("this.a"), equalTo("10"));
		assertThat(TestHelper.getLastRecordedStateForField("this.b"), equalTo("11"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.A"), equalTo("12"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.B"), equalTo("13"));
		
		@SuppressWarnings("unused")
		String accessed = "" + a + b + A + B;
	}
	
	@Test
	public void recordFieldSubsetAccess() throws Exception {
		// track initial states of accessed fields
		assertThat(TestHelper.getLastRecordedStateForField("this.a"), equalTo("10"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.B"), equalTo("13"));
		
		// don't record not accessed fields
		assertThat(TestHelper.getLastRecordedStateForField("b"), equalTo(null));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.A"), equalTo(null));
		
		@SuppressWarnings("unused")
		String accessed = "" + a + B;
	}
	
	@Test
	public void recordWrite() throws Exception {
		// initial value match
		assertThat(TestHelper.getLastRecordedStateForField("this.a"), equalTo("10"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.A"), equalTo("12"));
		
		a = 20;
		A = 22;
		
		// values after write match
		assertThat(TestHelper.getLastRecordedStateForField("this.a"), equalTo("20"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.A"), equalTo("22"));
	}
	
	@Test
	public void recordReadAndWrite() throws Exception {
		// initial modified values match
		assertThat(TestHelper.getLastRecordedStateForField("this.b"), equalTo("11"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.B"), equalTo("13"));
		
		@SuppressWarnings("unused")
		String accessed = "" + b + B;
		
		// no change after reading
		assertThat(TestHelper.getLastRecordedStateForField("this.b"), equalTo("11"));
		assertThat(TestHelper.getLastRecordedStateForField("FieldRecordingInitialValuesTest.B"), equalTo("13"));
	}
	
}
