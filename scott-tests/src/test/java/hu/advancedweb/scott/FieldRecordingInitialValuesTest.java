package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class FieldRecordingInitialValuesTest {
	
	byte b = 0;
	short s = 0;
	int i = 0;
	long l = 0L;
	float f = 0.0F;
	double d = 0.0D;
	boolean bool = false;
	char c = 'i';
	String object = "initial";
	
	
	@Test
	public void test_1_recordFieldAccess() throws Exception {
		// track initial states of accessed fields
		assertThat(TestHelper.getLastRecordedStateForField("b"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("s"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("i"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("l"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("f"), equalTo("0.0"));
		assertThat(TestHelper.getLastRecordedStateForField("d"), equalTo("0.0"));
		assertThat(TestHelper.getLastRecordedStateForField("bool"), equalTo("false"));
		assertThat(TestHelper.getLastRecordedStateForField("c"), equalTo("i"));
		assertThat(TestHelper.getLastRecordedStateForField("object"), equalTo("initial"));
		
		@SuppressWarnings("unused")
		String accessed = "" + b + s + i + l + f + d + bool + c + object;
	}
	
	@Test
	public void test_2_recordFieldSubsetAccess() throws Exception {
		// track initial states of accessed fields
		assertThat(TestHelper.getLastRecordedStateForField("b"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("s"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("i"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("l"), equalTo("0"));
		assertThat(TestHelper.getLastRecordedStateForField("f"), equalTo("0.0"));
		assertThat(TestHelper.getLastRecordedStateForField("d"), equalTo("0.0"));
		
		// don't record not accessed fields
		assertThat(TestHelper.getLastRecordedStateForField("bool"), equalTo(null));
		assertThat(TestHelper.getLastRecordedStateForField("c"), equalTo(null));
		assertThat(TestHelper.getLastRecordedStateForField("object"), equalTo(null));
		
		@SuppressWarnings("unused")
		String accessed = "" + b + s + i + l + f + d; 
	}
	
	@Test
	public void test_3_recordWrite() throws Exception {
		// initial value match
		assertThat(TestHelper.getLastRecordedStateForField("i"), equalTo("0"));
		
		i = 10;
		
		// values after write match
		assertThat(TestHelper.getLastRecordedStateForField("i"), equalTo("10"));
	}
	
	@Test
	public void test_4_recordReadAndWrite() throws Exception {
		// initial modified values match
		assertThat(TestHelper.getLastRecordedStateForField("i"), equalTo("0"));
		
		@SuppressWarnings("unused")
		String accessed = "" + i;
		
		// no change after reading
		assertThat(TestHelper.getLastRecordedStateForField("i"), equalTo("0"));
	}
	
}
