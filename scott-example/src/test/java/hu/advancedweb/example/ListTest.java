package hu.advancedweb.example;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class ListTest {
	
	@Test
	public void test_1() {
		Integer[] array = new Integer[] { 1, 4, 2, 4 };
		List<Integer> list = Arrays.asList(array);
		
		Set<Integer> set = new HashSet<Integer>(list);
		
		assertEquals(list.size(), set.size());
	}
	
	@Test
	public void test_2() throws Throwable {
		Integer[] array = new Integer[] { 1, 4, 2, 3 };
		List<Integer> list = Arrays.asList(array);
		
		Collections.sort(list);

		assertEquals(1, 2);
	}
	
	@Test
	public void test_3() {
		try {
			// TODO: log nulls
			String a = null;
			System.out.println(a);
			a.getBytes();
		} catch (Exception e) {
			// TODO: bug: determined scope of 'a' somehow contains the catch block too
			System.out.println(e);
		}
		System.out.println("xx");
		String a = "";
		System.out.println(a);
		assertEquals(1, 2);
	}
	
}
