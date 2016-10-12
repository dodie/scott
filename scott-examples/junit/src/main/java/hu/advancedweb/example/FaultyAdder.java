package hu.advancedweb.example;

public class FaultyAdder {

	public static int add(int a, int b) {
		if (a == 2 && b == 2) {
			return 5; // Yuck! A bug!
		} else {
			return a + b;
		}
	}
	
}
