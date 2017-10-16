[![License](https://img.shields.io/github/license/dodie/scott.svg)](https://github.com/dodie/scott/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/hu.advancedweb/scott.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22hu.advancedweb%22%20AND%20a%3A%22scott%22)


Scott Test Reporter
===================

Scott provides **detailed failure messages** for tests written in Java,
**without the use of complex assertion libraries** to aid developers in rapid development,
troubleshooting and debugging of tests. All information is **presented on the source code of the test method** as comments.

```java
test_1(hu.advancedweb.example.CounterTest)  Time elapsed: 0.005 sec  <<< FAILURE!
java.lang.AssertionError: 

   9|      @Test
  10|      public void test_1() {
  11|          Counter counter = new Counter();  // counter=Counter [state=0]
  12|          
  13|          counter.increase();  // counter=Counter [state=1]
  14|          counter.increase();  // counter=Counter [state=2]
  15|          
  16|          int state = counter.get();  // state=2
  17|          
  18|*         assertEquals(state, 3);  // AssertionError: expected:<2> but was:<3>
  19|      }

        at hu.advancedweb.example.CounterTest.test_1(CounterTest.java:18)
```


It automatically tracks the internal state of the tests to provide important details for a failing scenario,
favoring simple assertions expressed mostly in plain Java over the extensive use of test libraries,
such as Hamcrest or AssertJ.
(Although it plays nicely with other testing tools and frameworks.)

Scott does not intend to be a testing framework, nor does it provide an API to use in the tests.
Instead, it aims to be a small tool that can be dropped into a project to do its job automatically,
so you can worry much less about expressing assertions, and still have meaningful failure messages.

> **Scott**: All systems automated and ready. A chimpanzee and two trainees could run her.

> **Kirk**: Thank you, Mr. Scott. I'll try not to take that personally.

Supports [JUnit 4](https://github.com/dodie/scott/tree/master/scott-examples/junit4),
[JUnit 5](https://github.com/dodie/scott/tree/master/scott-examples/junit5),
and [Cucumber JVM](https://github.com/dodie/scott/tree/master/scott-examples/cucumber).

Features
--------
Consider this failing test case:

```java
@Test
public void myTest() {
	Integer[] myArray = new Integer[] { 1, 4, 2, 4 };
	List<Integer> myList = Arrays.asList(myArray);

	Set<Integer> mySet = new HashSet<>(myList);
	mySet.remove(4);

	assertTrue(mySet.contains(4));
}
```

Normally it just produces an assertion error without a meaningful message.
But **with Scott**, it shows additional information:

```java
myTest(hu.advancedweb.example.ListTest) FAILED!
  22|      @Test
  23|      public void myTest() {
  24|          Integer[] myArray = new Integer[] { 1, 4, 2, 4 };  // myArray=[1, 4, 2, 4]
  25|          List<Integer> myList = Arrays.asList(myArray);  // myList=[1, 4, 2, 4]
  26|
  27|          Set<Integer> mySet = new HashSet<>(myList);  // mySet=[1, 2, 4]
  28|          mySet.remove(4);  // mySet=[1, 2]
  29|
  30|*         assertTrue(mySet.contains(4));  // AssertionError
  31|      }
```

Notice that even **without sophisticated** assertions the required information is present in the test report.

For every failing test Scott reports the
- assignments to **local variables**,
- **changes** made to objects referenced by local variables and fields,
- **input parameters** and
- **relevant fields** that the test accesses, but does not modify.

All information is nicely **visualized on the source code** of the test method.

For a more demos check out the [JUnit examples](https://github.com/dodie/scott/tree/master/scott-examples/junit4),
or the [Cucumber showcase](https://github.com/dodie/scott/tree/master/scott-examples/cucumber).


How to use
----------

After including Scott to the build flow, it automatically creates the detailed failure messages for failing tests.
All you have to do is to write tests in Java with simple assertions or using your favorite testing library
and run them as you would do normally. Scott will do its magic behind the scenes.

A Maven Plugin is on its way to make it even easier,
but until then add this to your *pom.xml* file:

```xml
<build>
	<plugins>

		...

		<!-- Use the Maven Dependency Plugin to copy Scott's JAR
		     from the dependency to the target directory. -->
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-dependency-plugin</artifactId>
			<version>2.5.1</version>
			<executions>
				<execution>
					<id>copy-agent</id>
					<phase>process-test-classes</phase>
					<goals>
						<goal>copy</goal>
					</goals>
					<configuration>
						<artifactItems>
							<artifactItem>
								<groupId>hu.advancedweb</groupId>
								<artifactId>scott</artifactId>
								<outputDirectory>${project.build.directory}/agents</outputDirectory>
								<destFileName>scott-agent.jar</destFileName>
							</artifactItem>
						</artifactItems>
					</configuration>
				</execution>
			</executions>
		</plugin>

		<!-- Modify the Maven Surefire Plugin's configuration
		     to load Scott's Java Agent for instrumentation.
		     (Works with Failsafe Plugin too.) -->
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<configuration>
				<argLine>-javaagent:${project.build.directory}/agents/scott-agent.jar</argLine>
				<workingDirectory>${basedir}/target</workingDirectory>
			</configuration>
		</plugin>

		...

	</plugins>
</build>
<dependencies>

	...

	<!-- Add Scott and JUnit as dependencies. -->
	<dependency>
		<groupId>hu.advancedweb</groupId>
		<artifactId>scott</artifactId>
		<version>3.0.0</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.12</version>
		<scope>test</scope>
	</dependency>

	...

</dependencies>
```

For a demo check out and ```mvn install``` the [JUnit 4](https://github.com/dodie/scott/tree/master/scott-examples/junit4) or the [JUnit 5](https://github.com/dodie/scott/tree/master/scott-examples/junit5) sample projects
that contains the required setup configuration to use Scott (see the ```pom.xml``` files)
and a bunch of failing tests for the show.


Changelog
---------
See [Releases](https://github.com/dodie/scott/releases).


Contributing
------------
Contributions are welcome! Please make sure to visit the
[contribution](https://github.com/dodie/scott/tree/master/CONTRIBUTING.md) and 
[development guide](https://github.com/dodie/scott/tree/master/docs/development-guide.md) for some important notes on how to build and debug Scott.
If you are looking for issues that can get you started the development, see [Issues marked with the help-wanted tag](https://github.com/dodie/scott/issues?q=is%3Aissue+label%3A%22help+wanted%22+is%3Aopen).
