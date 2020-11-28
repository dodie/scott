package hu.advancedweb.example;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedTest {
	
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
			{ 0, 0, 0 }, 
			{ 1, 1, 2 }, 
			{ 2, 1, 3 }, 
			{ 2, 2, 4 }
		});
	}

	private final int a;
	private final int b;
	private final int expectedSum;

	public ParameterizedTest(int a, int b, int expectedSum) {
		this.a = a;
		this.b = b;
		this.expectedSum = expectedSum;
	}

	@Test
    public void testAddition() {
    	int sum = FaultyAdder.add(a, b);
    	assertThat(sum, equalTo(expectedSum));
    }
	
}