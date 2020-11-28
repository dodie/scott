package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class StaticFieldRecordingTest extends StaticFieldRecordingTestHelperSuperClass implements StaticFieldRecordingTestHelperInterface {
	
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
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.OBJECT"), equalTo(wrapped(OBJECT)));
	}
	
	@Test
	public void recordForeignStaticField() throws Exception {
		String s = StaticFieldRecordingTestHelper.SOME_VALUE_TO_READ;
		assertThat(s, equalTo(StaticFieldRecordingTestHelper.SOME_VALUE_TO_READ));
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTestHelper.SOME_VALUE_TO_READ"), equalTo(wrapped("42")));
	}
	
	@Test
	public void recordForeignStaticFieldModification() throws Exception {
		/*
		 * Note that Scott can't track constants (public static final), because they get inlined at compile time.
		 * The observer static fields in the test are not final.
		 */
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTestHelper.SOME_VALUE_TO_WRITE"), equalTo(wrapped("before_write")));
		StaticFieldRecordingTestHelper.SOME_VALUE_TO_WRITE = "after_write";
		assertThat(StaticFieldRecordingTestHelper.SOME_VALUE_TO_WRITE, equalTo("after_write"));
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTestHelper.SOME_VALUE_TO_WRITE"), equalTo(wrapped("after_write")));
	}
	
	@Ignore
	@Test
	public void recordInterfaceStaticField() throws Exception {
		/*
		 * This test is intentionally ignored, because Scott can't track constants (public static final).
		 * The static fields from interfaces are final by design.
		 */
		String s = SOME_VALUE_FROM_INTERFACE;
		assertThat(s, equalTo(SOME_VALUE_FROM_INTERFACE));
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTestHelperInterface.SOME_VALUE_FROM_INTERFACE"), equalTo("42"));
	}
	
	@Test
	public void recordSuperClassStaticField() throws Exception {
		String s = SOME_VALUE_FROM_SUPER_TO_READ;
		assertThat(s, equalTo(SOME_VALUE_FROM_SUPER_TO_READ));
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.SOME_VALUE_FROM_SUPER_TO_READ"), equalTo(wrapped("super")));
	}
	
	@Test
	public void recordSuperClassStaticFieldModification() throws Exception {
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.SOME_VALUE_FROM_SUPER_TO_WRITE"), equalTo(wrapped("before_write")));
		SOME_VALUE_FROM_SUPER_TO_WRITE = "after_write";
		assertThat(SOME_VALUE_FROM_SUPER_TO_WRITE, equalTo("after_write"));
		assertThat(TestHelper.getLastRecordedStateForField("StaticFieldRecordingTest.SOME_VALUE_FROM_SUPER_TO_WRITE"), equalTo(wrapped("after_write")));
	}

}
