Scott Test Reporter
===================

Scott provides detailed failure messages for tests written in Java
based on their runtime behaviour and source code.

With Scott even simple assertions provide the details needed to
trace the cause of the failure and to comprehend the context of the test case.
It provides details about local variables to reduce the need to debug tests.


Example
-------
Consider this failing test case:

```
@Test
public void test_1() {
	Integer[] myArray = new Integer[] { 1, 4, 2, 4 };
	List<Integer> myList = Arrays.asList(myArray);

	Set<Integer> mySet = new HashSet<Integer>(myList);
	mySet.remove(1);

	assertEquals(myList.size(), mySet.size());
}
```

This produces the following message **without Spock**:
```
test_1(hu.advancedweb.example.ListTest): expected:<4> but was:<3>
```

**With Scott**, it shows additional information:
```
test_1(hu.advancedweb.example.ListTest) FAILED!
  14|  @Test
  15|  public void test_1() {
  16|        Integer[] myArray = new Integer[] { 1, 4, 2, 4 }; //myArray=[1, 4, 2, 4]
  17|        List<Integer> myList = Arrays.asList(myArray); //myList=[1, 4, 2, 4]
  18|
  19|        Set<Integer> mySet = new HashSet<Integer>(myList); //mySet=[1, 2, 4]
  20|        mySet.remove(1); //mySet=[2, 4]
  21|
  22|        assertEquals(myList.size(), mySet.size());
  23|  }
```


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
Scott does not intend to be a testing framework. Instead, it aims to be a simple tool
that can be used in an existing project without requiring to change the tests or learn a new API.

Some features I think would be cool to have:
- Inspired by Spock, it would be really cool to show parameters passed to assert statements.
- Currently it supports Maven and JUnit only. (And tests must reside in the *test* direcotry to be discovered.)
In the future it would be great to support other tools as well.
- The reporting is fully text based. IDE support would be really nice.


The project is in early / prototype stage, use it with caution.
Any contribution, idea and feedback is appreciated!

