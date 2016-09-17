package cucumber.runtime.formatter;

import java.net.URL;

import gherkin.formatter.model.Result;

public class ScottHTMLFormatter extends HTMLFormatter {

	public ScottHTMLFormatter(URL htmlReportDir) {
		super(htmlReportDir);
	}

	@Override
	public void result(Result result) {
		super.result(new Result(result.getStatus(), result.getDuration(), result.getError(), null));
	}

}
