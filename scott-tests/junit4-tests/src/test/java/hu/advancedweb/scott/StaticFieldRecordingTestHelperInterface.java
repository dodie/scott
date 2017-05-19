package hu.advancedweb.scott;

public interface StaticFieldRecordingTestHelperInterface {
	
	/*
	 * Scott can't track constants (public static final), because they get inlined at compile time.
	 * So we don't have any test for the following field.
	 */
	
	public static String SOME_VALUE_FROM_INTERFACE = "interface";
}
