package hu.advancedweb.scott.runtime.track;

public class ExceptionData extends ContextualData {

	public final Throwable throwable;
	
	public ExceptionData(int lineNumber, String methodName, Throwable throwable) {
		super(lineNumber, methodName);
		this.throwable = throwable;
	}

	@Override
	public String toString() {
		return "ExceptionData [throwable=" + throwable + "]";
	}

}
