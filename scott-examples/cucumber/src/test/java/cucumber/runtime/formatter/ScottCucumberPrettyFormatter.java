package cucumber.runtime.formatter;

import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;

public class ScottCucumberPrettyFormatter extends CucumberPrettyFormatter {

	private final ScottCucumberEnricher enricher;

	public ScottCucumberPrettyFormatter(Appendable out) {
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
