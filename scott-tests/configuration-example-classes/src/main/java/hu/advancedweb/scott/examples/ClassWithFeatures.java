package hu.advancedweb.scott.examples;

import java.util.function.Function;

public class ClassWithFeatures {

	private static Integer I = 1;
	private static Integer i = 1;
	
	public static Integer i(int j) {
		return j+1;
	}

	public Integer ii() {
		int lo = i(4);
		lo++;
		I++;
		i++;
		
		Function<Integer, Integer> f = (x) -> x * x;
		
		return f.apply(1 + I + i + lo + iii(2));
	}

	private int iii(int j) {
		return j * j;
	}
}
