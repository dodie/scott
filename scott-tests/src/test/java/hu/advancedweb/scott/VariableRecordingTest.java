package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class VariableRecordingTest {
	
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
	
	@Test
	public void recordBoolean() throws Exception {
		boolean bt = true;
		assertThat(TestHelper.getLastRecordedStateFor("bt"), equalTo(Boolean.toString(bt)));
		
		boolean bf = false;
		assertThat(TestHelper.getLastRecordedStateFor("bf"), equalTo(Boolean.toString(bf)));
		
		boolean bot = Boolean.TRUE;
		assertThat(TestHelper.getLastRecordedStateFor("bot"), equalTo(Boolean.toString(bot)));
		
		boolean bof = Boolean.FALSE;
		assertThat(TestHelper.getLastRecordedStateFor("bof"), equalTo(Boolean.toString(bof)));
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
	public void recordArray() throws Exception {
		@SuppressWarnings("unused")
		String[] array = {"a", "b"};
		assertThat(TestHelper.getLastRecordedStateFor("array"), equalTo("[a, b]"));
	}

	@Test
	public void recordEmptyArray() throws Exception {
		@SuppressWarnings("unused")
		String[] array = {};
		assertThat(TestHelper.getLastRecordedStateFor("array"), equalTo("[]"));
	}

	@Test
	public void recordCustomObject() throws Exception {
		CustomClass myClass = new CustomClass(5);
		assertThat(TestHelper.getLastRecordedStateFor("myClass"), equalTo(myClass.toString()));
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
		assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
		assertThat(TestHelper.getLastRecordedStateFor("inner2"), equalTo(inner2));
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
		assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
		assertThat(TestHelper.getLastRecordedStateFor("inner2"), equalTo(inner2));
		assertThat(TestHelper.getLastRecordedStateFor("inner3"), equalTo(inner3));
		assertThat(TestHelper.getLastRecordedStateFor("inner4"), equalTo(inner4));
		assertThat(TestHelper.getLastRecordedStateFor("inner5"), equalTo(inner5));
		assertThat(TestHelper.getLastRecordedStateFor("inner6"), equalTo(inner6));
		assertThat(TestHelper.getLastRecordedStateFor("inner7"), equalTo(inner7));
		assertThat(TestHelper.getLastRecordedStateFor("inner8"), equalTo(inner8));
		assertThat(TestHelper.getLastRecordedStateFor("inner9"), equalTo(inner9));
		assertThat(TestHelper.getLastRecordedStateFor("inner10"), equalTo(inner10));
	}

	@SuppressWarnings("unused")
	@Test
	public void methodWithJustADeclarationDontCrash() {
		String outer = "outer!";
	}
	
	@Test
	public void recordMethodWithJustADeclaration() {
		String outer = "outer!";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}
	
	@Test
	public void recordMethodWithJustADeclarationInABlock() {
		{
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
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
	public void methodEndingWithADeclarationsInABlockDontCrash() {
		{
			String inner = "inner";
			System.out.println("");
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
	
	// FIXME: we can't record the name of a variable, if its declaration is the last statement in a block. See issue #15.
//	@Test
//	public void captureLastVariableName() throws Exception {
//		String outer = "outer";
//		{
//			String inner = "inner";
//		}
//		
//		assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo("inner")); // <- this fails, we track the value, but not the name
//		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo("outer"));
//	}

}
