package hu.advancedweb.scott.instrumentation.transformation.param;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This object is constructed before the instrumentation happens,
 * filled for each class based on the contents of the class
 * and the actual Scott parameters.
 * 
 * @see DiscoveryClassVisitor
 * @author David Csakvari
 */
public class TransformationParameters {
	
	public final boolean isJUnit4RuleInjectionRequired;
	
	public final boolean isJUnit5ExtensionInjectionRequired;

	private final List<String> trackTheseMethods;
	
	private final List<String> trackTheseLambdas;
	

	public boolean isMethodTrackingRequired(String methodName, String methodDesc, String methodSignature) {
		return trackTheseMethods.contains(encode(methodName, methodDesc, methodSignature)) ||
				(trackTheseLambdas.contains(encode(methodName, methodDesc, methodSignature)) && !trackTheseMethods.isEmpty());
	}

	private TransformationParameters(boolean isJUnit4RuleInjectionRequired, boolean isJUnit5ExtensionInjectionRequired, List<String> trackTheseMethods, List<String> trackTheseLambdas) {
		this.isJUnit4RuleInjectionRequired = isJUnit4RuleInjectionRequired;
		this.isJUnit5ExtensionInjectionRequired = isJUnit5ExtensionInjectionRequired;
		this.trackTheseMethods = trackTheseMethods;
		this.trackTheseLambdas = trackTheseLambdas;
	}
	
	public static final class Builder {
		private boolean isJUnit4RuleInjectionRequired;
		private boolean isJUnit5ExtensionInjectionRequired;
		private List<String> trackTheseMethods = new ArrayList<>();
		private List<String> trackTheseLambdas = new ArrayList<>();
		
		public TransformationParameters build() {
			return new TransformationParameters(isJUnit4RuleInjectionRequired, isJUnit5ExtensionInjectionRequired, Collections.unmodifiableList(trackTheseMethods), Collections.unmodifiableList(trackTheseLambdas));
		}
		
		void markClassForJUnit4RuleInjection() {
			this.isJUnit4RuleInjectionRequired = true;
		}
		
		public void markClassForJUnit5ExtensionInjection() {
			this.isJUnit5ExtensionInjectionRequired = true;
		}

		void markMethodForTracking(String methodName, String methodDesc, String methodSignature) {
			trackTheseMethods.add(encode(methodName, methodDesc, methodSignature));
		}
		
		void markLambdaForTracking(String methodName, String methodDesc, String methodSignature) {
			trackTheseLambdas.add(encode(methodName, methodDesc, methodSignature));
		}
		
	}
	
	private static String encode(String methodName, String methodDesc, String methodSignature) {
		return methodName + "|" + methodDesc + "|" + methodSignature;
	}

}
