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
	
	private final List<String> instrumentTheseMethods;
	
	public boolean isMethodInstrumentationRequired(String methodName, String methodDesc, String methodSignature) {
		return instrumentTheseMethods.contains(methodName + "|" + methodDesc + "|" + methodSignature);
	}
	
	private TransformationParameters(boolean isRuleInjectionRequired, List<String> instrumentTheseMethods) {
		this.isRuleInjectionRequired = isRuleInjectionRequired;
		this.instrumentTheseMethods = instrumentTheseMethods;
	}
	
	public static final class Builder {
		boolean isRuleInjectionRequired;
		List<String> instrumentTheseMethods = new ArrayList<>();
		
		public TransformationParameters build() {
			return new TransformationParameters(isRuleInjectionRequired, Collections.unmodifiableList(instrumentTheseMethods));
		}

		void markMethodForInstrumentation(String methodName, String methodDesc, String methodSignature) {
			instrumentTheseMethods.add(methodName + "|" + methodDesc + "|" + methodSignature);	
		}
	}

}
