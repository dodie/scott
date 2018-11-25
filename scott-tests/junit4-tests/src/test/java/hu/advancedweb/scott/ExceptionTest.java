package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionTest {
	
	@SuppressWarnings("null")
	@Test
	public void recordExceptions() throws Exception {
		String o = null;
		
		try {
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateForVariable("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateForVariable("o"), equalTo(wrapped(o)));
		}
	}
	
	@SuppressWarnings("null")
	@Test
	public void recordExceptionsWithVariablesInTheTryScope() {
		String o = null;
		
		try {
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateForVariable("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateForVariable("o"), equalTo(wrapped(o)));
		}	
	}
	
	@SuppressWarnings("null")
	@Test
	public void moreVariablesInTryBlockThanInCatchBlock() {
		String o = null;
		try {
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
			String inner2 = "inner2";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner2"), equalTo(wrapped(inner2)));
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateForVariable("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateForVariable("o"), equalTo(wrapped(o)));
		}
	}
	
	@SuppressWarnings("null")
	@Test
	public void nestedTryCatchBlocks() {
		String o = null;
		try {
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
			String inner2 = "inner2";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner2"), equalTo(wrapped(inner2)));
			o.length();
			String o_2 = null;
			try {
				String inner_2 = "inner";
				assertThat(TestHelper.getLastRecordedStateForVariable("inner_2"), equalTo(wrapped(inner_2)));
				String inner2_2 = "inner2";
				assertThat(TestHelper.getLastRecordedStateForVariable("inner2_2"), equalTo(wrapped(inner2_2)));
				o.length();
			} catch (Exception e_2) {
				o_2 = "fallback";
				assertThat(TestHelper.getLastRecordedStateForVariable("e_2"), equalTo(e_2.toString()));
				assertThat(TestHelper.getLastRecordedStateForVariable("o_2"), equalTo(wrapped(o_2)));
			}
			
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateForVariable("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateForVariable("o"), equalTo(wrapped(o)));
		}
	}
	
	@SuppressWarnings("null")
	@Test
	public void nestedTryCatchBlocks_2() {
		String o = null;
		try {
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
			String inner2 = "inner2";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner2"), equalTo(wrapped(inner2)));
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateForVariable("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateForVariable("o"), equalTo(wrapped(o)));
			
			String o_2 = null;
			try {
				String inner_2 = "inner";
				assertThat(TestHelper.getLastRecordedStateForVariable("inner_2"), equalTo(wrapped(inner_2)));
				String inner2_2 = "inner2";
				assertThat(TestHelper.getLastRecordedStateForVariable("inner2_2"), equalTo(wrapped(inner2_2)));
				o.length();
			} catch (Exception e_2) {
				o_2 = "fallback";
				assertThat(TestHelper.getLastRecordedStateForVariable("e_2"), equalTo(e_2.toString()));
				assertThat(TestHelper.getLastRecordedStateForVariable("o_2"), equalTo(wrapped(o_2)));
			}
		}
	}

	@Test(expected = NullPointerException.class)
	public void tryFinallyBlockThatThrowsException() {
		String o = null;
		ByteArrayOutputStream out = null;
		PrintStream printStream = null;

		try {
			out = new ByteArrayOutputStream();
			printStream = new PrintStream(out);

			printStream.append("Hello World");

			o.length();

		} finally {
			assertThat(TestHelper.getLastRecordedStateForVariable("out"), equalTo(out.toString()));
			printStream.close();
		}
	}

	@Test
	public void equivalentToTryWithResources() {
		try {
			Throwable throwable = null;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream printStream = new PrintStream(out);

			try {
				printStream.append("Hello World");
			} catch (Exception e) {
				throwable = e;
				throw e;
			} finally {
				assertThat(TestHelper.getLastRecordedStateForVariable("out"), equalTo(out.toString()));
				if (throwable != null) {
					try {
						printStream.close();
					} catch (Exception e) {
						throwable.addSuppressed(e);
					}
				} else {
					printStream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
