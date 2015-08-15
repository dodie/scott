package testagent;

public class MyHelper {

	public static void track(Object value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(byte value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}

	public static void track(short value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(int value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(long value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(float value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(double value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}
	
	public static void track(boolean value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}

	public static void track(char value, int lineNumber) {
		System.out.println("Tracked:" + value + ", in: " + lineNumber);
	}

}
