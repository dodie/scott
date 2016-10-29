Scott Test Reporter - Cucumber Example project
==============================================

This project contains the necessary configuration and Cucumber Formatters
to use Scott with Cucumber. (see [pom.xml](https://github.com/dodie/scott/blob/master/scott-example/pom.xml))
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
