package hu.advancedweb.scott.runtime;

import cucumber.runtime.formatter.CucumberJSONFormatter;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;

public class ScottCucumberJSONFormatter extends CucumberJSONFormatter {

	private final ScottCucumberEnricher enricher;

	public ScottCucumberJSONFormatter(Appendable out) {
		super(out);
		enricher = new ScottCucumberEnricher();
	}

	@Override
	public void scenario(Scenario scenario) {
		enricher.scenario();
		super.scenario(scenario);
	}

	@Override
	public void result(Result result) {
		super.result(enricher.result(result));
	}

}
