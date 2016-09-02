package hu.advancedweb.scott.runtime;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;

import hu.advancedweb.scott.runtime.javasource.MethodSourceLoader;
import hu.advancedweb.scott.runtime.track.LocalVariableState;
import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

public class ScottRuntime {

	Description description;
	String testSourcePath;
	String testMethodName;
	
	public ScottRuntime(Description description) {
		this.description = description;
		// current test source resolving is based on maven conventions
		testSourcePath = System.getProperty("user.dir") + "/../src/test/java/" + description.getTestClass().getCanonicalName().replace(".", File.separator) + ".java";
		testMethodName = description.getMethodName();
		LocalVariableStateRegistry.clear();
	}
	
	public String onTestFailure(Throwable throwable) throws Exception {
		final ScottReport scottReport = new ScottReport();
		fillSource(scottReport);
		fillTrackedData(scottReport);
		
		return render(throwable, scottReport);
	}
	
	private void fillSource(ScottReport methodSource) {
		new MethodSourceLoader(testSourcePath, testMethodName).loadMethodSource(methodSource);
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
	
	private String render(Throwable throwable, ScottReport scottReport) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, String> line : scottReport.getSourceLines().entrySet()) {
			sb.append(String.format("%1$4s", line.getKey()));
			sb.append("|  ");
			sb.append(line.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}

}
