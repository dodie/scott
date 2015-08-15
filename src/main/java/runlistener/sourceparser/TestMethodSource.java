package runlistener.sourceparser;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class TestMethodSource {
	
	private Map<Integer, String> sourceLines = new TreeMap<Integer, String>();
	
	public void addLine(int lineNumber, String source) {
		sourceLines.put(lineNumber, source);
	}
	
	public void commentLine(int lineNumber, String comment) {
		sourceLines.put(lineNumber, sourceLines.get(lineNumber) + " //" + comment);
	}
	
	public Map<Integer, String> getSourceLines() {
		return Collections.unmodifiableMap(sourceLines);
	}

}
