package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class RecordingTest {
	
	@Test
	public void recordInteger() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void recordShort() throws Exception {
		short s = 500;
		assertThat(TestHelper.getLastRecorderStateFor("s"), equalTo(Short.toString(s)));
	}

	@Test
	public void recordLong() throws Exception {
		long l = 1000L;
		assertThat(TestHelper.getLastRecorderStateFor("l"), equalTo(Long.toString(l)));
	}
	
	@Test
	public void recordDouble() throws Exception {
		double d = 5.5D;
		assertThat(TestHelper.getLastRecorderStateFor("d"), equalTo(Double.toString(d)));
	}
	
	@Test
	public void recordFloat() throws Exception {
		float f = 5.5F;
		assertThat(TestHelper.getLastRecorderStateFor("f"), equalTo(Float.toString(f)));
	}
	
	@Ignore // TODO: Scott record "1" for true and "0" for zero.
	@Test
	public void recordBoolean() throws Exception {
		boolean b = true;
		assertThat(TestHelper.getLastRecorderStateFor("b"), equalTo(Boolean.toString(b)));
	}
	
	@Test
	public void recordString() throws Exception {
		String s = "Hello World!";
		assertThat(TestHelper.getLastRecorderStateFor("s"), equalTo(s));
	}
	
	@Test
	public void recordNull() throws Exception {
		@SuppressWarnings("unused")
		String s = null;
		assertThat(TestHelper.getLastRecorderStateFor("s"), equalTo("null"));
	}
	
	@Test
	public void recordCustomObject() throws Exception {
		MyClass myClass = new MyClass(5);
		assertThat(TestHelper.getLastRecorderStateFor("myClass"), equalTo(myClass.toString()));
	}
	
	public static class MyClass {
		int i;
		
		public MyClass(int i) {
			this.i = i;
		}

		@Override
		public String toString() {
			return "MyClass [i=" + i + "]";
		}
	}

}
