package hu.advancedweb.example;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Example configuration to include Scott report in Cucumber's output.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = { 
				// Just add ScottCucumberIoFormatter as your first Plugin.
				"hu.advancedweb.scott.runtime.ScottCucumberIoFormatter",
				
				// Then specify the rest as usual.
				"pretty",
				"html:target/cucumber"
				},
        features = {"src/test/resources/feature/"})
public class FeatureTest {}
