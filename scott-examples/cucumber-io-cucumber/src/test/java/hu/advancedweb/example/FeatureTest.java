package hu.advancedweb.example;

import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

/**
 * Example configuration to include Scott report in Cucumber's output.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = { 
				"hu.advancedweb.scott.runtime.ScottCucumberIoFormatter:target/mycuc.html"
				},
        features = {"src/test/resources/feature/"})
public class FeatureTest {}
