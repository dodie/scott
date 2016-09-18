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
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void recordShort() throws Exception {
		short s = 500;
		assertThat(TestHelper.getLastRecordedStateFor("s"), equalTo(Short.toString(s)));
	}

	@Test
	public void recordLong() throws Exception {
		long l = 1000L;
		assertThat(TestHelper.getLastRecordedStateFor("l"), equalTo(Long.toString(l)));
	}
	
	@Test
	public void recordDouble() throws Exception {
		double d = 5.5D;
		assertThat(TestHelper.getLastRecordedStateFor("d"), equalTo(Double.toString(d)));
	}
	
	@Test
	public void recordFloat() throws Exception {
		float f = 5.5F;
		assertThat(TestHelper.getLastRecordedStateFor("f"), equalTo(Float.toString(f)));
	}
	
	// FIXME: Scott record "1" for true and "0" for zero. See Issue #1.
	@Ignore
	@Test
	public void recordBoolean() throws Exception {
		boolean b = true;
		assertThat(TestHelper.getLastRecordedStateFor("b"), equalTo(Boolean.toString(b)));
	}
	
	@Test
	public void recordString() throws Exception {
		String s = "Hello World!";
		assertThat(TestHelper.getLastRecordedStateFor("s"), equalTo(s));
	}
	
	@Test
	public void recordNull() throws Exception {
		@SuppressWarnings("unused")
		String s = null;
		assertThat(TestHelper.getLastRecordedStateFor("s"), equalTo("null"));
	}
	
	@Test
	public void recordCustomObject() throws Exception {
		CustomClass myClass = new CustomClass(5);
		assertThat(TestHelper.getLastRecordedStateFor("myClass"), equalTo(myClass.toString()));
	}
	
//	//FIXME: This test kills the whole Test file, nothing is recorded. See Issue #2.
//	// If I add a single Sysout to the inner block it seems to fix it.
//	@Test
//	public void mutationWithBlockThatHasASingleDeclaration() throws Exception {
//		String outer = "outer";
//		{
//			String inner = "inner";
//		}
//		
//		assertThat(TestHelper.getLastRecorderStateFor("inner"), equalTo("inner"));
//		assertThat(TestHelper.getLastRecorderStateFor("outer"), equalTo("outer"));
//	}
	
	public static class CustomClass {
		int i;
		
		public CustomClass(int i) {
			this.i = i;
		}

		@Override
		public String toString() {
			return "MyClass [i=" + i + "]";
		}
	}

}
