package hu.advancedweb.scott.runtime;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import hu.advancedweb.scott.runtime.javasource.MethodSourceLoader;
import hu.advancedweb.scott.runtime.track.LocalVariableState;
import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

/**
 * Scott's JUnit listener that produces the detailed failure reports for failed tests.
 * 
 * @author David Csakvari
 */
public class ScottRunListener extends RunListener {
	
	static String TEST_SOURCE_PATH;
	static String TEST_METHOD_NAME;

	@Override
	public void testStarted(Description description) throws Exception {
		LocalVariableStateRegistry.clear();
		
		// current test source resolving is based on maven conventions
		TEST_SOURCE_PATH = System.getProperty("user.dir") + "/../src/test/java/" + description.getTestClass().getCanonicalName().replace(".", File.separator) + ".java";
		TEST_METHOD_NAME = description.getMethodName();

		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		final ScottReport methodSource = new ScottReport();

		fillSource(methodSource);
		fillTrackedData(methodSource);
		
		render(failure, methodSource);

		super.testFailure(failure);
	}
	
	private void fillSource(ScottReport methodSource) {
		new MethodSourceLoader(TEST_SOURCE_PATH, TEST_METHOD_NAME).loadMethodSource(methodSource);
	}
	
	private void fillTrackedData(ScottReport methodSource) {
		Map<Integer, String> trackedValue = new HashMap<>();
		
		for (LocalVariableState event : LocalVariableStateRegistry.getLocalVariableStates()) {
			String lastValue = trackedValue.get(event.var);
			if (!event.value.equals(lastValue)) {
				methodSource.commentLine(event.lineNumber, LocalVariableStateRegistry.getLocalVariableName(event.var, event.lineNumber) + "=" + event.value);
				trackedValue.put(event.var, event.value);
			}
		}
	}
	
	private void render(Failure failure, ScottReport scottReport) {
		System.out.println(failure.getTestHeader() + " FAILED!");

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, String> line : scottReport.getSourceLines().entrySet()) {
			sb.append(String.format("%1$4s", line.getKey()));
			sb.append("|  ");
			sb.append(line.getValue());
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
	}

}
