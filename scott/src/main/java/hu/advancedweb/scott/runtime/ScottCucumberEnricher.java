package hu.advancedweb.scott.runtime;

import gherkin.formatter.model.Result;
import hu.advancedweb.scott.runtime.report.FailureRenderer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

class ScottCucumberEnricher {

	private List<String> informationList;

	public void scenario() {
		informationList = new ArrayList<>();
	}

	public Result result(Result result) {
		String errorMessage = result.getErrorMessage();
		if (result.getStatus().equals(Result.FAILED)) {
			String errorString = FailureRenderer.render(null, null, result.getError());
			StringBuilder builder = new StringBuilder();
			for (String info : informationList) {
				builder.append(info);
			}
			builder.append(errorString);
			builder.append(getErrorMessage(result.getError()));
			errorMessage = builder.toString();
		} else {
			String errorString = FailureRenderer.render(null, null, null);
			informationList.add(errorString);
		}
		return new Result(result.getStatus(), result.getDuration(), errorMessage);
	}

	private String getErrorMessage(Throwable error) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		error.printStackTrace(printWriter);
		String msg = stringWriter.getBuffer().toString();
		msg = msg.substring(msg.indexOf('\n'));
		return msg;
	}

}
