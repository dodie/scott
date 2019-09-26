[![License](https://img.shields.io/github/license/dodie/scott.svg)](https://github.com/dodie/scott/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/hu.advancedweb/scott.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22hu.advancedweb%22%20AND%20a%3A%22scott%22)
[![Build Status](https://dev.azure.com/dodiehun/scott/_apis/build/status/dodie.scott?branchName=master)](https://dev.azure.com/dodiehun/scott/_build/latest?definitionId=2&branchName=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hu.advancedweb%3Ascott-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=hu.advancedweb%3Ascott-parent)


Scott Test Reporter
===================

Scott provides failure messages containing **all state changes** for tests written in Java to get meaningful errors even **without assertion libraries**. All information is **presented on the source code of the test method** as comments.

```java
myTest(hu.advancedweb.example.ListTest) Time elapsed: 0.028 sec  <<< FAILURE!
java.lang.AssertionError: 

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
  
        at hu.advancedweb.example.ListTest.myTest(ListTest.java:25)
```

Scott automatically tracks the internal state of the tests to provide important details for a failing scenario. 

Although it plays nicely with other testing tools and frameworks, all information is present in the report even without using sophisticated assertions.

Besides the usual assertion error, Scott reports the changes and assignments related to
- local variables
- input parameters
- fields that the test accesses

Supports [JUnit 4](https://github.com/dodie/scott/tree/master/scott-examples/junit4),
[JUnit 5](https://github.com/dodie/scott/tree/master/scott-examples/junit5),
and [Cucumber Java](https://github.com/dodie/scott/tree/master/scott-examples/cucumber-io-cucumber)
on Java 7+ (up to Java 12).


How to use
----------

Just drop it into your project, and Scott will automatically enhance your test reports. You don't have to use its API
or modify your existing tests to make it work.

> **Scott**: All systems automated and ready. A chimpanzee and two trainees could run her.

> **Kirk**: Thank you, Mr. Scott. I'll try not to take that personally.


### Gradle

Add [hu.advanceweb.scott-gradle-plugin](https://plugins.gradle.org/plugin/hu.advancedweb.scott-gradle-plugin) to your [build.gradle](https://github.com/dodie/scott/blob/master/scott-examples/junit4/build.gradle):
```groovy
plugins {
  id "hu.advanceweb.scott-gradle-plugin" version "3.5.0"
}
```

Example projects:
- [JUnit 4](https://github.com/dodie/scott/tree/master/scott-examples/junit4)
- [JUnit 5](https://github.com/dodie/scott/tree/master/scott-examples/junit5)


### Maven

Add the following to your [pom.xml](https://github.com/dodie/scott/blob/master/scott-examples/junit4/pom.xml):
```xml
<build>
	<plugins>
		<!-- Add the Scott Plugin. -->
		<plugin>
			<groupId>hu.advancedweb</groupId>
			<artifactId>scott-maven-plugin</artifactId>
			<version>3.5.0</version>
			<executions>
				<execution>
					<goals>
						<goal>prepare-agent</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
<dependencies>
	<!-- Add Scott as a dependency -->
	<dependency>
		<groupId>hu.advancedweb</groupId>
		<artifactId>scott</artifactId>
		<version>3.5.0</version>
		<scope>test</scope>
	</dependency>
</dependencies>
```

Example projects:
- [JUnit 4](https://github.com/dodie/scott/tree/master/scott-examples/junit4)
- [JUnit 5](https://github.com/dodie/scott/tree/master/scott-examples/junit5)


### Cucumber

Scott for Cucumber tracks **whole scenarios**, and in case of a failure it prints the details of every step involved.

This feature provides valuable information if a test fails in a CI environment, as it can make it much easier to reproduce and fix browser-based tests, especially for flaky tests.

![HTML](https://github.com/dodie/scott-showcase/blob/master/cucumber_html_main.jpeg "HTML")

Example projects with Cucumber tests:
- [io.cucumber:cucumber-java](https://github.com/dodie/scott/tree/master/scott-examples/cucumber-io-cucumber)
- [info-cukes:cucumber-java](https://github.com/dodie/scott/tree/master/scott-examples/cucumber-info-cukes)


### Wire it up manually

If you can't use the Gradle or Maven Plugin for some reason, you can do the necessary steps [manually](https://github.com/dodie/scott/blob/master/docs/manual_setup.md).


Configuration
-------------
In case you are not satisfied with the default tracking behavior, the Scott Maven Plugin and Gradle Plugin provides [configuration options](https://github.com/dodie/scott/blob/master/docs/configuration.md) to fine-tune its behaviour.


Using Scott as an instrumentation library
-----------------------------------------
Scott's instrumentation module can be invoked programmatically with fine-tuned instrumentation rules so you can
build your own solution on top of Scott. For more information, check the [user guide](https://github.com/dodie/scott/blob/master/docs/scott_as_a_library.md).


Changelog
---------
See [Releases](https://github.com/dodie/scott/releases).


**Highlights from the latest releases:**

- Java 12 support
- [Gradle Plugin](https://github.com/dodie/scott/blob/master/readme.md#how-to-use)
- [Maven Plugin](https://github.com/dodie/scott/blob/master/readme.md#how-to-use)
- Scott Cucumber Java now records [whole scenarios](https://github.com/dodie/scott/tree/master/scott-examples/cucumber-io-cucumber), supports [io.cucumber:cucumber-java](https://github.com/dodie/scott/tree/master/scott-examples/cucumber-io-cucumber) and [info.cukes:cucumber-java](https://github.com/dodie/scott/tree/master/scott-examples/cucumber-info-cukes)
- [Customizable tracking behavior](https://github.com/dodie/scott/blob/master/docs/configuration.md#configuring-the-automatic-tracking-behavior-with-the-maven-plugin)


Contributing
------------
Contributions are welcome! Please make sure to visit the
[contribution](https://github.com/dodie/scott/tree/master/CONTRIBUTING.md) and 
[development guide](https://github.com/dodie/scott/tree/master/docs/development-guide.md) for some important notes on how to build and debug Scott.
If you are looking for issues that can get you started with the development, see [Issues marked with the help-wanted tag](https://github.com/dodie/scott/issues?q=is%3Aissue+label%3A%22help+wanted%22+is%3Aopen).


