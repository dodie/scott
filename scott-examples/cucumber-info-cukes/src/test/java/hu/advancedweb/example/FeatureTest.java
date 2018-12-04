package hu.advancedweb.example;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * To include Scott report in Cucumber's output you can use the formatters used in this example.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = { 
				// Instead of "json:target/cucumber.json" use:
				"hu.advancedweb.scott.runtime.ScottCucumberJSONFormatter:target/cucumber.json",
				
				// Instead of "pretty" use:
				"hu.advancedweb.scott.runtime.ScottCucumberPrettyFormatter",

				// Instead of "html:target/cucumber" use:
				"hu.advancedweb.scott.runtime.ScottCucumberHTMLFormatter:target/cucumber",
				},
        features = {"src/test/resources/feature/"})
public class FeatureTest {}
