package hu.advancedweb.example;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;

import hu.advancedweb.scott.runtime.ScottReportingRule;

public class ListTest {
	
	@Rule public ScottReportingRule rule = new ScottReportingRule();
	
	@Test
	public void myTest() {
		Integer[] myArray = new Integer[] { 1, 4, 2, 4 };
		List<Integer> myList = Arrays.asList(myArray);
		
		Set<Integer> mySet = new HashSet<>(myList);
		mySet.remove(4);

		assertTrue(mySet.contains(4));
	}
	
	@Test
	public void test_1() {
		Integer[] myArray = new Integer[] { 1, 4, 2, 4 };
		List<Integer> myList = Arrays.asList(myArray);
		
		Set<Integer> mySet = new HashSet<Integer>(myList);
		mySet.remove(1);

		assertEquals(myList.size(), mySet.size());
	}
	
	@Test
	public void test_2() throws Throwable {
		Integer[] array = new Integer[] { 1, 4, 2, 3 };
		List<Integer> list = Arrays.asList(array);
		Collections.sort(list);

		assertArrayEquals(array, new Integer[] { 1, 4, 2, 3 });
	}
	
	
	
	// FIXME
	@Test
	public void test_3() {
		try {
			String a = null;
			System.out.println(a);
			a.getBytes();
		} catch (Exception e) {
			// FIXME: bug: determined scope of 'a' somehow contains the catch block too
			System.out.println(e);
		}
		String a = "";
		System.out.println(a);
		assertEquals(1, 2);
	}
	
	// FIXME: this does not render anyting
	@Test
	public void test_4() throws Exception {
		String b = "outer";
		{
			String a = "inner";
		}
		
		// if i remove this line, it works fine
		b = "Y"; 
		assertEquals(1, 2);
	}
	
}
