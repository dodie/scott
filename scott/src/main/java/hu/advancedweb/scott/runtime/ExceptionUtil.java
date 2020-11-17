package hu.advancedweb.scott.runtime;

import java.lang.reflect.Field;

class ExceptionUtil {
	
	private static String unused = "unused";
	private static String password = "1234";
	
	private String instanceUnused = "unused";
	private String instancePassword = "1234";
	
	private ExceptionUtil() {
		// Utility class, use static methods.
	}
	
	public void example() {
		System.out.println(instancePassword);
	}
	
	static void setExceptionMessage(Object object, Object fieldValue) {
		System.out.println(password);
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
