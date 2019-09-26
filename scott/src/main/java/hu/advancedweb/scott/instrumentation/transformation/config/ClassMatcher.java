package hu.advancedweb.scott.instrumentation.transformation.config;

import java.util.List;

class ClassMatcher {
	
	private ClassMatcher() {
		// Utility class, use static methods instead of instantiating this class.
	}
	
	static boolean anyMatchesAsClassOrPackage(List<String> classFqns, List<String> fqns) {
		for (String classFqn : classFqns) {
			if (matchesAsClass(classFqn, fqns) || matchesAsInnerClass(classFqn, fqns) || matchesAsPackage(classFqn, fqns)) {
				return true;
			}
		}

		return false;
	}

	static boolean matchesAsClass(String classFqn, List<String> fqns) {
		return fqns.contains(classFqn);
	}
	
	static boolean matchesAsInnerClass(String classFqn, List<String> fqns) {
		for (String fqn : fqns) {
			if (classFqn.startsWith(fqn + "$")) {
				return true;
			}
		}

		return false;
	}
	
	static boolean matchesAsPackage(String classFqn, List<String> fqns) {
		for (String fqn : fqns) {
			if (classFqn.startsWith(fqn + ".")) {
				return true;
			}
		}

		return false;
	}

}
