package hu.advancedweb.scott.runtime;

import java.lang.reflect.Field;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * An alternative way to produce the detailed failure reports is to register this JUnit listener.
 * 
 * @see ScottReportingRule
 * @author David Csakvari
 */
public class ScottRunListener extends RunListener {
	
	private Description description;
	
	@Override
	public void testStarted(Description description) throws Exception {
		this.description = description;
		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		String scottReport = FailureRenderer.render(description, failure.getException());
		setExceptionMessage(failure.getException(), scottReport);
		super.testFailure(failure);
	}
	
	private void setExceptionMessage(Object object, Object fieldValue) {
	    final String fieldName = "detailMessage";
		Class<?> clazz = object.getClass();
	    while (clazz != null) {
	        try {
	            Field field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            field.set(object, fieldValue);
	            return;
	        } catch (NoSuchFieldException e) {
	            clazz = clazz.getSuperclass();
	        } catch (Exception e) {
	            throw new IllegalStateException(e);
	        }
	    }
	}
}
