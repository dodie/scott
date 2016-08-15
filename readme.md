Scott Test Reporter
===================

Scott provides detailed failure messages for tests written in Java
based on their runtime behaviour and source code.

With Scott even simple assertions provide the details needed to
trace the cause of the failure and to comprehend the context of the test case.
It provides details about local variables to reduce the need to debug tests.

It does not intend to be a testing framework. Instead, it aims to be a simple tool
that can be used in an existing project without requiring to change the tests or learn a new API,
favoring simple assertions expressed mostly in plain old Java.

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
  15|   @Test
  16|   public void myTest() {
  17|           Integer[] myArray = new Integer[] { 1, 4, 2, 4 }; //myArray=[1, 4, 2, 4]
  18|           List<Integer> myList = Arrays.asList(myArray); //myList=[1, 4, 2, 4]
  19|  
  20|           Set<Integer> mySet = new HashSet<>(myList); //mySet=[1, 2, 4]
  21|           mySet.remove(4); //mySet=[1, 2]
  22|  
  23|           assertTrue(mySet.contains(4));
  24|   }
```

Notice that even without using sophisticated assertions the required information is present in the test report.


Features
--------
For every failing test it reports the
- assignments to local variables
- and changes made to objects referenced by local variables,
- visualised on the source code of the test method.


How to try
----------
The [scott-example](https://github.com/dodie/scott/tree/master/scott-example) project contains the required setup configuration to use Scott (see [pom.xml](https://github.com/dodie/scott/blob/master/scott-example/pom.xml)).

1. Check out this repo
2. Run ```mvn install``` in the *scott* directory.
3. Run ```mvn install``` in the *scott-example* directory, and see the tests failing.


Goals/Limitations
-----------------
Some important features are need some more work:
- Inspired by Spock, it would be really cool to show parameters passed to assert statements.
- Currently it supports Maven and JUnit only, and tests must reside in the *test* direcotry to be discovered.
In the future it would be great to relax these limitations.
- Better Maven and IDE support for easier usage.

The project is in an early stage, any contribution, idea and feedback is appreciated!

