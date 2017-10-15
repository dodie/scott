package hu.advancedweb.scott.runtime;

import cucumber.runtime.formatter.ColorAware;
import gherkin.formatter.PrettyFormatter;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;

public class ScottCucumberPrettyFormatter extends PrettyFormatter implements ColorAware {

	private final ScottCucumberEnricher enricher;

	public ScottCucumberPrettyFormatter(Appendable out) {
		super(out, false, true);
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
