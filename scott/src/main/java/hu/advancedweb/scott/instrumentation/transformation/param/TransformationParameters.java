package hu.advancedweb.scott.instrumentation.transformation.param;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This object is constructed before the instrumentation happens.
 * It is filled for each class based on the contents of the class
 * and the actual Scott parameters.
 * 
 * @see DiscoveryClassVisitor
 * @author David Csakvari
 */
public class TransformationParameters {
	
	public final boolean isRuleInjectionRequired;
	
	private final List<String> trackTheseMethods;
	
	private final List<String> clearTrackedDataInTheBeginningOfTheseMethods;
	
	public boolean isMethodTrackingRequired(String methodName, String methodDesc, String methodSignature) {
		return trackTheseMethods.contains(encode(methodName, methodDesc, methodSignature));
	}

	public boolean isClearingTrackedDataInTheBeginningOfThisMethodRequired(String methodName, String methodDesc, String methodSignature) {
		return clearTrackedDataInTheBeginningOfTheseMethods.contains(encode(methodName, methodDesc, methodSignature));
	}
	
	private TransformationParameters(boolean isRuleInjectionRequired, List<String> trackTheseMethods, List<String> clearTrackedDataInTheBeginningOfTheseMethods) {
		this.isRuleInjectionRequired = isRuleInjectionRequired;
		this.trackTheseMethods = trackTheseMethods;
		this.clearTrackedDataInTheBeginningOfTheseMethods = clearTrackedDataInTheBeginningOfTheseMethods;
	}
	
	public static final class Builder {
		private boolean isRuleInjectionRequired;
		private List<String> trackTheseMethods = new ArrayList<>();
		private List<String> clearTrackedDataInTheBeginningOfTheseMethods = new ArrayList<>();
		
		public TransformationParameters build() {
			return new TransformationParameters(isRuleInjectionRequired, Collections.unmodifiableList(trackTheseMethods), Collections.unmodifiableList(clearTrackedDataInTheBeginningOfTheseMethods));
		}
		
		void markClassForRuleInjection() {
			this.isRuleInjectionRequired = true;
		}

		void markMethodForTracking(String methodName, String methodDesc, String methodSignature) {
			trackTheseMethods.add(encode(methodName, methodDesc, methodSignature));
		}
		
		void markMethodForClearingTrackedData(String methodName, String methodDesc, String methodSignature) {
			clearTrackedDataInTheBeginningOfTheseMethods.add(encode(methodName, methodDesc, methodSignature));
		}
	}
	
	private static String encode(String methodName, String methodDesc, String methodSignature) {
		return methodName + "|" + methodDesc + "|" + methodSignature;
	}

}
