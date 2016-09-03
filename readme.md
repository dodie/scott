Scott Test Reporter
===================

Scott provides detailed failure messages for tests written in Java
based on their runtime behaviour and source code.

With Scott even simple assertions provide the details needed to
trace the cause of the failure and to comprehend the context of the test case.
It provides details about local variables to reduce the need to debug tests.

It does not intend to be a testing framework. Instead, it aims to be a simple tool
that can be used in an existing project without requiring to change the tests or
learn a new testing API, favoring simple assertions expressed mostly in plain old Java.

Supports Java 6, 7 and 8.


Example
-------
Consider this failing test case:

```
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

```
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

Notice that even without using sophisticated assertions the required information is present in the test report.


Features
--------
For every failing test it reports the
- assignments to local variables
- and changes made to objects referenced by local variables,
- visualised on the source code of the test method.


How to use
----------
A Maven Plugin is on it's way to make it even easier,
but until then add this to your *pom.xml* file:

```
<build>
	<plugins>

		...

		<!-- Use the Maven Dependency Plugin to copy Scott's JAR from the dependency to a directory. -->
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

		<!-- Modify the Maven Surefire Plugin's configuration that runs the tests
		     to load Scott's Java Agent for instrumentation
		     and to use Scott's JUnit listener to produce the test reports. -->
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<configuration>
				<argLine>-javaagent:${project.build.directory}/agents/scott-agent.jar</argLine>
				<workingDirectory>${basedir}/target</workingDirectory>
				<properties>
					<property>
						<name>listener</name>
						<value>hu.advancedweb.scott.runtime.ScottRunListener</value>
					</property>
				</properties>
			</configuration>
		</plugin>

		...

	</plugins>
</build>
<dependencies>

	...

	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.12</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>hu.advancedweb</groupId>
		<artifactId>scott</artifactId>
		<version>1.0.0</version>
		<scope>test</scope>
	</dependency>

	...

</dependencies>
```

For a demo check out and ```mvn install``` the [scott-example](https://github.com/dodie/scott/tree/master/scott-example) project
that contains the required setup configuration to use Scott (see [pom.xml](https://github.com/dodie/scott/blob/master/scott-example/pom.xml))
and a bunch of failing tests for the show.


Goals/Limitations
-----------------
Some important features are need some more work:
- Inspired by Spock, it would be really cool to show parameters passed to assert statements.
- Currently it supports Maven and JUnit only, and tests must reside in the *test* direcotry to be discovered.
In the future it would be great to relax these limitations.
- Better Maven and IDE support for easier usage.

The project is in an early stage, any contribution, idea and feedback is appreciated!

