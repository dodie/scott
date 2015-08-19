package hu.advancedweb.scott.runtime;

import hu.advancedweb.scott.runtime.event.LocalVariableState;
import hu.advancedweb.scott.runtime.event.LocalVariableStateRegistry;
import hu.advancedweb.scott.runtime.javasource.MethodSourceLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

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
		
		TEST_SOURCE_PATH = System.getProperty("user.dir") + "/../src/test/java/" + description.getTestClass().getCanonicalName().replace(".", File.separator) + ".java";
		TEST_METHOD_NAME = description.getMethodName();

		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		MethodSource testMethodSource = new MethodSourceLoader().loadMethodSource(TEST_SOURCE_PATH, TEST_METHOD_NAME);
		
		if (testMethodSource != null) {
			Map<Integer, String> trackedValue = new HashMap<>();
			
			for (LocalVariableState event : LocalVariableStateRegistry.getLocalVariableStates()) {
				String lastValue = trackedValue.get(event.var);
				if (!event.value.equals(lastValue)) {
					testMethodSource.commentLine(event.lineNumber, LocalVariableStateRegistry.getLocalVariableName(event.var, event.lineNumber) + "=" + event.value);
					trackedValue.put(event.var, event.value);
				}
			}
			renderFailure(failure, testMethodSource);
		} else {
			renderFailure(failure);
		}
		
		super.testFailure(failure);
	}
	
	private void renderFailure(Failure failure, MethodSource testMethodSource) {
		System.out.println(failure.getTestHeader() + " FAILED!");
		System.out.println(renderTestMethodSource(testMethodSource));
	}
	
	private void renderFailure(Failure failure) {
		System.out.println(failure.getTestHeader() + " FAILED! (no source found)");
	}

	public String renderTestMethodSource(MethodSource testMethodSource) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, String> line : testMethodSource.getSourceLines().entrySet()) {
			sb.append(String.format("%1$4s", line.getKey()));
			sb.append("|  ");
			sb.append(line.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}

}
