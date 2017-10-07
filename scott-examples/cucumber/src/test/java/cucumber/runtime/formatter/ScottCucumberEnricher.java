package cucumber.runtime.formatter;

import gherkin.formatter.model.Result;
import hu.advancedweb.scott.runtime.report.FailureRenderer;

import java.util.ArrayList;
import java.util.List;

public class ScottCucumberEnricher {

    private List<String> informationList;

    public void scenario() {
        informationList = new ArrayList<>();
    }

    public Result result(Result result) {
        String errorMessage = result.getErrorMessage();
        String dummyFailure = FailureRenderer.render(null, null);
        if (result.getStatus().equals(Result.FAILED)) {
            StringBuilder builder = new StringBuilder();
            for (String dummy : informationList) {
                builder.append(dummy);
            }
            builder.append(dummyFailure).append(errorMessage);
            errorMessage = builder.toString();
        } else {
            informationList.add(dummyFailure);
        }
        return new Result(result.getStatus(), result.getDuration(), errorMessage);
    }
}
