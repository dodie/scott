Scott Test Reporter - Cucumber Example project
==============================================

This project contains the necessary configuration
to use Scott with Cucumber. (See [pom.xml](https://github.com/dodie/scott/blob/master/scott-examples/cucumber/pom.xml) for the project
setup and the [FeatureTest](https://github.com/dodie/scott/blob/master/scott-examples/cucumber/src/test/java/hu/advancedweb/example/FeatureTest.java) for the ```@CucumberOptions```.)
and a bunch of failing tests to demonstrate the detailed failure messages.


Usage
-----
Run ``` mvn install ``` to see the tests failing.


Demo
----
Scott tracks the input parameters and field values, and present them in the test reports.

**Console output:**
![Console](https://github.com/dodie/scott-showcase/blob/master/cucumber_console.png "Console")

**HTML report:**
![HTML](https://github.com/dodie/scott-showcase/blob/master/cucumber_html.png "HTML")

Of course the produced JSON can contain the augmented message, so tools like Jenkins can display it as well.
