package hu.advancedweb.scott.instrumentation.transformation.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ConfigTest {

	private int anyMethodLoc = 10;
	
	private String anyTrackerClass = "";

	@Test
	public void instrumentation_should_be_allowed_by_default() {
		Configuration config = new Configuration.Builder().setTrackerClass(anyTrackerClass).build();
		assertTrue(config.isClassInstrumentationAllowed("hu.awm.Example", new ArrayList<String>()));
		assertTrue(config.isMethodInstrumentationAllowed("test", anyMethodLoc, new ArrayList<String>(), new ArrayList<String>()));
	}

	@Test
	public void only_included_classes_should_be_instrumented() {
		Configuration configWithInclusion = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setInclude(Collections.singletonList("hu.awm"))
				.build();
		assertFalse(configWithInclusion.isClassInstrumentationAllowed("hu.Example", new ArrayList<String>()));
		assertTrue(configWithInclusion.isClassInstrumentationAllowed("hu.awm.Example", new ArrayList<String>()));
		assertTrue(configWithInclusion.isClassInstrumentationAllowed("hu.awm.module.Example", new ArrayList<String>()));

		Configuration configWithExclusion = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setExclude(Collections.singletonList("hu.awm.module"))
				.build();
		assertTrue(configWithExclusion.isClassInstrumentationAllowed("hu.awm.Example", new ArrayList<String>()));
		assertFalse(configWithExclusion.isClassInstrumentationAllowed("hu.awm.module.Example", new ArrayList<String>()));

		Configuration configWithInclusionAndExclusion = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setInclude(Collections.singletonList("hu.awm"))
				.setExclude(Collections.singletonList("hu.awm.module"))
				.build();
		assertTrue(configWithInclusionAndExclusion.isClassInstrumentationAllowed("hu.awm.Example", new ArrayList<String>()));
		assertFalse(configWithInclusionAndExclusion.isClassInstrumentationAllowed("hu.awm.module.Example", new ArrayList<String>()));

		Configuration configWithInclusionAndExcludeAnnotation = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setInclude(Arrays.asList("hu.awm"))
				.setExcludeByAnnotation(Arrays.asList("hu.awm.Exclude"))
				.build();
		assertFalse(configWithInclusionAndExcludeAnnotation.isClassInstrumentationAllowed("hu.Example", new ArrayList<String>()));
		assertTrue(configWithInclusionAndExcludeAnnotation.isClassInstrumentationAllowed("hu.awm.Example", new ArrayList<String>()));
		assertFalse(configWithInclusionAndExcludeAnnotation.isClassInstrumentationAllowed("hu.awm.Example", Collections.singletonList("hu.awm.Exclude")));
	}

	@Test
	public void only_included_methods_should_be_instrumented() {
		Configuration config = new Configuration.Builder().setTrackerClass(anyTrackerClass).build();
		assertTrue(config.isMethodInstrumentationAllowed("test", anyMethodLoc, new ArrayList<String>(), new ArrayList<String>()));

		Configuration configWithNameExclusion = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setExcludeMethodsByName(Collections.singletonList("toString"))
				.build();
		assertTrue(configWithNameExclusion.isMethodInstrumentationAllowed("test", anyMethodLoc, new ArrayList<String>(), new ArrayList<String>()));
		assertFalse(configWithNameExclusion.isMethodInstrumentationAllowed("toString", anyMethodLoc, new ArrayList<String>(), new ArrayList<String>()));

		Configuration configWithIncludeAnnotation = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setIncludeByAnnotation(Arrays.asList("hu.awm.Include", "hu.awm.Include2"))
				.build();
		assertFalse(configWithIncludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, new ArrayList<String>(), new ArrayList<String>()));
		assertTrue(configWithIncludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, Collections.singletonList("hu.awm.Include"), new ArrayList<String>()));
		assertTrue(configWithIncludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, new ArrayList<String>(), Arrays.asList("hu.awm.Include")));

		Configuration configWithIncludeAndExcludeAnnotation = new Configuration.Builder()
				.setTrackerClass(anyTrackerClass)
				.setIncludeByAnnotation(Collections.singletonList("hu.awm.Include"))
				.setExcludeByAnnotation(Collections.singletonList("hu.awm.Exclude"))
				.build();
		assertFalse(configWithIncludeAndExcludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, new ArrayList<String>(), new ArrayList<String>()));
		assertTrue(configWithIncludeAndExcludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, Collections.singletonList("hu.awm.Include"), new ArrayList<String>()));
		assertFalse(configWithIncludeAndExcludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, Collections.singletonList("hu.awm.Exclude"), Arrays.asList("hu.awm.Include")));
		assertFalse(configWithIncludeAndExcludeAnnotation.isMethodInstrumentationAllowed("test", anyMethodLoc, Arrays.asList("hu.awm.Include", "hu.awm.Exclude"), new ArrayList<String>()));
	}

}
