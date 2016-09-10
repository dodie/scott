package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

import hu.advancedweb.scott.instrumentation.transformation.ConstructorTransformerMethodVisitor;

/**
 *  For now rule injection works only in this test.
 *	@see ConstructorTransformerMethodVisitor
 */
public class RuleInjectionTest {
	
	@Test
	public void testName() throws Exception {
		Field field = this.getClass().getDeclaredField("scottReportingRule");
		
		Annotation[] annotations = field.getDeclaredAnnotations();
		Object value = field.get(this);
		
		assertThat(value, notNullValue());
		assertTrue(annotations.length != 0);
	}
	
}
