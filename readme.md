Scott Test Reporter
===================

Scott provides **detailed failure messages** for tests written in Java,
**without the use of complex assertion libraries** to aid developers in rapid development,
troubleshooting and debugging of tests.

It automatically tracks the state of the test to provide the important details for a failing scenario,
favoring simple assertions expressed mostly in plain Java over the extensive use of test libraries,
such as Hamcrest or AssertJ.
(Although it plays nicely with other testing tools and frameworks.)

Scott does not intend to be a testing framework, nor does it provide an API to use in the tests.
Instead, it aims to be a small tool that can be dropped into a project to do its job automatically,
so you can worry much less about expressing assertions,
and still have meaningful failure messages.

Supports Java 7 and above.

> **Scott**: All systems automated and ready. A chimpanzee and two trainees could run her.

> **Kirk**: Thank you, Mr. Scott. I'll try not to take that personally.


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

Notice that even without sophisticated assertions the required information is present in the test report.

For every failing test Scott reports the
- assignments to local variables,
- changes made to objects referenced by local variables and fields,
- input parameters and
- relevant fields that the test accesses, but does not modify.

All information is nicely visualized on the source code of the test method.

For a more demos check out the [JUnit examples](https://github.com/dodie/scott/tree/master/scott-examples/junit),
or the [Cucumber showcase](https://github.com/dodie/scott/tree/master/scott-examples/cucumber).


How to use
----------
A Maven Plugin is on it's way to make it even easier,
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
		<version>2.0.1</version>
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

For a demo check out and ```mvn install``` the [scott-example](https://github.com/dodie/scott/tree/master/scott-examples/junit) project
that contains the required setup configuration to use Scott (see [pom.xml](https://github.com/dodie/scott/tree/master/scott-examples/junit/pom.xml))
and a bunch of failing tests for the show.


More to read
------------
[Blog post](https://advancedweb.hu/2015/08/26/scott-detailed-failure-reports/).
