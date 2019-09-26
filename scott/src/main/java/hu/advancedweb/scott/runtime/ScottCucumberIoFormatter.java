package hu.advancedweb.scott.runtime;

import java.util.ArrayList;
import java.util.List;

import cucumber.api.Result;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestStepFinished;
import cucumber.api.formatter.Formatter;
import hu.advancedweb.scott.runtime.report.FailureRenderer;

public class ScottCucumberIoFormatter implements Formatter {
	
	List<String> results = new ArrayList<>();

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestCaseStarted.class, new EventHandler<TestCaseStarted>() {
			@Override
			public void receive(TestCaseStarted event) {
				results.clear();
			}
		});
		
		publisher.registerHandlerFor(TestStepFinished.class, new EventHandler<TestStepFinished>() {
			@Override
			public void receive(TestStepFinished event) {
				String errorString = FailureRenderer.render(null, null, event.result.is(Result.Type.FAILED) ? event.result.getError() : null);
				results.add(errorString);
				
				if (event.result.is(Result.Type.FAILED)) {
					ExceptionUtil.setExceptionMessage(event.result.getError(), concat(results, "\n"));
				}
			}
		});
	}
	
	private static String concat(Iterable<String> strings, String separator) {
	    StringBuilder sb = new StringBuilder();
	    String sep = "";
	    for(String s: strings) {
	        sb.append(sep).append(s);
	        sep = separator;
	    }
	    return sb.toString();                           
	}

}
