package hu.advancedweb.scott.runtime;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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
	
	/**
	 * Sometimes testFailure is called multiple times for a single test,
	 * so we has to track the already augmented Exception objects.
	 */
	private Set<Object> augmentedExceptions = new HashSet<>();
	
	@Override
	public void testStarted(Description description) throws Exception {
		this.description = description;
		augmentedExceptions.clear();
		
		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		String scottReport = FailureRenderer.render(description, failure.getException());
		setExceptionMessage(failure.getException(), scottReport);
		super.testFailure(failure);
	}
	
	private void setExceptionMessage(Object object, Object fieldValue) {
		if (!augmentedExceptions.contains(object)) {
			augmentedExceptions.add(object);
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
}
