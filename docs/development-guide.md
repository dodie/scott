# Required software for development

- *Java 1.8* to compile the core package, and *Java 1.9* to run the whole test suite
- *Maven 3.0.5*


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

Scott has integration for JUnit4, JUnit5 and Cucumber-JVM. For details, see the [examples](https://github.com/dodie/scott/tree/master/scott-examples).


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

This test suite is responsible for making Scott crash-free, and verify that
the correct values are recorded during test execution. The report rendering part is not examined
in this test suite.

The correct rendering can be ensured by running the example test suites in the ```scott-examples``` directory,
and examining their outputs manually. These projects also serve as an example about using Scott with various tools
in your projects.


# Debug mode

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

