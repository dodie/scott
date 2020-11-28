package hu.advancedweb.scott.runtime.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.advancedweb.scott.runtime.report.javasource.MethodSource;
import hu.advancedweb.scott.runtime.track.StateData;
import hu.advancedweb.scott.runtime.track.StateRegistry;

/**
 * Renders the pretty-printed plain text report containing the source of the
 * failing test case annotated with the state-changes in the test.
 * 
 * @author David Csakvari
 */
public class FailureRenderer {
	
	private FailureRenderer() {
		// Utility class, use static methods instead of instantiating this class.
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
			String lastValue = trackedValue.get(event.name);
			if (!event.value.equals(lastValue)) {
				if (event.lineNumber == 0) {
					scottReport.addInitialSnapshot(getInitLine(event), event.name, event.value);
				} else {
					scottReport.addSnapshot(event.lineNumber, event.name, event.value);
				}
				trackedValue.put(event.name, event.value);
			}
		}
		
		trackedValue = new HashMap<>();
		
		for (StateData event : StateRegistry.getFieldStates()) {
			String lastValue = trackedValue.get(event.name);
			if (!event.value.equals(lastValue)) {
				if (event.lineNumber == 0) {
					scottReport.addInitialSnapshot(0, event.name, event.value);
				} else {
					scottReport.addSnapshot(event.lineNumber, event.name, event.value);
				}
			}
			trackedValue.put(event.name, event.value);
		}
	}
	
	private static int getInitLine(StateData event) {
		if (event.methodName.startsWith("lambda$")) {
			return StateRegistry.getMethodStartLine().get(event.methodName);
		} else {
			return 0;
		}
	}
	
	private static void fillException(ScottReport scottReport, MethodSource methodSource, Throwable throwable) {
		int lineNumber = scottReport.getBeginLineNumber();
		
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
		
		final StringBuilder sb = new StringBuilder();
		sb.append("\n");
		
		final int lastLineNumber = scottReport.getSourceLines().lastKey();
		for (Map.Entry<Integer, String> line : scottReport.getSourceLines().entrySet()) {
			final int lineNumber = line.getKey();
			final String lineText = line.getValue().replaceAll("\t", "    ");
			boolean initialAdded = false;
			
			if (firstLineWithBraketAppended && initialReportAppended == false) {
				initialReportAppended = true;
				initialAdded = initialAdded || renderInitialSnapshot(scottReport, sb, lineText, 0);
			}
			
			initialAdded = initialAdded || renderInitialSnapshot(scottReport, sb, lineText, lineNumber - 1);
			if (lineNumber == lastLineNumber) {
				initialAdded = initialAdded || renderInitialSnapshot(scottReport, sb, lineText, lineNumber);
			}
			
			if (initialAdded) {
				sb.append(addBlankLineRemovingContent(lineText));
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
			for (Map.Entry<String, List<Snapshot>> entry : scottReport.getVariableMapSnapshot(lineNumber).entrySet()) {
				String variableSnapshotText = entry.getKey();

				if (!entry.getValue().isEmpty()) {
					variableSnapshotText += "=";
					int all = entry.getValue().size();
					int lowerLimit = size;
					int upperLimit = all - size;
					int lastItem = all - 1;
					boolean hide = all > (size * 2);
					int counter = 0;
					for (Snapshot snapshot : entry.getValue()) {
						if (hide) {
							if(counter > lowerLimit && counter < upperLimit) {
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
							variableSnapshotText += "[" + snapshot.value.trim() + "]";
						} else {
							variableSnapshotText += snapshot.value.trim();
						}
						counter++;
					}

					if (hide) {
						variableSnapshotText += " =>" + all;
					}
				}
				
				renderMultilineComment(sb, lineText, variableSnapshotText, !isFirstCommentInThisLine);
				isFirstCommentInThisLine = false;
			}

			if (scottReport.getExceptionLineNumber() == lineNumber) {
				renderMultilineComment(sb, lineText, getExceptionMessage(scottReport), !isFirstCommentInThisLine);
				isFirstCommentInThisLine = false;
			}
			sb.append("\n");
			
			firstLineWithBraketAppended = firstLineWithBraketAppended || lineText.contains("{");
		}
		
		return sb.toString();
	}

	private static boolean renderInitialSnapshot(ScottReport scottReport, final StringBuilder sb, final String lineText, final int lineNumber) {
		boolean initialAdded = false;
		if (!scottReport.getInitialSnapshots(lineNumber).isEmpty()) {
			for (Snapshot snapshot : scottReport.getInitialSnapshots(lineNumber)) {
				String comment = "    => " + snapshot.name + "=" + snapshot.value.trim();
				String[] commentLines = comment.split("\\n");
				for (String commentLine : commentLines) {
					sb.append(addBlankLineRemovingContent(lineText));
					sb.append("//");
					sb.append(commentLine);
					sb.append("\n");
				}
			}
			
			if (!scottReport.getInitialSnapshots(lineNumber).isEmpty()) {
				initialAdded = true;
			}
		}
		return initialAdded;
	}

	private static String getExceptionMessage(ScottReport scottReport) {
		if (scottReport.getExceptionMessage() != null) {
			return scottReport.getExceptionClassName() + ": " + scottReport.getExceptionMessage().trim();
		} else {
			return scottReport.getExceptionClassName();
		}
	}
	
	private static void renderMultilineComment(StringBuilder sb, String lineText, String comment, boolean startInNewLine) {
		String[] commentLines = comment.split("\\n");
		for (String commentLine : commentLines) {
			if (startInNewLine) {
				sb.append("\n");
				sb.append(blankLine(lineText));
			}
			
			sb.append("  // ");
			sb.append(commentLine);
			startInNewLine = true;
		}
	}

	/**
	 * Adds a blank line, where new content can be added right after the old content.
	 * E.g, given the following line:
  	 *    |          String dot = ".";
  	 * It produces the following comment line:
  	 * 	  |                             //
	 */
	private static String blankLine(String lineText) {
		return "    " + "|  " + lineText.replaceAll(".", " ");
	}
	
	/**
	 * Adds a blank line, where new content can be added at the place of the old content
	 * 	 * E.g, given the following line:
  	 *    |          String dot = ".";
  	 * It produces the following comment line:
  	 * 	  |          //
	 */
	private static String addBlankLineRemovingContent(String lineText) {
		return "    " + "|  " + lineText.replaceFirst("[^\\s].*$", "");
	}

}
