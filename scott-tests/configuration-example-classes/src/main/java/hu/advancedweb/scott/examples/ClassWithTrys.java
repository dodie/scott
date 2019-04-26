package hu.advancedweb.scott.examples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ClassWithTrys {

	public boolean hello() {
		try {
			if (this.getClass().getName().isEmpty()) {
				return false;
			}

			String string = "a";
			return true;
		} finally {
			System.out.println("Hey");
		}
	}

	public void hello2() throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				PrintStream printStream = new PrintStream(out)) {

			printStream.append("Hello World");
		}
	}

}
