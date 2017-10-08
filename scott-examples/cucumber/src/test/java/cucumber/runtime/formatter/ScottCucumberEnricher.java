package cucumber.runtime.formatter;

import gherkin.formatter.model.Result;
import hu.advancedweb.scott.runtime.report.FailureRenderer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ScottCucumberEnricher {

    private List<String> informationList;

    public void scenario() {
        informationList = new ArrayList<>();
    }

    public Result result(Result result) {
        String errorMessage = result.getErrorMessage();
        if (result.getStatus().equals(Result.FAILED)) {
            StringBuilder builder = new StringBuilder();
            for (String dummy : informationList) {
                builder.append(dummy);
            }
            builder.append(getErrorMessage(result.getError()));
            errorMessage = builder.toString();
        } else {
            String dummyFailure = FailureRenderer.render(null, null);
            informationList.add(dummyFailure);
        }
        return new Result(result.getStatus(), result.getDuration(), errorMessage);
    }

    private String getErrorMessage(Throwable error) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        error.printStackTrace(printWriter);
        String msg = stringWriter.getBuffer().toString();
        msg = msg.substring(msg.indexOf("\n"));
        return msg;
    }
}
