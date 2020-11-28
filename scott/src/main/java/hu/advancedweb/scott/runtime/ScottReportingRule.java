package hu.advancedweb.scott.runtime;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import hu.advancedweb.scott.runtime.report.FailureRenderer;

/**
 * To produce the detailed failure reports use this JUnit Rule.
 * 
 * @author David Csakvari
 */
public class ScottReportingRule implements TestRule {

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				String testClassName = description.getTestClass() != null ? description.getTestClass().getTypeName() : null;
				String testMethodName = description.getMethodName();
				
				/*
				 * Evaluate test and in case of a failure 
				 * throw a new error with the correct exception type
				 * that contains Scott's output and the original cause.
				 */
				try {
					base.evaluate();
				} catch (AssertionError assertionError) {
					throw new AssertionError(FailureRenderer.render(testClassName, testMethodName, assertionError), assertionError);
				} catch (Error error) {
					throw new Error(FailureRenderer.render(testClassName, testMethodName, error), error);
				} catch (Throwable throwable) {
					throw new Throwable(FailureRenderer.render(testClassName, testMethodName, throwable), throwable);
				}
			}
		};
	}
	
}
