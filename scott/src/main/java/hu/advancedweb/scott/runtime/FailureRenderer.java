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
		Integer lineNumber = scottReport.getBeginLineNumber();
		for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
			if (description.getClassName().equals(stackTraceElement.getClassName())) {
				lineNumber = stackTraceElement.getLineNumber();
				break;
			}
		}
		
		scottReport.setException(lineNumber, throwable.getClass().getSimpleName(), throwable.getMessage());
	}
	
	private static String renderPlain(ScottReport scottReport) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (Map.Entry<Integer, String> line : scottReport.getSourceLines().entrySet()) {
			int lineNumber = line.getKey();
			String lineText = line.getValue().replaceAll("\t", "    ");
			
			sb.append(String.format("%1$4s", lineNumber));
			if (scottReport.getExceptionLineNumber() == lineNumber) {
				sb.append("|* ");
			} else {
				sb.append("|  ");
			}
			
			sb.append(lineText);
			
			boolean isFirstCommentInThisLine = true;
			for (VariableSnapshot variableSnapshot : scottReport.getVariableSnapshots(lineNumber)) {
				if (!isFirstCommentInThisLine) {
					addBlankLine(sb, lineText);
				}
				
				sb.append("  // ");
				sb.append(variableSnapshot.name);
				sb.append("=");
				sb.append(variableSnapshot.value);
				isFirstCommentInThisLine = false;
			}
			
			if (scottReport.getExceptionLineNumber() == lineNumber) {
				if (!isFirstCommentInThisLine) {
					addBlankLine(sb, lineText);
				}
				
				sb.append("  // ");
				
				if (scottReport.getExceptionMessage() != null) {
					sb.append(scottReport.getExceptionMessage());
				} else {
					sb.append(scottReport.getExceptionClassName());
				}
			}
			
			sb.append("\n");
		}
		return sb.toString();
	}

	private static void addBlankLine(StringBuilder sb, String lineText) {
		sb.append("\n");
		sb.append("    ");
		sb.append("|  ");
		sb.append(lineText.replaceAll(".", " "));
	}

}
