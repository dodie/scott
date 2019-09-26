package hu.advancedweb.scott.instrumentation;

import java.util.Arrays;
import java.util.List;

import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class ScottConfigurer {
	
	private ScottConfigurer() {
		// Utility class, use static methods instead of instantiating this class.
	}
	
	public static Configuration getConfiguration() {
		return new Configuration.Builder()
				.setTrackerClass("hu.advancedweb.scott.runtime.track.StateRegistry")
				.setIncludeLambdasOnlyWhenOtherInstrumentationIsInPlace(true)
				.setIncludeByAnnotation(getPropertyConfig("scott.track.method_annotation",
						new String[] { "org.junit.Test", "org.junit.jupiter.api.Test", "org.junit.jupiter.api.TestFactory",
								"cucumber.api.java" }))
				.setInjectJUnit4RuleWhenAnnotationFound(getPropertyConfig(
						"scott.inject_junit4_rule.method_annotation", new String[] { "org.junit.Test" }))
				.setInjectJUnit5ExtensionWhenAnnotationFound(getPropertyConfig(
						"scott.inject_junit5_extension.method_annotation",
						new String[] { "org.junit.jupiter.api.Test", "org.junit.jupiter.api.TestFactory" }))
				.setTrackReturn(false)
				.setTrackUnhandledException(false)
				.setVerboseLogging("true".equalsIgnoreCase(System.getenv("scottDebug")))
				.build();
	}

	private static List<String> getPropertyConfig(String propertyKey, final String[] defaultValues) {
		final String property = System.getProperty(propertyKey);

		final String[] params;
		if (property != null) {
			params = property.split(",");
		} else {
			params = defaultValues;
		}

		/*
		 * Support backward compatibility.
		 * Previously the packages are had to be specified with wildcards ad the end. (E.g. 'hu.awm.*'.)
		 * This is not the case anymore, so if a config element ends with '.*', we simply discard these two characters.
		 */
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			if (param.endsWith(".*")) {
				params[i] = param.substring(0, param.length() - 2);
			}
		}

		return Arrays.asList(params);
	}

}
