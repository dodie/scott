package hu.advancedweb.scott.instrumentation;

import java.lang.instrument.Instrumentation;

import hu.advancedweb.scott.instrumentation.transformation.TestClassTransformer;

/**
 * Scott's Java Agent that instruments test methods for detailed failure reports.
 * 
 * @author David Csakvari
 */
public class ScottAgent {
	
	public static void premain(String agentArgument, Instrumentation instrumentation) {
		instrumentation.addTransformer(new TestClassTransformer());
	}
	
}
