package hu.advancedweb.scott.examples;



import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ClassWithLambdas {
	
	public void primitiveBooleanTrue() {
		Arrays.asList("abc").stream().anyMatch(s -> s.contains("abc"));
	}
	
	public void primitiveBooleanFalse() {
		Arrays.asList("abc").stream().anyMatch(s -> s.contains("not in the list"));
	}
	
	public void primitiveInt() {
		IntStream.builder().add(Integer.MAX_VALUE).build().reduce(0, (a, b) -> a + b);
	}
	
	public void primitiveDouble() {
		DoubleStream.builder().add(Double.MAX_VALUE).build().reduce(0D, (a, b) -> a + b);
	}
	
	public void primitiveLong() {
		LongStream.builder().add(Long.MAX_VALUE).build().reduce(0L, (a, b) -> a + b);
	}
	
	public void object() {
		Supplier<String> objectSupplier = () -> "Hello world";
		objectSupplier.get();
	}
	
	public void simpleReturn() {
		Consumer<String> consumer = (s) -> {
			if (s == null) {
				return;
			}
			s.length();
		};
		consumer.accept(null);
	}

}
