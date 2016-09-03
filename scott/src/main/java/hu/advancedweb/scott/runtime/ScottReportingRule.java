package hu.advancedweb.scott.runtime;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * To produce the detailed failure reports use this JUnit Rule.
 * 
 * @author David Csakvari
 */
public class ScottReportingRule implements TestRule {
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				/*
				 * Evaluate test and in case of a failure 
				 * throw a new error with the correct exception type
				 * that contains Scott's output and the original cause.
				 */
				try {
					base.evaluate();
				} catch (AssertionError assertionError) {
					throw new AssertionError(FailureRenderer.render(description, assertionError), assertionError);
				} catch (Error error) {
					throw new Error(FailureRenderer.render(description, error), error);
				} catch (Throwable throwable) {
					throw new Throwable(FailureRenderer.render(description, throwable), throwable);
				}
			}
		};
	}
}
