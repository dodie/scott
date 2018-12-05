# Specify Scott agent as a command-line argument

If you can't use the Maven Plugin for some reason, you can do the necessary steps manually.

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

