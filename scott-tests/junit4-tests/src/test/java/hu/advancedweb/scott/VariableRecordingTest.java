package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class VariableRecordingTest {
	
	@Test
	public void recordInteger() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void recordShort() throws Exception {
		short s = 500;
		assertThat(TestHelper.getLastRecordedStateForVariable("s"), equalTo(Short.toString(s)));
	}

	@Test
	public void recordLong() throws Exception {
		long l = 1000L;
		assertThat(TestHelper.getLastRecordedStateForVariable("l"), equalTo(Long.toString(l)));
	}
	
	@Test
	public void recordDouble() throws Exception {
		double d = 5.5D;
		assertThat(TestHelper.getLastRecordedStateForVariable("d"), equalTo(Double.toString(d)));
	}
	
	@Test
	public void recordFloat() throws Exception {
		float f = 5.5F;
		assertThat(TestHelper.getLastRecordedStateForVariable("f"), equalTo(Float.toString(f)));
	}
	
	@Test
	public void recordBoolean() throws Exception {
		boolean bt = true;
		assertThat(TestHelper.getLastRecordedStateForVariable("bt"), equalTo(Boolean.toString(bt)));
		
		boolean bf = false;
		assertThat(TestHelper.getLastRecordedStateForVariable("bf"), equalTo(Boolean.toString(bf)));
		
		boolean bot = Boolean.TRUE;
		assertThat(TestHelper.getLastRecordedStateForVariable("bot"), equalTo(Boolean.toString(bot)));
		
		boolean bof = Boolean.FALSE;
		assertThat(TestHelper.getLastRecordedStateForVariable("bof"), equalTo(Boolean.toString(bof)));
	}
	
	@Test
	public void recordString() throws Exception {
		String s = "Hello World!";
		assertThat(TestHelper.getLastRecordedStateForVariable("s"), equalTo(wrapped(s)));
	}
	
	@Test
	public void recordNull() throws Exception {
		@SuppressWarnings("unused")
		String s = null;
		assertThat(TestHelper.getLastRecordedStateForVariable("s"), equalTo("null"));
	}
	
	@Test
	public void recordArray() throws Exception {
		@SuppressWarnings("unused")
		String[] array = {"a", "b"};
		assertThat(TestHelper.getLastRecordedStateForVariable("array"), equalTo("[a, b]"));
	}

	@Test
	public void recordEmptyArray() throws Exception {
		@SuppressWarnings("unused")
		String[] array = {};
		assertThat(TestHelper.getLastRecordedStateForVariable("array"), equalTo("[]"));
	}

	@Test
	public void recordCustomObject() throws Exception {
		CustomClass myClass = new CustomClass(5);
		assertThat(TestHelper.getLastRecordedStateForVariable("myClass"), equalTo(myClass.toString()));
	}
	
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
	
	@Test
	public void recordConsecutiveDeclarations() {
		String inner = "inner";
		String inner2 = "inner2";
		assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner2"), equalTo(wrapped(inner2)));
	}
	
	@Test
	public void recordManyDeclarations() {
		String inner = "inner";
		String inner2 = "inner2";
		String inner3 = "inner3";
		String inner4 = "inner4";
		String inner5 = "inner5";
		String inner6 = "inner6";
		String inner7 = "inner7";
		String inner8 = "inner8";
		String inner9 = "inner9";
		String inner10 = "inner10";
		assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner2"), equalTo(wrapped(inner2)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner3"), equalTo(wrapped(inner3)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner4"), equalTo(wrapped(inner4)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner5"), equalTo(wrapped(inner5)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner6"), equalTo(wrapped(inner6)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner7"), equalTo(wrapped(inner7)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner8"), equalTo(wrapped(inner8)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner9"), equalTo(wrapped(inner9)));
		assertThat(TestHelper.getLastRecordedStateForVariable("inner10"), equalTo(wrapped(inner10)));
	}

	@SuppressWarnings("unused")
	@Test
	public void methodWithJustADeclarationDontCrash() {
		String outer = "outer!";
	}
	
	@Test
	public void recordMethodWithJustADeclaration() {
		String outer = "outer!";
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped(outer)));
	}
	
	@Test
	public void recordMethodWithJustADeclarationInABlock() {
		{
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void methodWithJustADeclarationInABlockDontCrash() {
		{
			String inner = "inner";
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void methodWithJustDeclarationsInABlockDontCrash() {
		{
			String inner = "inner";
			String inner2 = "inner2";
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void methodEndingWithADeclarationInABlockDontCrash() {
		{
			String inner = "inner";
			System.out.print(""); // Some other statement.
			String inner2 = "inner2";
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void methodWithJustADeclarationInATryBlockDontCrash() {
		try {
			String inner = "inner";
		} catch (Exception e) {
			// Don't care.
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void methodWithJustADeclarationInACatchBlockDontCrash() {
		try {
			throw new NullPointerException();
		} catch (Exception e) {
			String inner = "inner";
		}
	}
	
}
