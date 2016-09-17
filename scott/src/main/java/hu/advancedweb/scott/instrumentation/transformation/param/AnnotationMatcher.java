package hu.advancedweb.scott.instrumentation.transformation.param;

/**
 * Decides if an annotation descriptor matches for the Scott parameters
 * or the given default values.
 * 
 * @author David Csakvari
 */
class AnnotationMatcher {
	
	static boolean match(final String annotationDesc, final String propertyKey, final String[] defaultValues) {
		final String property = System.getProperty(propertyKey);
		
		final String[] params;
		if (property != null) {
			params = property.split(",");
		} else {
			params = defaultValues;
		}
		
		for (final String param : params) {
			if (param.endsWith(".*")) {
				String desc = param.substring(0, param.length() - 2);
				desc = "L" + desc.replace(".", "/");
				if (annotationDesc.startsWith(desc)) {
					return true;
				}
			} else {
				String desc = "L" + param.replace(".", "/") + ";";
				if (annotationDesc.equals(desc)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
