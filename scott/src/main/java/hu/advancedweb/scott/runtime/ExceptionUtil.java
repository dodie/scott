package hu.advancedweb.scott.runtime;

import java.lang.reflect.Field;

class ExceptionUtil {
	
	private ExceptionUtil() {
		// Utility class, use static methods.
	}
	
	static void setExceptionMessage(Object object, Object fieldValue) {
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
