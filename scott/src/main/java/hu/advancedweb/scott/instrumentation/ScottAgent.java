package hu.advancedweb.scott.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;

/**
 * Scott's Java Agent that instruments classes at class-load time.
 * 
 * For more information, see:
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.instrument/java/lang/instrument/package-summary.html
 * 
 * @author David Csakvari
 */
public class ScottAgent {
	
	private ScottAgent() {
		// Intended to be used as an agent and not to be instantiated directly.
	}
	
	public static void premain(String agentArgument, Instrumentation instrumentation) {
		instrumentation.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				if (loader == null) {
					/*
					 * Leave the class alone, if it is being loaded by the Bootstrap classloader,
					 * as we don't want to do anything with JDK libs. See Issue #22.
					 */
					return classfileBuffer;
				} else {
					try {
						return new ScottClassTransformer().transform(classfileBuffer, ScottConfigurer.getConfiguration());
					} catch (Exception e) {
						System.err.println("Scott: test instrumentation failed for " + className + "!");
						e.printStackTrace();
						throw e;
					}
				}
			}
		});
	}

}
