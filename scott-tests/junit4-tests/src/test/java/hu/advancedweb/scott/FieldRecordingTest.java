package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FieldRecordingTest {
	
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
	public void recordInteger() throws Exception {
		i = 5;
		assertThat(TestHelper.getLastRecordedStateForField("this.i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void recordShort() throws Exception {
		s = 500;
		assertThat(TestHelper.getLastRecordedStateForField("this.s"), equalTo(Short.toString(s)));
	}

	@Test
	public void recordLong() throws Exception {
		l = 1000L;
		assertThat(TestHelper.getLastRecordedStateForField("this.l"), equalTo(Long.toString(l)));
	}
	
	@Test
	public void recordDouble() throws Exception {
		d = 5.5D;
		assertThat(TestHelper.getLastRecordedStateForField("this.d"), equalTo(Double.toString(d)));
	}
	
	@Test
	public void recordFloat() throws Exception {
		f = 5.5F;
		assertThat(TestHelper.getLastRecordedStateForField("this.f"), equalTo(Float.toString(f)));
	}
	
	@Test
	public void recordBoolean() throws Exception {
		bool = true;
		assertThat(TestHelper.getLastRecordedStateForField("this.bool"), equalTo(Boolean.toString(bool)));
	}
	
	@Test
	public void recordString() throws Exception {
		object = "Hello World!";
		assertThat(TestHelper.getLastRecordedStateForField("this.object"), equalTo(wrapped(object)));
	}

}
