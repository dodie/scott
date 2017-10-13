package hu.advancedweb.scott.runtime;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import hu.advancedweb.scott.runtime.report.FailureRenderer;

public class ScottJUnit5Extension implements TestExecutionExceptionHandler {

	@Override
	public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
		String testClassName = context.getTestClass().isPresent() ? context.getTestClass().get().getTypeName() : null;
		String testMethodName = context.getTestMethod().isPresent() ? context.getTestMethod().get().getName() : null;

		if (throwable instanceof AssertionError) {
			throw new AssertionError(FailureRenderer.render(testClassName, testMethodName, throwable), throwable);
		} else if (throwable instanceof Error) {
			throw new Error(FailureRenderer.render(testClassName, testMethodName, throwable), throwable);
		} else {
			throw new Throwable(FailureRenderer.render(testClassName, testMethodName, throwable), throwable);
		}
	}

}
