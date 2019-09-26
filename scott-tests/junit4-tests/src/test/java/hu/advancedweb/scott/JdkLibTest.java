package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.junit.Test;

public class JdkLibTest {
	
	/*
	 * These tests ensure that unit tests can call various JDK libs.
	 */
	
	@Test
	public void using_streams_dont_crash() throws Exception {
		List<String> strings = Arrays.asList("abc", "bc", "", "xyz", "abc");
		
		List<String> recollectedStrings = strings.stream().collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("recollectedStrings"), equalTo(recollectedStrings.toString()));
		
		Map<Integer, List<String>> groupedStrings = strings.stream().collect(Collectors.groupingByConcurrent(String::length));
		assertThat(TestHelper.getLastRecordedStateForVariable("groupedStrings"), equalTo(groupedStrings.toString()));
		
		List<String> filteredStrings = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("filteredStrings"), equalTo(filteredStrings.toString()));
		
		List<String> otherStrings = new ArrayList<>();
		strings.forEach(s -> otherStrings.add(s));
		assertThat(TestHelper.getLastRecordedStateForVariable("otherStrings"), equalTo(otherStrings.toString()));
		
		List<String> doubleStrings = strings.stream().map(s -> s + s).collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("doubleStrings"), equalTo(doubleStrings.toString()));
		
		List<String> parallellyComputedDoubleStrings = strings.parallelStream().map(s -> s + s).collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("parallellyComputedDoubleStrings"), equalTo(parallellyComputedDoubleStrings.toString()));
		
		List<String> distinctStrings = strings.stream().distinct().collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("distinctStrings"), equalTo(distinctStrings.toString()));
		
		List<String> limitedStrings = strings.stream().limit(3).collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("limitedStrings"), equalTo(limitedStrings.toString()));
		
		List<String> sortedStrings = strings.stream().sorted().collect(Collectors.toList());
		assertThat(TestHelper.getLastRecordedStateForVariable("sortedStrings"), equalTo(sortedStrings.toString()));
		
		String mergedStrings = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
		assertThat(TestHelper.getLastRecordedStateForVariable("mergedStrings"), equalTo(wrapped(mergedStrings.toString())));
		
		boolean hasX = strings.stream().anyMatch(s -> s.contains("x"));
		assertThat(TestHelper.getLastRecordedStateForVariable("hasX"), equalTo(Boolean.toString(hasX)));
	}
	
	@Test
	public void using_binary_operatior_dont_crash() throws Exception {
		BinaryOperator.maxBy(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return 0; // Does not matter.
			}
		}).apply("hello", "world");
	}

}
