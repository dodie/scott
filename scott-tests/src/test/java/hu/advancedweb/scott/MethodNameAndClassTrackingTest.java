package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.objectweb.asm.Type;

import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

public class MethodNameAndClassTrackingTest {
	
	@Test
	public void test1() throws Exception {
		assertThat(LocalVariableStateRegistry.getTestClassType(), equalTo(Type.getInternalName(MethodNameAndClassTrackingTest.class)));
		assertThat(LocalVariableStateRegistry.getTestMethodName(), equalTo("test1"));
	}

	@Test
	public void test2() throws Exception {
		assertThat(LocalVariableStateRegistry.getTestClassType(), equalTo(Type.getInternalName(MethodNameAndClassTrackingTest.class)));
		assertThat(LocalVariableStateRegistry.getTestMethodName(), equalTo("test2"));
	}

}
