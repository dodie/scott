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

This produces the following message without Spock:
```
test_1(hu.advancedweb.example.ListTest): expected:<4> but was:<3>
```

With Scott, it shows additional information:
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
- and changes made to objects referenced by local variables
- visualised on the source code of the test method.

