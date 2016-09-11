package hu.advancedweb.example;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ListTest {
	
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
	
}
