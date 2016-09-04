package hu.advancedweb.scott;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VariableStateLeakageTest {
	
	@Test
	public void step_1_run_a_test_and_check_that_scott_recorded_something() throws Exception {
		int i = 5;
		i = i + 2;
		assertTrue(!LocalVariableStateRegistry.getLocalVariableStates().isEmpty());
	}
	
	@Test
	public void step_2_check_that_the_recorded_variable_states_are_cleared_for_the_next_test() throws Exception {
		assertTrue(LocalVariableStateRegistry.getLocalVariableStates().isEmpty());
	}

}
