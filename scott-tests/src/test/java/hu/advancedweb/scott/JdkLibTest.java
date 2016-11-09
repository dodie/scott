//package hu.advancedweb.scott;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.function.BinaryOperator;
//import java.util.stream.Collectors;
//
//import org.junit.Test;
//
//public class JdkLibTest {
//	
//	/*
//	 * These tests ensure that unit tests can call various JDK libs.
//	 */
//	
//	// FIXME: See Issue #22
//	
//	@Test
//	public void stream_collector_test() throws Exception {
//		List<String> list = new ArrayList<String>();
//		list.add("a");
//		list.add("b");
//		list.stream().collect(Collectors.toList());
//		list.stream().collect(Collectors.groupingByConcurrent(String::length));
//	}
//	
//	@Test
//	public void binary_operatior_test() throws Exception {
//		BinaryOperator.maxBy(new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				return 0; // Does not matter.
//			}
//		}).apply("hello", "world");
//	}
//
//}
