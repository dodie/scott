package hu.advancedweb.scott.instrumentation.transformation.config;

import java.util.ArrayList;
import java.util.List;


/**
 * Based on the configuration parameters of Scott and GhostWriter.
 */
public class Configuration {

	/*
	 * Inclusion/Exclusion settings for classes and methods.
	 */

	/**
	 * List of packages and classes to be instrumented.
	 * When empty, all classes are instrumented.
	 */
	private List<String> include = new ArrayList<String>();

	/**
	 * List of packages and classes to be excluded from the instrumentation.
	 * When empty, no classes are excluded from the instrumentation.
	 */
	private List<String> exclude = new ArrayList<String>();

	/**
	 * Instrument methods if they are marked with at least one of the specified annotations.
	 * When a specified annotation found on a class, all of its methods will be instrumented.
	 * When empty, all methods are instrumented.
	 */
	private List<String> includeByAnnotation = new ArrayList<String>();

	/**
	 * Exclude methods from instrumentation if they are marked with at least one of the specified annotations.
	 * When a specified annotation found on a class, all of its methods will be excluded.
	 * When empty, no methods are excluded from the instrumentation.
	 */
	private List<String> excludeByAnnotation = new ArrayList<String>();

	/**
	 * When a method name matches for any of the supplied parameters, it will not be instrumented.
	 * When empty, no methods are excluded from the instrumentation.
	 */
	private List<String> excludeMethodsByName = new ArrayList<String>();

	/**
	 * Allow lambdas to be instrumented.
	 */
	private boolean includeLambdas = true;

	/**
	 * Instrument lambdas only when at least one method is instrumented in the class.
	 */
	private boolean includeLambdasOnlyWhenOtherInstrumentationIsInPlace = false;

	/**
	 * Exclude methods from instrumentation with the amount of LOC is under or equal to the limit.
	 * When 0, no methods are excluded from the instrumentation.
	 */
	private int minimumMethodLoc = 0;

	/**
	 * Specifies the class which will receive the tracked events. 
	 */
	private String trackerClass;

	/*
	 * Feature flags.
	 */
	
	/**
	 * Track events for entering a method.
	 */
	private boolean trackMethodStart = true;

	/**
	 * Track events for returning from a method.
	 */
	private boolean trackReturn = true;

	/**
	 * Track unhandled exceptions.
	 */
	private boolean trackUnhandledException = true;

	/**
	 * Track local variable assignments.
	 */
	private boolean trackLocalVariableAssignments = true;
	
	/**
	 * Track local variable assignments.
	 */
	private boolean trackLocalVariableIncrements = true;
	
	/**
	 * Track local variable assignments.
	 */
	private boolean trackLocalVariablesAfterEveryMethodCall = true;
	
	/**
	 * Track field changes.
	 */
	private boolean trackFieldStateChanges = true;

	/*
	 * Inject ScottReportingRule to catch failing tests for JUnit4, if the class has at least one method with at least one of the following annotations.
	 * When empty, the rule is not injected to the class.
	 */
	private List<String> injectJUnit4RuleWhenAnnotationFound = new ArrayList<String>();

	/*
	 * Inject ScottJUnit5Extension to catch failing tests for JUnit5, if the class has at least one method with at least one of the following annotations.
	 * When empty, the extension is not injected to the class.
	 */
	private List<String> injectJUnit5ExtensionWhenAnnotationFound = new ArrayList<String>();

	Configuration(
			List<String> include,
			List<String> exclude,
			List<String> includeByAnnotation,
			List<String> excludeByAnnotation,
			List<String> excludeMethodsByName,
			boolean includeLambdas,
			boolean includeLambdasOnlyWhenOtherInstrumentationIsInPlace,
			int minimumMethodLoc,
			String trackerClass,
			boolean trackMethodStart,
			boolean trackReturn,
			boolean trackUnhandledException,
			boolean trackLocalVariableAssignments,
			boolean trackLocalVariableIncrements,
			boolean trackLocalVariablesAfterEveryMethodCall,
			boolean trackFieldStateChanges,
			List<String> injectJUnit4RuleWhenAnnotationFound,
			List<String> injectJUnit5ExtensionWhenAnnotationFound) {
		this.include = include;
		this.exclude = exclude;
		this.includeByAnnotation = includeByAnnotation;
		this.excludeByAnnotation = excludeByAnnotation;
		this.excludeMethodsByName = excludeMethodsByName;
		this.includeLambdas = includeLambdas;
		this.includeLambdasOnlyWhenOtherInstrumentationIsInPlace = includeLambdasOnlyWhenOtherInstrumentationIsInPlace;
		this.trackerClass = trackerClass;
		this.minimumMethodLoc = minimumMethodLoc;
		this.trackMethodStart = trackMethodStart;
		this.trackReturn = trackReturn;
		this.trackUnhandledException = trackUnhandledException;
		this.trackLocalVariableAssignments = trackLocalVariableAssignments;
		this.trackLocalVariableIncrements = trackLocalVariableIncrements;
		this.trackLocalVariablesAfterEveryMethodCall = trackLocalVariablesAfterEveryMethodCall;
		this.trackFieldStateChanges = trackFieldStateChanges;
		this.injectJUnit4RuleWhenAnnotationFound = injectJUnit4RuleWhenAnnotationFound;
		this.injectJUnit5ExtensionWhenAnnotationFound = injectJUnit5ExtensionWhenAnnotationFound;
	}

