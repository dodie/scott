package hu.advancedweb.scott.runtime.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Model object for the detailed failure report.
 * 
 * @author David Csakvari
 */
public class ScottReport {
	
	private Map<Integer, String> sourceLines = new TreeMap<Integer, String>();
	private Map<Integer, List<VariableSnapshot>> variableSnapshotForLines = new TreeMap<Integer, List<VariableSnapshot>>();
	private int beginLineNumber;
	private int exceptionLineNumber;
	private String exceptionMessage;
	private String exceptionClassName;
	
	public void setBeginLine(int beginLine) {
		this.beginLineNumber = beginLine;
	}

	public void addLine(String source) {
		sourceLines.put(beginLineNumber, source);
		beginLineNumber++;
	}
	
	public void addVariableSnapshot(int lineNumber, String name, String value) {
		checkIfSourceFound(lineNumber);
		List<VariableSnapshot> variableSnapshots = variableSnapshotForLines.getOrDefault(lineNumber, new ArrayList<VariableSnapshot>());
		variableSnapshots.add(new VariableSnapshot(name, value));
		variableSnapshotForLines.put(lineNumber, variableSnapshots);
	}
	
	public void setException(int lineNumber, String exceptionClassName, String exceptionMessage) {
		checkIfSourceFound(lineNumber);
		this.exceptionLineNumber = lineNumber;
		this.exceptionClassName = exceptionClassName;
		this.exceptionMessage = exceptionMessage;
	}
	
	public Map<Integer, String> getSourceLines() {
		return Collections.unmodifiableMap(sourceLines);
	}
	
	public List<VariableSnapshot> getVariableSnapshots(int lineNumber) {
		return Collections.unmodifiableList(variableSnapshotForLines.getOrDefault(lineNumber, new ArrayList<VariableSnapshot>()));
	}
	
	public int getBeginLineNumber() {
		return beginLineNumber;
	}
	
	public int getExceptionLineNumber() {
		return exceptionLineNumber;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}
	
	public String getExceptionClassName() {
		return exceptionClassName;
	}
	
	private void checkIfSourceFound(int lineNumber) {
		if (!sourceLines.containsKey(lineNumber)) {
			sourceLines.put(lineNumber, "???");
		}
	}


}
