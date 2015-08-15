package testagent;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class MyHelper extends RunListener {
	
	static List<String> events = new ArrayList<String>();
	
	@Override
	public void testStarted(Description description) throws Exception {
		events.clear();
		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		System.out.println(failure.getTestHeader() + " FAILED!");
		for (String event : events) {
			System.out.println(event);
		}
		System.out.println("----------");
		
		super.testFailure(failure);
	}

	public static void track(Object value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(byte value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}

	public static void track(short value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(int value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(long value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(float value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(double value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(boolean value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}

	public static void track(char value, int lineNumber) {
		events.add("Tracked:" + value + ", in: " + lineNumber);
	}

}
