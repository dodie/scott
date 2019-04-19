package hu.advancedweb.scott.examples;

import java.util.function.Consumer;


public class ClassWithUnhandledException {
	
	public void boom() {
		Consumer<String> unsafeConsumer = (s) -> {
			if (s == null) {
				throw new RuntimeException("Something went wrong.");
			}
			s.length();
		};
		unsafeConsumer.accept(null);
	}

}
