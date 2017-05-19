package hu.advancedweb.scott;

public class StaticFieldRecordingTestHelper {
	
	/*
	 * Scott can't track constants (public static final), because they get inlined at compile time.
	 * This is the reason of the SOME_VALUE_TO_READ field not being final.
	 */
	
	public static String SOME_VALUE_TO_READ = "42";
	public static String SOME_VALUE_TO_WRITE = "before_write";
}
