package hu.advancedweb.example;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * The Cucumber Runner saves the exception message in its
 * Result object before Scott could enhance it.
 * The Exception object is available for the Formatters, but by default
 * they use the saved text instead.
 * 
 * As a workaround, we are using custom formatters that simply reconstruct the
 * Result with the up to date Exception object.
 * 
 * @author David Csakvari
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = { 
				// Instead of "json:target/cucumber.json" use:
				"hu.advancedweb.scott.runtime.ScottCucumberJSONFormatter:target/cucumber.json",
				
				// Instead of "pretty" use:
				"hu.advancedweb.scott.runtime.ScottCucumberPrettyFormatter",

				// Instead of "html:target/cucumber" use:
				"hu.advancedweb.scott.runtime.ScottHTMLFormatter:target/cucumber",
				},
        features = {"src/test/resources/feature/"})
public class FeatureTest {}
