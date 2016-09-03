package hu.advancedweb.scott.runtime;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * An alternative way to produce the detailed failure reports is to register this JUnit listener.
 * 
 * @see ScottReportingRule
 * @author David Csakvari
 */
public class ScottRunListener extends RunListener {
	
	private Description description;
	
	@Override
	public void testStarted(Description description) throws Exception {
		this.description = description;
		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		System.out.println(failure.getTestHeader() + " FAILED!");
		System.out.println(FailureRenderer.render(description, failure.getException()));
		super.testFailure(failure);
	}
}
