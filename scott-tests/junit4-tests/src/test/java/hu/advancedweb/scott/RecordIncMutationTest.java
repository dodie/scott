package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class RecordIncMutationTest {
	
	@Test
	public void primitiveInc() throws Exception {
		int i = 0;
		i++;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		long l = 0L;
		l++;
		assertThat(TestHelper.getLastRecordedStateForVariable("l"), equalTo(Long.toString(l)));
		
		float f = 0F;
		f++;
		assertThat(TestHelper.getLastRecordedStateForVariable("f"), equalTo(Float.toString(f)));
		
		double d = 0D;
		d++;
		assertThat(TestHelper.getLastRecordedStateForVariable("d"), equalTo(Double.toString(d)));
	}
	
	@Test
	public void boxedInc() throws Exception {
		Integer i = Integer.valueOf("0");
		i++;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		Long l = Long.valueOf("0");
		l++;
		assertThat(TestHelper.getLastRecordedStateForVariable("l"), equalTo(Long.toString(l)));
		
		Float f = Float.valueOf("0");
		f++;
		assertThat(TestHelper.getLastRecordedStateForVariable("f"), equalTo(Float.toString(f)));
		
		Double d = Double.valueOf("0");
		d++;
		assertThat(TestHelper.getLastRecordedStateForVariable("d"), equalTo(Double.toString(d)));
	}
	
	int a = 0;
	Integer aA = Integer.valueOf("0");
	
	@Test
	public void fieldInc() throws Exception {
		this.a++;
		this.aA++;
		
		assertThat(TestHelper.getLastRecordedStateForField("this.a"), equalTo(Integer.toString(this.a)));
		assertThat(TestHelper.getLastRecordedStateForField("this.aA"), equalTo(Integer.toString(this.aA)));
	}
	
}
