package hu.advancedweb.scott.instrumentation;

import hu.advancedweb.scott.instrumentation.transformation.TestVariableMutationEventEmitterClassTransformer;

import java.lang.instrument.Instrumentation;

public class ScottAgent {
	public static void premain(String agentArgument, Instrumentation instrumentation) {
		instrumentation.addTransformer(new TestVariableMutationEventEmitterClassTransformer());
	}
}
