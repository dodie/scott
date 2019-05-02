package hu.advancedweb.scott.instrumentation.transformation;

class Logger {
	
	private boolean enabled;
	
	Logger(boolean enabled) {
		this.enabled = enabled;
	}
	
	void log(String message) {
		if (enabled) {
			System.out.println("Scott instrumentation: " + message);
		}
	}

}
