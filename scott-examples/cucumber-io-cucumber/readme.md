Scott Test Reporter - Cucumber Example project
==============================================

This project contains the necessary configuration
to use Scott with Cucumber (```io.cucumber:cucumber-java```). (See [pom.xml](https://github.com/dodie/scott/blob/master/scott-examples/cucumber/pom.xml)
for the project setup and the [FeatureTest](https://github.com/dodie/scott/blob/master/scott-examples/cucumber-cucumber-io/src/test/java/hu/advancedweb/example/FeatureTest.java)
for the ```@CucumberOptions```.) and a bunch of failing tests to demonstrate the detailed failure messages.

Scott for Cucumber **tracks whole scenarios**, and in case of a failure it prints the details of every step involved.

This feature provides valuable information if a test fails in a CI environment, as it can make it much easier to reproduce and fix browser-based tests, especially for flaky tests.


Usage
-----
Run ``` mvn install ``` to see the tests failing. For a Docker-based setup, see the [development guide](https://github.com/dodie/scott/blob/master/docs/development-guide.md).


Demo
----
Scott tracks the input parameters and field values, and present them in the test reports.

**HTML report:**
![HTML](https://github.com/dodie/scott-showcase/blob/master/cucumber_html2.png "HTML")

