package testagent;

import java.lang.instrument.Instrumentation;

import testagent.instrumentation.MyClassTransformer;

public class MyAgent {
	public static void premain(String agentArgument, Instrumentation instrumentation) {
		instrumentation.addTransformer(new MyClassTransformer());
	}
}
