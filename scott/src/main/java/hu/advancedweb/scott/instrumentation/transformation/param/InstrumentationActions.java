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
public class InstrumentationActions {

	public final String trackerClass;
	public final boolean includeClass;
	private final List<String> trackTheseMethods;
	private final List<String> trackTheseLambdas;
	public final boolean isJUnit4RuleInjectionRequired;
	public final boolean isJUnit5ExtensionInjectionRequired;
	public final boolean trackMethodStart;
	public final boolean trackReturn;
	public final boolean trackUnhandledException;
	public final boolean trackLocalVariableAssignments;
	public final boolean trackLocalVariableIncrements;
	public final boolean trackLocalVariablesAfterEveryMethodCall;
	public final boolean trackFieldAssignments;
	public final boolean trackFieldsAfterEveryMethodCall;
	public final boolean verboseLogging;

	public boolean isMethodTrackingRequired(String methodName, String methodDesc, String methodSignature) {
		return trackTheseMethods.contains(encode(methodName, methodDesc, methodSignature)) ||
				trackTheseLambdas.contains(encode(methodName, methodDesc, methodSignature));
	}

	public boolean anyLambdaMarkedForTracking() {
		return !trackTheseLambdas.isEmpty();
	}

	private InstrumentationActions(
			String trackerClass,
			boolean includeClass,
			List<String> trackTheseMethods,
			List<String> trackTheseLambdas,
			boolean isJUnit4RuleInjectionRequired,
			boolean isJUnit5ExtensionInjectionRequired,
			boolean trackMethodStart,
			boolean trackReturn,
			boolean trackUnhandledException,
			boolean trackLocalVariableAssignments,
			boolean trackLocalVariableIncrements,
			boolean trackLocalVariablesAfterEveryMethodCall,
			boolean trackFieldAssignments,
			boolean trackFieldsAfterEveryMethodCall,
			boolean verboseLogging) {
		this.trackerClass = trackerClass;
		this.includeClass = includeClass;
		this.isJUnit4RuleInjectionRequired = isJUnit4RuleInjectionRequired;
		this.isJUnit5ExtensionInjectionRequired = isJUnit5ExtensionInjectionRequired;
		this.trackTheseMethods = trackTheseMethods;
		this.trackTheseLambdas = trackTheseLambdas;
		this.trackMethodStart = trackMethodStart;
		this.trackReturn = trackReturn;
		this.trackUnhandledException = trackUnhandledException;
		this.trackLocalVariableAssignments = trackLocalVariableAssignments;
		this.trackLocalVariableIncrements = trackLocalVariableIncrements;
		this.trackLocalVariablesAfterEveryMethodCall = trackLocalVariablesAfterEveryMethodCall;
		this.trackFieldAssignments = trackFieldAssignments;
		this.trackFieldsAfterEveryMethodCall = trackFieldsAfterEveryMethodCall;
		this.verboseLogging = verboseLogging;
	}

	public static final class Builder {
		private String trackerClass;
		private boolean includeClass;
		private List<String> trackTheseMethods = new ArrayList<>();
		private List<String> trackTheseLambdas = new ArrayList<>();
		private boolean isJUnit4RuleInjectionRequired;
		private boolean isJUnit5ExtensionInjectionRequired;
		private boolean trackMethodStart;
		private boolean trackReturn;
		private boolean trackUnhandledException;
		private boolean trackLocalVariableAssignments;
		private boolean trackLocalVariableIncrements;
		private boolean trackLocalVariablesAfterEveryMethodCall;
		private boolean trackFieldAssignments;
		private boolean trackFieldsAfterEveryMethodCall;
		private boolean verboseLogging;
		private boolean alreadyInstrumented;

		public InstrumentationActions build() {
			return new InstrumentationActions(
					trackerClass,
					alreadyInstrumented ? false : includeClass,
					Collections.unmodifiableList(trackTheseMethods),
					Collections.unmodifiableList(trackTheseLambdas),
					isJUnit4RuleInjectionRequired,
					isJUnit5ExtensionInjectionRequired,
					trackMethodStart,
					trackReturn,
					trackUnhandledException,
					trackLocalVariableAssignments,
					trackLocalVariableIncrements,
					trackLocalVariablesAfterEveryMethodCall,
					trackFieldAssignments,
					trackFieldsAfterEveryMethodCall,
					verboseLogging);
		}

		void setTrackerClass(String trackerClass) {
			this.trackerClass = trackerClass;
		}

		void markClassForJUnit4RuleInjection() {
			this.isJUnit4RuleInjectionRequired = true;
		}

		void markClassForJUnit5ExtensionInjection() {
			this.isJUnit5ExtensionInjectionRequired = true;
		}

		void markMethodForTracking(String methodName, String methodDesc, String methodSignature) {
			trackTheseMethods.add(encode(methodName, methodDesc, methodSignature));
		}

		boolean anyMethodMarkedForTracking() {
			return !trackTheseMethods.isEmpty();
		}

		void markLambdaForTracking(String methodName, String methodDesc, String methodSignature) {
			trackTheseLambdas.add(encode(methodName, methodDesc, methodSignature));
		}

		void clearTrackedLambdas() {
			trackTheseLambdas.clear();
		}

		void includeClass(boolean includeClass) {
			this.includeClass = includeClass;
		}

		void trackMethodStart(boolean trackMethodStart) {
			this.trackMethodStart = trackMethodStart;
		}

		void trackReturn(boolean trackReturn) {
			this.trackReturn = trackReturn;
		}

		void trackUnhandledException(boolean trackUnhandledException) {
			this.trackUnhandledException = trackUnhandledException;
		}

		void trackLocalVariableAssignments(boolean trackLocalVariableAssignments) {
			this.trackLocalVariableAssignments = trackLocalVariableAssignments;
		}
		
		void trackLocalVariableIncrements(boolean trackLocalVariableIncrements) {
			this.trackLocalVariableIncrements = trackLocalVariableIncrements;
		}
		
		void trackLocalVariablesAfterEveryMethodCall(boolean trackLocalVariablesAfterEveryMethodCall) {
			this.trackLocalVariablesAfterEveryMethodCall = trackLocalVariablesAfterEveryMethodCall;
		}
		
		void trackFieldAssignments(boolean trackFieldAssignments) {
			this.trackFieldAssignments = trackFieldAssignments;
		}
		
		void trackFieldsAfterEveryMethodCall(boolean trackFieldsAfterEveryMethodCall) {
			this.trackFieldsAfterEveryMethodCall = trackFieldsAfterEveryMethodCall;
		}

		public void verboseLogging(boolean verboseLogging) {
			this.verboseLogging = verboseLogging;
		}
		
		public void alreadyInstrumented(boolean alreadyInstrumented) {
			this.alreadyInstrumented = alreadyInstrumented;
		}

	}

	private static String encode(String methodName, String methodDesc, String methodSignature) {
		return methodName + "|" + methodDesc + "|" + methodSignature;
	}

}
