package hu.advancedweb.scott.instrumentation.transformation;

class Logger {
	
	static void log(String message) {
		if ("true".equalsIgnoreCase(System.getenv("scottDebug"))) {
			System.out.println("Scott instrumentation: " + message);
		}
	}

}
