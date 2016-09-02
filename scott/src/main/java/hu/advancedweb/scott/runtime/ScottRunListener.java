package hu.advancedweb.scott.runtime;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Scott's JUnit listener that produces the detailed failure reports for failed tests.
 * 
 * @author David Csakvari
 */
public class ScottRunListener extends RunListener {
	
	private ScottRuntime scottRuntime;

	@Override
	public void testStarted(Description description) throws Exception {
		scottRuntime = new ScottRuntime(description);
		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		System.out.println(failure.getTestHeader() + " FAILED!");
		System.out.println(scottRuntime.onTestFailure(failure.getException()));
		super.testFailure(failure);
	}
}
