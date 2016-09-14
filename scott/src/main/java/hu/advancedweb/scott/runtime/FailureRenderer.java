package hu.advancedweb.scott.runtime;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;

import hu.advancedweb.scott.runtime.javasource.MethodSource;
import hu.advancedweb.scott.runtime.track.LocalVariableState;
import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

/**
 * Renders the pretty-printed report optimized for terminals.
 * 
 * @author David Csakvari
 */
public class FailureRenderer {

	public static String render(Description description, Throwable throwable) {
		final ScottReport scottReport = new ScottReport();
		MethodSource methodSource = getTestMethodSource(description);
		
		fillSource(scottReport, methodSource);
		fillTrackedData(scottReport);
		fillException(scottReport, methodSource, throwable);

		return renderPlain(scottReport);
	}
	
	private static MethodSource getTestMethodSource(Description description) {
		try {
			String testClassName = description.getTestClass().getCanonicalName();
			String testSourcePath = getSourcePath(testClassName.replace(".", File.separator));
			String testMethodName = description.getMethodName();
			return new MethodSource(testSourcePath, testClassName, testMethodName);
		} catch (Exception e) {
			try {
				// As a fallback, look for the currently tracked method, and try to take its source.
				String testClassName = LocalVariableStateRegistry.getTestClassType().replace("/", ".");
				String testSourcePath = getSourcePath(LocalVariableStateRegistry.getTestClassType());
				String testMethodName = LocalVariableStateRegistry.getTestMethodName();
				return new MethodSource(testSourcePath, testClassName, testMethodName);
			} catch (Exception e2) {
				// Ignore, we simply don't fill the test source for the report.
				// It's better than crashing the test run.
				return null;
			}
		}
	}

	private static String getSourcePath(String type) {
		// TODO: current test source resolving is based on maven conventions. See issue #6.
		return System.getProperty("user.dir") + "/src/test/java/" + type + ".java";
	}
	
	private static void fillSource(ScottReport scottReport, MethodSource methodSource) {
		if (methodSource != null) {
			scottReport.setBeginLine(methodSource.getBeginLine());
			for (String line : methodSource.getReportLines()) {
				scottReport.addLine(line);
			}
		}
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
	
	private static void fillException(ScottReport scottReport, MethodSource methodSource, Throwable throwable) {
		Integer lineNumber = scottReport.getBeginLineNumber();
		for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
			if (methodSource.getClassName().equals(stackTraceElement.getClassName()) &&
					methodSource.getMethodName().equals(stackTraceElement.getMethodName())) {
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
