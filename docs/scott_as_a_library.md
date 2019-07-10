# Using Scott as an instrumentation library

Scott's instrumentation module can be invoked programmatically with fine-tuned instrumentation rules.

With it, instrument classes by adding custom logic to add custom behavior on:
- method start
- method end
- variable change
- field change
- return from a method
- uncaught exception

You can fine tune the tracking rules with various configuration options.

This guide helps gettngs started to build your own solution on top of Scott.


## Instrument class a class file

### Post compilation

One option is to create a tool that can start to perform the instrumentation. A simple way to achieve this
is to create a `public static void main` method that does roughly the following:

```
byte[] originalClass = Files.readAllBytes(path);
byte[] instrumentedClass = new ScottClassTransformer().transform(originalClass, configuration);
Files.write(path, instrumentedClass);
```

### Load time with Java agent

Another popular option is, to create a Java Agent that does the same. For example:

```java
import java.lang.instrument.*;
import java.security.ProtectionDomain;

import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;

public class MyAgent {

	public static void premain(String agentArgument, Instrumentation instrumentation) {
		instrumentation.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				if (loader == null) {
					/*
					 * Leave the class alone, if it is being loaded by the Bootstrap classloader,
					 * as we don't want to do anything with JDK libs.
					 */
					return classfileBuffer;
				} else {
					try {
						return new ScottClassTransformer().transform(classfileBuffer, configuration);
					} catch (Exception e) {
						System.err.println("Instrumentation failed for " + className + "!");
						e.printStackTrace();
						throw e;
					}
				}
			}
		});
	}

}
```

## Configuration

The instrumentation requires a configure object that describe the details. You can create one using the
[Configuration Builder](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/transformation/config/Configuration.java).
For the following configuration instruments all methods annotated by `@Deprecated`, inserting extra code to call
static methods in `com.myorg.TrackerClass` whenever the instrumented method is started, a variable changes, or the method returns.

```java

new Configuration.Builder()
			.setTrackerClass("com.myorg.TrackerClass")
			.setIncludeByAnnotation("java.lang.Deprecated"}))
			.setTrackReturn(true)
			.setTrackLocalVariableAssignments(true)
			.setTrackLocalVariableIncrements(true)
			.setTrackLocalVariablesAfterEveryMethodCall(true)
			.build();
```


## Runtime

To make it complete, you have to define a class with the necessary static methods that the instrumented code can call.
(In the previous example it was the `com.myorg.TrackerClass`).

You have to provide these methods:

- public static void trackMethodStart(int lineNumber, String methodName, Class<?> clazz)
- public static void trackEndOfArgumentsAtMethodStart(int lineNumber, String methodName, Class<?> clazz)
- public static void trackLambdaDefinition(int lineNumber, String methodName, Class<?> clazz)
- public static void trackFieldState(byte value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(short value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(int value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(long value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(float value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(double value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(boolean value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(char value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackFieldState(Object value, String name, int lineNumber, String methodName, Class<?> clazz, boolean isStatic, String owner)
- public static void trackLocalVariableState(byte value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(short value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(int value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(long value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(float value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(double value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(boolean value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(char value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackLocalVariableState(Object value, String name, int lineNumber, String methodName, Class<?> clazz)
- public static void trackUnhandledException(Throwable throwable, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(byte value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(short value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(int value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(long value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(float value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(double value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(boolean value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(char value, int lineNumber, String methodName, Class<?> clazz)
- public static void trackReturn(Object value, int lineNumber, String methodName, Class<?> clazz)

For an inspiration, check [ScottConfigurer](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/ScottConfigurer.java).


