package hu.advancedweb.scott.instrumentation.transformation.param;

public class TransformationParameters {
	
	public final boolean isTestClass;
	
	private TransformationParameters(boolean isTestClass) {
		this.isTestClass = isTestClass;
	}
	
	public static final class Builder {
		boolean isTestClass;
		
		public TransformationParameters build() {
			return new TransformationParameters(isTestClass);
		}
	}

}
