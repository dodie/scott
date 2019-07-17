# Manual setup

Although Scott has Gradle and Maven plugins to ease integration (check this guide for more information [here](https://github.com/dodie/scott)), it is possible to integrate it manually, to have full control over the instrumentation.

There are two options:

1. Specify the agent manually
2. Transform the classes after compilation


## 1. Specify the agent manually

If you can't use the Gradle or Maven Plugin for some reason, you can do the necessary steps manually.

1. Get Scott as a dependency.
2. Extract scott-agent.jar.
3. Specify the ```-javaagent:<path-to>/scott-agent.jar``` to your application.

It can be done in multiple ways. For an example to do exactly this with Maven, see the following example.

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

## 2. Transform the classes after compilation

It's possible modify existing class files (or create new class files based on existing class files) to include the
instrumentation.

1. Get Scott as a dependency.
2. Use the [ScottClassFileTransformer](https://github.com/dodie/scott/blob/master/scott/src/main/java/hu/advancedweb/scott/instrumentation/ScottClassFileTransformer.java) to transform class files as part of the build process. When using it, make sure to have all runtime dependencies of the code available on the classpath. For more information, see Issue #79.
3. Make sure to include Scott as a run-time dependency to your application for the instrumentation to work.

The following Gradle setup achieves something similar:

```groovy
import hu.advancedweb.scott.instrumentation.ScottClassFileTransformer

buildscript {
	dependencies {
		classpath "hu.advancedweb:scott:3.5.0"
	}
}

task instrument(type: JavaExec) {
	main = 'hu.advancedweb.scott.instrumentation.ScottClassFileTransformer'
	args = [sourceSets.main.java.outputDir.absolutePath]
	classpath = buildscript.configurations.classpath
	classpath += sourceSets.main.runtimeClasspath
}

compileJava.finalizedBy instrument
```