	public boolean isClassInstrumentationAllowed(String classFqn, List<String> classAnnotations) {
		boolean allowedByInclusion = include.isEmpty() || ClassMatcher.matchesAsClass(classFqn, include) ||ClassMatcher.matchesAsInnerClass(classFqn, include) || ClassMatcher.matchesAsPackage(classFqn, include);
		if (!allowedByInclusion) {
			return false;
		}

		boolean allowedByExclusion = !ClassMatcher.matchesAsClass(classFqn, exclude) && !ClassMatcher.matchesAsInnerClass(classFqn, exclude) && !ClassMatcher.matchesAsPackage(classFqn, exclude);
		if (!allowedByExclusion) {
			return false;
		}

		boolean allowedByExcludeAnnotation = !ClassMatcher.anyMatchesAsClassOrPackage(classAnnotations, excludeByAnnotation);
		if (!allowedByExcludeAnnotation) {
			return false;
		}

		return true;
	}

	public boolean isMethodInstrumentationAllowed(String methodName, int methodLoc, List<String> methodAnnotations, List<String> classAnnotations) {
		boolean allowedByNameExclusion = !excludeMethodsByName.contains(methodName);
		if (!allowedByNameExclusion) {
			return false;
		}

		boolean allowedByMethodLength = minimumMethodLoc <= methodLoc;
		if (!allowedByMethodLength) {
			return false;
		}

		boolean allowedByIncludeAnnotation = includeByAnnotation.isEmpty() ||
				ClassMatcher.anyMatchesAsClassOrPackage(methodAnnotations, includeByAnnotation) ||
				ClassMatcher.anyMatchesAsClassOrPackage(classAnnotations, includeByAnnotation);
		if (!allowedByIncludeAnnotation) {
			return false;
		}

		boolean allowedByExcludeMethodAnnotation = !ClassMatcher.anyMatchesAsClassOrPackage(methodAnnotations, excludeByAnnotation);
		if (!allowedByExcludeMethodAnnotation) {
			return false;
		}

		return true;
	}

	public boolean isLambdaInstrumentationAllowed(int methodLoc) {
		boolean allowedByMethodLength = minimumMethodLoc <= methodLoc;
		if (!allowedByMethodLength) {
			return false;
		}

		return includeLambdas;
	}

	public boolean isLambdaInstrumentationAllowedWhenOtherInstrumentationIsInPlace() {
		return includeLambdasOnlyWhenOtherInstrumentationIsInPlace;
	}

	public boolean isJUnit4RuleInjectionRequired(List<String> methodAnnotations) {
		return ClassMatcher.anyMatchesAsClassOrPackage(methodAnnotations, injectJUnit4RuleWhenAnnotationFound);
	}

	public boolean isJUnit5ExtensionInjectionRequired(List<String> methodAnnotations) {
		return ClassMatcher.anyMatchesAsClassOrPackage(methodAnnotations, injectJUnit5ExtensionWhenAnnotationFound);
	}
	
	public String getTrackerClass() {
		return trackerClass;
	}

	public boolean isTrackMethodStart() {
		return trackMethodStart;
	}

	public boolean isTrackReturn() {
		return trackReturn;
	}

	public boolean isTrackUnhandledException() {
		return trackUnhandledException;
	}

	public boolean isTrackLocalVariableAssignments() {
		return trackLocalVariableAssignments;
	}
	
	public boolean isTrackLocalVariableIncrements() {
		return trackLocalVariableIncrements;
	}
	
	public boolean isTrackLocalVariablesAfterEveryMethodCall() {
		return trackLocalVariablesAfterEveryMethodCall;
	}

	public boolean isTrackFieldStateChanges() {
		return trackFieldStateChanges;
	}


	public static class Builder {

