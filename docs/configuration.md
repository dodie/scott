# Configuration
## Configuring the automatic tracking behavior
In case you are not satisfied with the default tracking behavior, the Scott Maven Plugin and Gradle Plugin provides configuration
options:

| Parameter name  | Description   | Default value |
| -------------   | ------------- | ------------- |
| trackMethodAnnotations  | Collect runtime data from a method if it's marked with at least one of the specified annotations.  | org.junit.Test, org.junit.jupiter.api.Test, org.junit.jupiter.api.TestFactory, cucumber.api.java.\* |
| junit4RuleMethodAnnotations | Inject ```ScottReportingRule``` to catch failing tests for JUnit4, if the class has at least one method with at least one of the following annotations. | org.junit.Test |
| junit5RuleMethodAnnotations | Inject ```ScottJUnit5Extension``` to catch failing tests for JUnit5, if the class has at least one method with at least one of the following annotations. | org.junit.jupiter.api.Test, org.junit.jupiter.api.TestFactory |


See the following example for the Maven Plugin configuration:

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
With Gradle you can provide these configuration to the plugin by adding an extension object called `scott`:

```groovy
scott {
    toolVersion ='3.4.1'
    trackMethodAnnotations = ['org.junit.Test', 'cucumber.api.java.*']
}
```

Here, we override the scoot version using the `toolVersion` property and also set the `trackMethodAnnotations` parameter describe above. 

## Configuring the automatic tracking behavior with command line arguments
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
