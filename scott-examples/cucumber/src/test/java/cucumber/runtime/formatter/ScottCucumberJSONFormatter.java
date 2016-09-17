package cucumber.runtime.formatter;

import cucumber.runtime.formatter.CucumberJSONFormatter;
import gherkin.formatter.model.Result;

public class ScottCucumberJSONFormatter extends CucumberJSONFormatter {

	public ScottCucumberJSONFormatter(Appendable out) {
		super(out);
	}
	
	@Override
	public void result(Result result) {
		super.result(new Result(result.getStatus(), result.getDuration(), result.getError(), null));
	}
	
}
