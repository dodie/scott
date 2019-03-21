package hu.advancedweb.scott.instrumentation.transformation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VariableTypeTest {

	@Test
	public void test() {
		assertEquals(VariableType.INTEGER, VariableType.getReturnTypeFromMethodDesc("()I"));
		assertEquals(VariableType.REFERENCE, VariableType.getReturnTypeFromMethodDesc("()Ljava/lang/String"));
		assertEquals(VariableType.VOID, VariableType.getReturnTypeFromMethodDesc("()V"));
		assertEquals(VariableType.DOUBLE, VariableType.getReturnTypeFromMethodDesc("(I)D"));
	}

}
