package hu.advancedweb.scott;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.Test;

public class ReturnRecordingTest {
	
	@Test
	public void recordReturnFromLambda() throws Exception {
		boolean primitiveBooleanTrue = Arrays.asList("abc").stream().anyMatch(s -> s.contains("abc"));
		assertEquals(true, primitiveBooleanTrue);
		assertTrue(TestHelper.containsReturnData("true"));
		
		boolean primitiveBooleanFalse = Arrays.asList("abc").stream().anyMatch(s -> s.contains("not in the list"));
		assertEquals(false, primitiveBooleanFalse);
		assertTrue(TestHelper.containsReturnData("false"));
		
		long primitiveIntValue = IntStream.builder().add(Integer.MAX_VALUE).build().reduce(0, (a, b) -> a + b);
		assertEquals(primitiveIntValue, Integer.MAX_VALUE);
		assertTrue(TestHelper.containsReturnData(Integer.toString(Integer.MAX_VALUE)));
		
		double primitiveDoubleValue = DoubleStream.builder().add(Double.MAX_VALUE).build().reduce(0D, (a, b) -> a + b);
		assertEquals(primitiveDoubleValue, Double.MAX_VALUE, 0.1D);
		assertTrue(TestHelper.containsReturnData(Double.toString(Double.MAX_VALUE)));
		
		long primitiveLongValue = LongStream.builder().add(Long.MAX_VALUE).build().reduce(0L, (a, b) -> a + b);
		assertEquals(primitiveLongValue, Long.MAX_VALUE);
		assertTrue(TestHelper.containsReturnData(Long.toString(Long.MAX_VALUE)));
		
		Supplier<String> objectSupplier = () -> "Hello world";
		String objectValue = objectSupplier.get();
		assertEquals(objectValue, "Hello world");
		assertTrue(TestHelper.containsReturnData(TestHelper.wrapped("Hello world")));
		
		Consumer<String> consumer = (s) -> {
			if (s == null) {
				return;
			}
			s.length();
		};
		
		consumer.accept(null);
		assertTrue(TestHelper.containsReturn());
	}
	
}
