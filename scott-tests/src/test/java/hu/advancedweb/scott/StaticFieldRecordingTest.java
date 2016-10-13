package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class StaticFieldRecordingTest {
	
	static byte B = 0;
	static short S = 0;
	static int I = 0;
	static long L = 0L;
	static float F = 0.0F;
	static double D = 0.0D;
	static boolean BOOL = false;
	static char C = 'i';
	static String OBJECT = "initial";

	
	@Test
	public void recordInteger() throws Exception {
		I = 5;
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.I"), equalTo(Integer.toString(I)));
	}
	
	@Test
	public void recordShort() throws Exception {
		S = 500;
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.S"), equalTo(Short.toString(S)));
	}

	@Test
	public void recordLong() throws Exception {
		L = 1000L;
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.L"), equalTo(Long.toString(L)));
	}
	
	@Test
	public void recordDouble() throws Exception {
		D = 5.5D;
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.D"), equalTo(Double.toString(D)));
	}
	
	@Test
	public void recordFloat() throws Exception {
		F = 5.5F;
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.F"), equalTo(Float.toString(F)));
	}
	
	@Test
	public void recordBoolean() throws Exception {
		BOOL = true;
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.BOOL"), equalTo(Boolean.toString(BOOL)));
	}
	
	@Test
	public void recordString() throws Exception {
		OBJECT = "Hello World!";
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.OBJECT"), equalTo(OBJECT));
	}

}
