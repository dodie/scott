package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;

public class RuleInjectionTest {
	
	@Test
	public void ruleInjected() throws Exception {
		Field field = this.getClass().getDeclaredField("scottReportingRule");
		
		Annotation[] annotations = field.getDeclaredAnnotations();
		Object value = field.get(this);
		
		assertThat(value, notNullValue());
		assertTrue(annotations.length != 0);
	}
	
}
