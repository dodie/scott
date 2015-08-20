Scott Test Reporter
===================

Scott provides detailed failure messages for tests written in Java
based on their runtime behaviour and source code.

For example, consider this failing test case:

```
@Test
public void test_1() {
	Integer[] array = new Integer[] { 1, 4, 2, 4 };
	List<Integer> list = Arrays.asList(array);

	Set<Integer> set = new HashSet<Integer>(list);
	set.remove(1);

	assertEquals(list.size(), set.size());
}
```

This produces the following message without Spock:
```
test_1(hu.advancedweb.example.ListTest): expected:<4> but was:<3>
```

With Scott, it shows additional information:
```
test_1(hu.advancedweb.example.ListTest) FAILED!
  14|   @Test
  15|   public void test_1() {
  16|           Integer[] array = new Integer[] { 1, 4, 2, 4 }; //array=[1, 4, 2, 4]
  17|           List<Integer> list = Arrays.asList(array); //list=[1, 4, 2, 4]
  18|
  19|           Set<Integer> set = new HashSet<Integer>(list); //set=[1, 2, 4]
  20|           set.remove(1); //set=[2, 4]
  21|
  22|           assertEquals(list.size(), set.size());
  23|   }
```

Features
--------
For every failing test it reports the
- source code of the failing test method
- the newly assigned values for local variables
- changes made to object referenced by local variables

