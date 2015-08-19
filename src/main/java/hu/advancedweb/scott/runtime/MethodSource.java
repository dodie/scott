package hu.advancedweb.scott.runtime;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Model object for the 
 * @author dodi
 *
 */
public class MethodSource {
	
	private Map<Integer, String> sourceLines = new TreeMap<Integer, String>();
	private int beginLine;
	
	public MethodSource(int beginLine) {
		this.beginLine = beginLine;
	}

	public void addLine(String source) {
		sourceLines.put(beginLine, source);
		beginLine++;
	}
	
	public void commentLine(int lineNumber, String comment) {
		sourceLines.put(lineNumber, sourceLines.get(lineNumber) + " //" + comment);
	}
	
	public Map<Integer, String> getSourceLines() {
		return Collections.unmodifiableMap(sourceLines);
	}

}
