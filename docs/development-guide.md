# Required software for development

- *Java 8* to compile the core package, and *Java 11* to run the whole test suite
- *Maven 3.0.5*
- *Gradle 4.10.2*


# How it works

Scott has two parts: **instrumentation** and **runtime**.

![Scott's architecture](https://github.com/dodie/scott/blob/master/docs/architecture.png "Scott's architecture")


## Instrumentation

Data about variables, parameters and fields are collected at runtime. To achieve this Scott
instruments the bytecode of the test methods at startup, during the class loading with a
[Java Agent](http://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html). See [ScottAgent](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/ScottAgent.java).

The test classes are explored and instrumented with with [ASM](http://asm.ow2.org/). The whole process happens really fast, many other tools use this technique in the industry, such as the [JaCoCo Java Code Coverage Library](http://www.eclemma.org/jacoco/).

First, Scott [determines](https://github.com/dodie/scott/tree/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/transformation/param) if a class or method should be instrumented. Then, it  [augments](https://github.com/dodie/scott/tree/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/transformation) the test methods with tracking code that collects data when a test is executed.


## Runtime

When an instrumented test case is executed, it calls Scott's [StateRegistry](https://github.com/dodie/scott/tree/master/scott/src/main/java/hu/advancedweb/scott/runtime/track) after every interesting internal state change with the details of the change. For example it keeps track of the changes made to local variables and fields during a test case.
The ```StateRegistry``` stores this data so it can be queried later, for example in case of test failure. This is done by the [Test Framework Integration](https://github.com/dodie/scott/tree/master/scott/src/main/java/hu/advancedweb/scott/runtime). For example, for are two ways to integrate JUnit4 with Scott: a [Rule](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottReportingRule.java) and a [RunListener](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottRunListener.java). Scott automatically injects the ```ScottReportingRule``` for every JUnit4 test it finds. The Rule clears the ```StateRegistry``` before each test, and in case of a failing test it [renders](https://github.com/dodie/scott/tree/master/scott/src/main/java/hu/advancedweb/scott/runtime/report) the report based on the runtime information and the source code of the test.

Scott has integration for JUnit4, JUnit5 and Cucumber Java. For details, see the [examples](https://github.com/dodie/scott/tree/master/scott-examples).


## Integration

For the above to work, the ```Scott Agent``` has to be passed as a ```-javaagent``` to the application. 
The [Scott Maven Plugin](https://github.com/dodie/scott/tree/master/scott-maven-plugin) helps to automatize this job.
For details see the [usage guide](https://github.com/dodie/scott/blob/master/docs/usage.md).


# Project Layout and Building from Source

The core module where you can find all the instrumentation and rendering parts is ```scott```,
while the related tests are in the```scott-tests``` project. These two are submodules of the root pom,
which is provided for convenience. The tests are in a separate module, because the suite excersizes
the complete artifact much like a real project would use it.

When chasing a bug or adding a new feature make sure to add tests for the expected behavior
and build the project with all the tests by issueing the following on the **root pom**.

```
mvn clean install
```

This [test suite](https://github.com/dodie/scott/tree/master/scott-tests) is responsible for making Scott crash-free, and verify that
the correct values are recorded during test execution. The report rendering part is not examined
in this test suite.

The correct rendering can be ensured by running the example test suites in the [scott-examples](https://github.com/dodie/scott/tree/master/scott-examples) directory,
and examining their outputs manually. These projects also serve as an example about using Scott with various tools
in your projects.


# Recommended Tools

- [ASM Bytecode Outline](https://marketplace.eclipse.org/content/bytecode-outline) plugin for Eclipse
- [ASM Bytecode Outline](https://plugins.jetbrains.com/plugin/5918-asm-bytecode-outline) plugin for IntelliJ IDEA
- [javap](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javap.html) Java Class File Disassembler


# Tips for Bug Hunting

## Tests

Always create a failing test that reproduces the problematic use-case. Try to create a minimal test case that still exhibits the bug.


## Debug logging

Scott can be started in debug mode to make it easier to see what instrumentations happen during the
test execution. To activate it, just set the ```scottDebug``` environment variable to true when running
your tests (for example on Unix: ```export scottDebug="true"```), then Scott shows the visited and modified classes.

For example:
```
Scott instrumentation: Visiting: hu/advancedweb/scott/RecordMutationTest.mutationWithLoops
Scott instrumentation:  - instrumentToClearTrackedDataAndSignalStartOfRecording
Scott instrumentation:  - instrumentToTrackVariableName at 81: LocalVariableScope [var=1, name=i, start=81, end=88]
Scott instrumentation:  - instrumentToTrackVariableState of variable at 81: LocalVariableScope [var=1, name=i, start=81, end=88]
Scott instrumentation:  - instrumentToTrackVariableName at 83: LocalVariableScope [var=2, name=j, start=81, end=86]
Scott instrumentation:  - instrumentToTrackVariableState of variable at 83: LocalVariableScope [var=2, name=j, start=81, end=86]
Scott instrumentation:  - instrumentToTrackVariableName at 84: LocalVariableScope [var=1, name=i, start=81, end=88]
Scott instrumentation:  - instrumentToTrackVariableState of variable at 84: LocalVariableScope [var=1, name=i, start=81, end=88]
Scott instrumentation:  - instrumentToTrackVariableState of variable at 85: LocalVariableScope [var=2, name=j, start=81, end=86]
...
```

## Compare results with original bytecode

You can run the [TestClassTransformer](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/transformation/TestClassTransformer.java) as a standalone application to process a class file with Scott.
It takes two arguments: one for the path of a class file and one for the path where it will write the results.

Then, you can easily compare the original and the instrumented class files with `javap`.


## Typical issues

- If you see an exception in in the `hu.advancedweb.scott.instrumentation` package, chances are that Scott failed to instrument the test cases, and the tests did not even run.
- If the test fails with a `java.lang.VerifyError`, it's a sign that the instrumentation phase finished, but the class files were modified in an illegal way. Example:
```
[ERROR] java.lang.VerifyError:
[ERROR] Bad local variable type
[ERROR] Exception Details:
[ERROR] Location:
[ERROR] hu/advancedweb/example/CounterTest.try_with_resources()V @246: aload
[ERROR] Reason:
[ERROR] Type top (current frame, locals[4]) is not assignable to reference type
[ERROR] Current Frame:
[ERROR] bci: @246
[ERROR] flags: { }
[ERROR] locals: { 'hu/advancedweb/example/CounterTest', 'java/lang/Throwable', 'java/lang/Throwable', 'java/io/ByteArrayOutputStream' }
[ERROR] stack: { 'java/io/ByteArrayOutputStream' }
[ERROR] Bytecode:
[ERROR] 0000000: 1255 1270 b800 5c01 4c01 4dbb 002b 59b7
[ERROR] 0000010: 002d 4e12 7112 7212 7312 70b8 0063 2d12
[ERROR] 0000020: 7212 7312 70b8 0067 bb00 2e59 2d2d 1272
[ERROR] 0000030: 1273 1270 b800 67b7 0030 3a04 1274 1275
```
- If an exception happens in `hu.advancedweb.scott.runtime`, it's a sign that the instrumentation works, but when it tries to call Scott's runtime methods, something goes wrong.
- If the tests work as expected, but there is no output, or something seems to be missing from the recording, it can indicate either a rendering issue, or it might be that the instrumentation did not happen correctly.
