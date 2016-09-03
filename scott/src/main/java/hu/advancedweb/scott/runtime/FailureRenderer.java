package hu.advancedweb.scott.runtime;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;

import hu.advancedweb.scott.runtime.javasource.MethodSourceLoader;
import hu.advancedweb.scott.runtime.track.LocalVariableState;
import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

/**
 * Renders the pretty-printed report optimized for terminals.
 * 
 * @author David Csakvari
 */
public class FailureRenderer {

	public static String render(Description description, Throwable throwable) {
		// current test source resolving is based on maven conventions
		String testSourcePath = System.getProperty("user.dir") + "/../src/test/java/" + description.getTestClass().getCanonicalName().replace(".", File.separator) + ".java";
		String testMethodName = description.getMethodName();
		
		final ScottReport scottReport = new ScottReport();
		fillSource(scottReport, testSourcePath, testMethodName);
		fillTrackedData(scottReport);
		fillException(scottReport, description, throwable);

		return renderPlain(scottReport);
	}
	
	private static void fillSource(ScottReport scottReport, String testSourcePath, String testMethodName) {
		new MethodSourceLoader(testSourcePath, testMethodName).loadMethodSource(scottReport);
	}
	
	private static void fillTrackedData(ScottReport scottReport) {
		Map<Integer, String> trackedValue = new HashMap<>();
		
		for (LocalVariableState event : LocalVariableStateRegistry.getLocalVariableStates()) {
			String lastValue = trackedValue.get(event.var);
			if (!event.value.equals(lastValue)) {
				scottReport.addVariableSnapshot(event.lineNumber, LocalVariableStateRegistry.getLocalVariableName(event.var, event.lineNumber), event.value);
				trackedValue.put(event.var, event.value);
			}
		}
	}
	
	private static void fillException(ScottReport scottReport, Description description, Throwable throwable) {
		Integer lineNumber = null;
		for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
			if (description.getClassName().equals(stackTraceElement.getClassName())) {
				lineNumber = stackTraceElement.getLineNumber();
				break;
			}
		}
		if (lineNumber == null) {
			// TODO: rakjuk az elejére, vagy a végére okosan.
			lineNumber = 1;
		}
		
		scottReport.setException(lineNumber, throwable.getMessage());
	}
	
	private static String renderPlain(ScottReport scottReport) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (Map.Entry<Integer, String> line : scottReport.getSourceLines().entrySet()) {
			int lineNumber = line.getKey();
			sb.append(String.format("%1$4s", lineNumber));
			sb.append("|  ");
			sb.append(line.getValue());
			
			for (VariableSnapshot variableSnapshot : scottReport.getVariableSnapshots(lineNumber)) {
				sb.append("  // ");
				sb.append(variableSnapshot.name);
				sb.append("=");
				sb.append(variableSnapshot.value);
			}
			
			if (scottReport.getExceptionLineNumber() == lineNumber) {
				sb.append("  // ");
				sb.append(scottReport.getExceptionMessage());
			}
			
			sb.append("\n");
		}
		return sb.toString();
	}

}
