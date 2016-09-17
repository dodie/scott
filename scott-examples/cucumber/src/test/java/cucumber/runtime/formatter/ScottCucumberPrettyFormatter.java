package cucumber.runtime.formatter;

import gherkin.formatter.model.Result;

public class ScottCucumberPrettyFormatter extends CucumberPrettyFormatter {

	public ScottCucumberPrettyFormatter(Appendable out) {
		super(out);
	}

	@Override
	public void result(Result result) {
		super.result(new Result(result.getStatus(), result.getDuration(), result.getError(), null));
	}

}
