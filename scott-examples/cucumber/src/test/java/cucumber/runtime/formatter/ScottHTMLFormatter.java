package cucumber.runtime.formatter;

import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;

import java.net.URL;

public class ScottHTMLFormatter extends HTMLFormatter {

	private final ScottCucumberEnricher enricher;

	public ScottHTMLFormatter(URL htmlReportDir) {
		super(htmlReportDir);
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
