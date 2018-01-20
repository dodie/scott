# Recommended: Scott Maven Plugin
The recommended way to use Scott is the **Scott Maven Plugin**. Just add the following to your ```pom.xml``` file.

```xml
<build>
	<plugins>
		<!-- Add the Scott Plugin. -->
		<plugin>
			<groupId>hu.advancedweb</groupId>
			<artifactId>scott-maven-plugin</artifactId>
			<version>${scott.version}</version>
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
		<version>${scott.version}</version>
		<scope>test</scope>
	</dependency>
</dependencies>
```

For complete examples see the following examples:

- [JUnit 4](https://github.com/dodie/scott/tree/master/scott-examples/junit4)
- [JUnit 5](https://github.com/dodie/scott/tree/master/scott-examples/junit5)
- [Cucumber JVM](https://github.com/dodie/scott/tree/master/scott-examples/cucumber)


## Configuration

By default the instrumentation happens automatically for JUnit4, JUnit5 test methods and Cucumber Steps to collect the data.

For every JUnit4 test, it also injects the [ScottReportingRule](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottReportingRule.java) to produce the nice, detailed error messages in case of a failure, based on the collected data.
Similarly, for every JUnit5 test, it injects the [ScottJUnit5Extension](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottJUnit5Extension.java) for the same reason.

So, by default there is no need for additional configuration to use Scott with JUnit4 or JUnit5 tests.

However, the reporting for Cucumber JVM is based on the
[formatter configuration](https://github.com/dodie/scott/blob/master/scott-examples/cucumber/src/test/java/hu/advancedweb/example/FeatureTest.java#L15) of the Cucumber runner, so it can't be done automatically. For this to work, you must manually specify at least one of the formatters Scott provide:

- [ScottCucumberJSONFormatter](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottCucumberJSONFormatter.java)
- [ScottCucumberHTMLFormatter](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottCucumberHTMLFormatter.java)
- [ScottCucumberPrettyFormatter](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/runtime/ScottCucumberPrettyFormatter.java)

See the [Cucumber Example Test](https://github.com/dodie/scott/blob/master/scott-examples/cucumber/src/test/java/hu/advancedweb/example/FeatureTest.java) for more info.


### Configuring the automatic tracking behavior with the Maven Plugin
In case you are not satisfied with the default tracking behavior, the Scott Maven Plugin provides configuration
options:

| Parameter name  | Description   | Default value |
| -------------   | ------------- | ------------- |
| trackMethodAnnotations  | Collect runtime data from a method if it's marked with at least one of the specified annotations.  | org.junit.Test, org.junit.jupiter.api.Test, org.junit.jupiter.api.TestFactory, cucumber.api.java.\* |
| junit4RuleMethodAnnotations | Inject ```ScottReportingRule``` to catch failing tests for JUnit4, if the class has at least one method with at least one of the following annotations. | org.junit.Test |
| junit5RuleMethodAnnotations | Inject ```ScottJUnit5Extension``` to catch failing tests for JUnit5, if the class has at least one method with at least one of the following annotations. | org.junit.jupiter.api.Test, org.junit.jupiter.api.TestFactory |


See the following example for the Plugin configuration:

```xml
<plugin>
	<groupId>hu.advancedweb</groupId>
	<artifactId>scott-maven-plugin</artifactId>
	<version>${scott.version}</version>
	<executions>
		<execution>
			<goals>
				<goal>prepare-agent</goal>
			</goals>
		</execution>
		<configuration>
			<trackMethodAnnotations>
				<trackMethodAnnotation>org.junit.Test</trackMethodAnnotation>
				<trackMethodAnnotation>org.junit.jupiter.api.Test</trackMethodAnnotation>
				<trackMethodAnnotation>org.junit.jupiter.api.TestFactory</trackMethodAnnotation>
				<trackMethodAnnotation>cucumber.api</trackMethodAnnotation>
			</trackMethodAnnotations>
			<junit4RuleMethodAnnotations>
				<junit4RuleMethodAnnotation>org.junit.Test</junit4RuleMethodAnnotation>
			</junit4RuleMethodAnnotations>
			<junit5RuleMethodAnnotations>
				<junit5RuleMethodAnnotation>org.junit.jupiter.api.Test</junit5RuleMethodAnnotation>
				<junit5RuleMethodAnnotation>org.junit.jupiter.api.TestFactory</junit5RuleMethodAnnotation>
			</junit5RuleMethodAnnotations>
		</configuration>
	</executions>
</plugin>
```


### Configuring the automatic tracking behavior with command line arguments
The automatic tracking behavior can also be customized by supplying the following configuration parameters:

| Parameter name  | Description   | Default value |
| -------------   | ------------- | ------------- |
| scott.track.method_annotation  | Collect runtime data from a method if it's marked with at least one of the specified annotations.  | "org.junit.Test", "org.junit.jupiter.api.Test", "org.junit.jupiter.api.TestFactory", "cucumber.api.java.\*" |
| scott.inject_junit4_rule.method_annotation | Inject ```ScottReportingRule``` to catch failing tests for JUnit4, if the class has at least one method with at least one of the following annotations. | "org.junit.Test" |
| scott.inject_junit5_extension.method_annotation | Inject ```ScottJUnit5Extension``` to catch failing tests for JUnit5, if the class has at least one method with at least one of the following annotations. | "org.junit.jupiter.api.Test", "org.junit.jupiter.api.TestFactory" |

Every parameter can contain zero, one or more strings, separated by commas. Each item has to be one of the following:

- A Fully Qualified Name of an annotation.
- Or an expression that starts with a package name and ends with a ```*```.

Currently these parameters as to be passed as java arguments. For example:

```bash
mvn clean install -Dscott.track.method_annotation="org.junit.Test,cucumber.api.java.*"
```


# Manual
However, if you can't use the Maven Plugin for some reason, you can do the necessary steps manually.

1. Get Scott as a dependency.
2. Extract scott-agent.jar.
3. Specify the ```-javaagent:<path-to>/scott-agent.jar``` to your application.

To do exactly this with Maven, see the following example.

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
		<version>${scott.version}</version>
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
