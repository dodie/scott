package hu.advancedweb.scott.runtime.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.advancedweb.scott.runtime.report.javasource.MethodSource;
import hu.advancedweb.scott.runtime.track.StateData;
import hu.advancedweb.scott.runtime.track.StateRegistry;

/**
 * Renders the pretty-printed report optimized for terminals.
 * 
 * @author David Csakvari
 */
public class FailureRenderer {

	public static String render(String testClassName, String testMethodName) {
		return render(testClassName, testMethodName, null);
	}

	public static String render(String testClassName, String testMethodName, Throwable throwable) {
		MethodSource methodSource = getTestMethodSource(testClassName, testMethodName);

		final ScottReport scottReport = new ScottReport();

		if (methodSource != null) {
			fillSource(scottReport, methodSource);
		}

		fillTrackedData(scottReport);

		if (throwable != null) {
			fillException(scottReport, methodSource, throwable);
		}

		return renderPlain(scottReport);
	}
	
	private static MethodSource getTestMethodSource(String testClassName, String testMethodName) {
		try {
			return new MethodSource(testClassName, testMethodName);
		} catch (Exception e) {
			try {
				// As a fallback, look for the currently tracked method, and try to take its source.
				testClassName = StateRegistry.getTestClassType().replace("/", ".");
				testMethodName = StateRegistry.getTestMethodName();
				return new MethodSource(testClassName, testMethodName);
			} catch (Exception e2) {
				// Ignore, we simply don't fill the test source for the report.
				// It's better than crashing the test run.
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
		
		for (StateData event : StateRegistry.getLocalVariableStates()) {
			String lastValue = trackedValue.get(event.key);
			if (!event.value.equals(lastValue)) {
				if (event.lineNumber == 0) {
					scottReport.addInitialSnapshot(getInitLine(event), StateRegistry.getLocalVariableName(event.key, event.lineNumber), event.value);
				} else {
					scottReport.addSnapshot(event.lineNumber, StateRegistry.getLocalVariableName(event.key, event.lineNumber), event.value);
				}
				trackedValue.put(event.key, event.value);
			}
		}
		
		trackedValue = new HashMap<>();
		
		for (StateData event : StateRegistry.getFieldStates()) {
			String lastValue = trackedValue.get(event.key);
			if (!event.value.equals(lastValue)) {
				if (event.lineNumber == 0) {
					scottReport.addInitialSnapshot(0, event.key, event.value);
				} else {
					scottReport.addSnapshot(event.lineNumber, event.key, event.value);
				}
			}
			trackedValue.put(event.key, event.value);
		}
	}
	
	private static int getInitLine(StateData event) {
		final String methodScope = event.key.substring(0, event.key.indexOf("\\"));
		
		if (methodScope.startsWith("lambda$")) {
			return StateRegistry.getMethodStartLine().get(methodScope);
		} else {
			return 0;
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
		boolean firstLineWithBraketAppended = false;
		boolean initialReportAppended = false;
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		
		int lastLineNumber = scottReport.getSourceLines().lastKey();
		for (Map.Entry<Integer, String> line : scottReport.getSourceLines().entrySet()) {
			int lineNumber = line.getKey();
			String lineText = line.getValue().replaceAll("\t", "    ");
			boolean initialAdded = false;
			
			if (firstLineWithBraketAppended && initialReportAppended == false) {
				initialReportAppended = true;
				if (!scottReport.getInitialSnapshots(0).isEmpty()) {
					String blankLine = lineText.replaceFirst("[^\\s].*$", "");
					for (Snapshot snapshot : scottReport.getInitialSnapshots(0)) {
						sb.append("    ");
						sb.append("|  ");
						sb.append(blankLine);
						sb.append("//    => ");
						sb.append(snapshot.name + "=" + snapshot.value.trim());
						sb.append("\n");
						initialAdded = true;
					}
				}
			}
			
			if (!scottReport.getInitialSnapshots(lineNumber - 1).isEmpty()) {
				String blankLine = lineText.replaceFirst("[^\\s].*$", "");
				for (Snapshot snapshot : scottReport.getInitialSnapshots(lineNumber - 1)) {
					sb.append("    ");
					sb.append("|  ");
					sb.append(blankLine);
					sb.append("//    => ");
					sb.append(snapshot.name + "=" + snapshot.value.trim());
					sb.append("\n");
					initialAdded = true;
				}
			}
			
			if (lineNumber == lastLineNumber) {
				if (!scottReport.getInitialSnapshots(lineNumber).isEmpty()) {
					String blankLine = lineText.replaceFirst("[^\\s].*$", "");
					for (Snapshot snapshot : scottReport.getInitialSnapshots(lineNumber)) {
						sb.append("    ");
						sb.append("|  ");
						sb.append(blankLine);
						sb.append("//    => ");
						sb.append(snapshot.name + "=" + snapshot.value.trim());
						sb.append("\n");
						initialAdded = true;
					}
				}
			}
			
			if (initialAdded) {
				String blankLine = lineText.replaceFirst("[^\\s].*$", "");
				sb.append("    ");
				sb.append("|  ");
				sb.append(blankLine);
				sb.append("\n");
			}
			
			sb.append(String.format("%1$4s", lineNumber));
			if (scottReport.getExceptionLineNumber() == lineNumber) {
				sb.append("|* ");
			} else {
				sb.append("|  ");
			}
			
			sb.append(lineText);
			
			boolean isFirstCommentInThisLine = true;

			int size = 3;
			for (Map.Entry<String, List<Snapshot>> entry : scottReport.getVariableMapSnapshot(lineNumber).entrySet()){
				String variableSnapshotText = entry.getKey();

				if (!entry.getValue().isEmpty()) {
					variableSnapshotText += "=";
					int all = entry.getValue().size();
					int lowerLimit = size;
					int upperLimit = all - size;
					int lastItem = all - 1;
					boolean hide = all > (size*2);
					int counter = 0;
					for (Snapshot snapshot : entry.getValue()) {
						if (hide) {
							if(counter>lowerLimit && counter<upperLimit) {
								counter++;
								continue;
							} else if (counter==lowerLimit) {
								variableSnapshotText += "...";
								counter++;
								continue;
							}
						}
						if (counter>0) {
							variableSnapshotText += ";";
						}

						if (counter>1 && counter==lastItem) {
							variableSnapshotText += "["+snapshot.value.trim()+"]";
						} else {
							variableSnapshotText += snapshot.value.trim();
						}
						counter++;
					}

					if (hide) {
						variableSnapshotText += " =>"+all;
					}
				}
				renderComment(sb, lineText, variableSnapshotText, isFirstCommentInThisLine);
				isFirstCommentInThisLine = false;

			}

			if (scottReport.getExceptionLineNumber() == lineNumber) {
				String[] exceptionMessageLines = getExceptionComment(scottReport);
				
				for (String comment : exceptionMessageLines) {
					renderComment(sb, lineText, comment, isFirstCommentInThisLine);
					isFirstCommentInThisLine = false;
				}
			}
			sb.append("\n");
			
			firstLineWithBraketAppended = firstLineWithBraketAppended || lineText.contains("{");
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
