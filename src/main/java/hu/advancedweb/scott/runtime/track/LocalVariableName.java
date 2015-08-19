package hu.advancedweb.scott.runtime.track;

/**
 * Represents a local variable name for a variable index and line number.
 * 
 * @author David Csakvari
 */
public class LocalVariableName {
	public final int lineNumber;
	public final int var;
	public final String name;
	
	public LocalVariableName(int lineNumber, String name, int var) {
		this.lineNumber = lineNumber;
		this.name = name;
		this.var = var;
	}
}
