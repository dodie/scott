package hu.advancedweb.scott;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FieldRecordingTest {
	
	byte b = 0;
	short s = 0;
	int i = 0;
	long l = 0L;
	float f = 0.0F;
	double d = 0.0D;
	boolean bool = false;
	char c = 'i';
	String object = "initial";

	@Test
	public void test_1_recordRead() throws Exception {
		// initial values match
		
		
	}
	
	@Test
	public void test_2_recordWrite() throws Exception {
		// initial values match
		
		// values after write match
	}
	
	@Test
	public void test_3_recordReadAndWrite() throws Exception {
		// initial values match
		
		// no change after reading
		
		// values after write
	}
	
	@Test
	public void test_4_recordSubset() throws Exception {
		// initial values contains only the accessed fields
	}

	
	// TODO: maybe: align with var recording test and do tests by type as well


}
