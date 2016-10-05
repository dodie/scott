package hu.advancedweb.scott.runtime.report;

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
		
		if (methodSource != null) {
			fillSource(scottReport, methodSource);
		}
		
		fillTrackedData(scottReport);
		fillException(scottReport, methodSource, throwable);

		return renderPlain(scottReport);
	}
	
	private static MethodSource getTestMethodSource(Description description) {
		try {
			String testClassName = description.getTestClass().getCanonicalName();
			String testSourcePath = getSourcePath(testClassName);
			String testMethodName = description.getMethodName();
			return new MethodSource(testSourcePath, testClassName, testMethodName);
		} catch (Exception e) {
			try {
				// As a fallback, look for the currently tracked method, and try to take its source.
				String testClassName = LocalVariableStateRegistry.getTestClassType().replace("/", ".");
				String testSourcePath = getSourcePath(testClassName);
				String testMethodName = LocalVariableStateRegistry.getTestMethodName();
				return new MethodSource(testSourcePath, testClassName, testMethodName);
			} catch (Exception e2) {
				// Ignore, we simply don't fill the test source for the report.
				// It's better than crashing the test run.
				return null;
			}
		}
	}
	
	private static String getSourcePath(final String fqn) {
		final String baseDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator;
		final String relativeFilePath = fqn.replace(".", File.separator) + ".java";
		return getSourcePath(baseDirectory, relativeFilePath, 2);
	}
	
	private static String getSourcePath(final String baseDirectory, final String relativeFilePath, final int level) {
		if (!new File(baseDirectory).exists() || !new File(baseDirectory).isDirectory()) {
			return null;
		} else {
			String pathInBaseDirectory = baseDirectory + relativeFilePath;
			if (new File(pathInBaseDirectory).exists()) {
				return pathInBaseDirectory;
			} else {
				if (level == 0) {
					return null;
				}
				String[] entries = new File(baseDirectory).list();
				for (String entry : entries) {
					String path = getSourcePath(baseDirectory + entry + File.separator, relativeFilePath, level - 1);
					if (path != null) {
						return path;
					}
				}
				return null;
			}
		}
	}

	private static void fillSource(ScottReport scottReport, MethodSource methodSource) {
		scottReport.setBeginLine(methodSource.getBeginLine());
		for (String line : methodSource.getReportLines()) {
			scottReport.addLine(line);
		}
	}

	private static void fillTrackedData(ScottReport scottReport) {
		Map<String, String> trackedValue = new HashMap<>();
		
		for (LocalVariableState event : LocalVariableStateRegistry.getLocalVariableStates()) {
			String lastValue = trackedValue.get(event.key);
			if (!event.value.equals(lastValue)) {
				scottReport.addVariableSnapshot(event.lineNumber, LocalVariableStateRegistry.getLocalVariableName(event.key, event.lineNumber), event.value);
				trackedValue.put(event.key, event.value);
			}
		}
	}
	
	private static void fillException(ScottReport scottReport, MethodSource methodSource, Throwable throwable) {
		Integer lineNumber = scottReport.getBeginLineNumber();
		
		if (methodSource != null) {
			for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
				if (methodSource.getClassName().equals(stackTraceElement.getClassName()) &&
						methodSource.getMethodName().equals(stackTraceElement.getMethodName())) {
					lineNumber = stackTraceElement.getLineNumber();
					break;
				}
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
				String[] variableSnapshotTextLines = getVariableSnapshotComment(variableSnapshot);
				
				for (String comment : variableSnapshotTextLines) {
					renderComment(sb, lineText, comment, isFirstCommentInThisLine);
					isFirstCommentInThisLine = false;
				}
			}
			
			if (scottReport.getExceptionLineNumber() == lineNumber) {
				String[] exceptionMessageLines = getExceptionComment(scottReport);
				
				for (String comment : exceptionMessageLines) {
					renderComment(sb, lineText, comment, isFirstCommentInThisLine);
					isFirstCommentInThisLine = false;
				}
			}
			
			sb.append("\n");
		}
		return sb.toString();
	}

	private static String[] getExceptionComment(ScottReport scottReport) {
		final String exceptionMessage;
		if (scottReport.getExceptionMessage() != null) {
			exceptionMessage = scottReport.getExceptionClassName() + ": " + scottReport.getExceptionMessage().trim();
		} else {
			exceptionMessage = scottReport.getExceptionClassName();
		}
		
		String[] exceptionMessageLines = exceptionMessage.split("\\n");
		return exceptionMessageLines;
	}

	private static String[] getVariableSnapshotComment(VariableSnapshot variableSnapshot) {
		final String variableSnapshotText;
		if (variableSnapshot.name != null) {
			variableSnapshotText = variableSnapshot.name + "=" + variableSnapshot.value.trim();
		} else {
			variableSnapshotText = variableSnapshot.value.trim();
		}
		String[] variableSnapshotTextLines = variableSnapshotText.split("\\n");
		return variableSnapshotTextLines;
	}
	
	private static void renderComment(StringBuilder sb, String lineText, String comment, boolean isFirstCommentInThisLine) {
		if (!isFirstCommentInThisLine) {
			addBlankLine(sb, lineText);
		}
		
		sb.append("  // ");
		sb.append(comment);
		isFirstCommentInThisLine = false;
	}

	private static void addBlankLine(StringBuilder sb, String lineText) {
		sb.append("\n");
		sb.append("    ");
		sb.append("|  ");
		sb.append(lineText.replaceAll(".", " "));
	}

}