		List<String> include = new ArrayList<String>();
		List<String> exclude = new ArrayList<String>();
		List<String> includeByAnnotation = new ArrayList<String>();
		List<String> excludeByAnnotation = new ArrayList<String>();
		List<String> excludeMethodsByName = new ArrayList<String>();
		boolean includeLambdas = true;
		boolean includeLambdasOnlyWhenOtherInstrumentationIsInPlace = false;
		int minimumMethodLoc = 0;
		String trackerClass;
		boolean trackMethodStart = true;
		boolean trackReturn = true;
		boolean trackUnhandledException = true;
		boolean trackLocalVariableAssignments = true;
		boolean trackLocalVariableIncrements = true;
		boolean trackLocalVariablesAfterEveryMethodCall = true;
		boolean trackFieldStateChanges = true;
		List<String> injectJUnit4RuleWhenAnnotationFound = new ArrayList<String>();
		List<String> injectJUnit5ExtensionWhenAnnotationFound = new ArrayList<String>();

		public Configuration build() {
			if (trackerClass == null) {
				throw new IllegalArgumentException("Missing required parameter: trackerClass");
			}
			
			return new Configuration(
					include,
					exclude,
					includeByAnnotation,
					excludeByAnnotation,
					excludeMethodsByName,
					includeLambdas,
					includeLambdasOnlyWhenOtherInstrumentationIsInPlace,
					minimumMethodLoc,
					trackerClass,
					trackMethodStart,
					trackReturn,
					trackUnhandledException,
					trackLocalVariableAssignments,
					trackLocalVariableIncrements,
					trackLocalVariablesAfterEveryMethodCall,
					trackFieldStateChanges,
					injectJUnit4RuleWhenAnnotationFound,
					injectJUnit5ExtensionWhenAnnotationFound);
		}

		public Builder setInclude(List<String> include) {
			this.include = include;
			return this;
		}

		public Builder setExclude(List<String> exclude) {
			this.exclude = exclude;
			return this;
		}

		public Builder setIncludeByAnnotation(List<String> includeByAnnotation) {
			this.includeByAnnotation = includeByAnnotation;
			return this;
		}

		public Builder setExcludeByAnnotation(List<String> excludeByAnnotation) {
			this.excludeByAnnotation = excludeByAnnotation;
			return this;
		}

		public Builder setExcludeMethodsByName(List<String> excludeMethodsByName) {
			this.excludeMethodsByName = excludeMethodsByName;
			return this;
		}

		public Builder setIncludeLambdas(boolean includeLambdas) {
			this.includeLambdas = includeLambdas;
			return this;
		}

		public Builder setIncludeLambdasOnlyWhenOtherInstrumentationIsInPlace(
				boolean includeLambdasOnlyWhenOtherInstrumentationIsInPlace) {
			this.includeLambdasOnlyWhenOtherInstrumentationIsInPlace = includeLambdasOnlyWhenOtherInstrumentationIsInPlace;
			return this;
		}

		public Builder setTrackMethodStart(boolean trackMethodStart) {
			this.trackMethodStart = trackMethodStart;
			return this;
		}
		
		public Builder setTrackerClass(String trackerClass) {
			this.trackerClass = trackerClass;
			return this;
		}

		public Builder setMinimumMethodLoc(int minimumMethodLoc) {
			this.minimumMethodLoc = minimumMethodLoc;
			return this;
		}

		public Builder setTrackReturn(boolean trackReturn) {
			this.trackReturn = trackReturn;
			return this;
		}

		public Builder setTrackUnhandledException(boolean trackUnhandledException) {
			this.trackUnhandledException = trackUnhandledException;
			return this;
		}

		public Builder setTrackLocalVariableAssignments(boolean trackLocalVariableAssignments) {
			this.trackLocalVariableAssignments = trackLocalVariableAssignments;
			return this;
		}
		
		public Builder setTrackLocalVariableIncrements(boolean trackLocalVariableIncrements) {
			this.trackLocalVariableIncrements = trackLocalVariableIncrements;
			return this;
		}
		
		public Builder setTrackLocalVariablesAfterEveryMethodCall(boolean trackLocalVariablesAfterEveryMethodCall) {
			this.trackLocalVariablesAfterEveryMethodCall = trackLocalVariablesAfterEveryMethodCall;
			return this;
		}

		public Builder setTrackFieldStateChanges(boolean trackFieldStateChanges) {
			this.trackFieldStateChanges = trackFieldStateChanges;
			return this;
		}

		public Builder setInjectJUnit4RuleWhenAnnotationFound(List<String> injectJUnit4RuleWhenAnnotationFound) {
			this.injectJUnit4RuleWhenAnnotationFound = injectJUnit4RuleWhenAnnotationFound;
			return this;
		}

		public Builder setInjectJUnit5ExtensionWhenAnnotationFound(
				List<String> injectJUnit5ExtensionWhenAnnotationFound) {
			this.injectJUnit5ExtensionWhenAnnotationFound = injectJUnit5ExtensionWhenAnnotationFound;
			return this;
		}
	}

}
