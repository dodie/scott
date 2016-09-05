Scott Test Reporter - Example project
=====================================

This project contains the necessary setup configuration to use Scott (see [pom.xml](https://github.com/dodie/scott/blob/master/scott-example/pom.xml))
and a bunch of failing tests to demonstrate the detailed failure messages.

Usage
-----
Run ``` mvn install ``` to see the tests failing.


Example failure report for various failing tests:
-------------------------------------------------
```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running hu.advancedweb.example.UserTest
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.362 sec <<< FAILURE! - in hu.advancedweb.example.UserTest
test_1(hu.advancedweb.example.UserTest)  Time elapsed: 0.219 sec  <<< FAILURE!
java.lang.AssertionError: 
  14|      @Test
  15|      public void test_1() {
  16|          UserService service = new UserService();  // service=hu.advancedweb.example.UserService@5cbc508c
  17|          
  18|          User john = service.createUser("john@doe", "John Doe");  // john=User [id=42, email=john@doe, name=John Doe]
  19|          User jane = service.createUser("jane@doe", "Jane Doe");  // jane=User [id=43, email=jane@doe, name=Jane Doe]
  20|          
  21|*         assertEquals(john.getId(), jane.getId());  // expected:<42> but was:<43>
  22|      }

        at org.junit.Assert.fail(Assert.java:88)
        at org.junit.Assert.failNotEquals(Assert.java:834)
        at org.junit.Assert.assertEquals(Assert.java:118)
        at org.junit.Assert.assertEquals(Assert.java:144)
        at hu.advancedweb.example.UserTest.test_1(UserTest.java:21)

Running hu.advancedweb.example.CounterTest
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.001 sec <<< FAILURE! - in hu.advancedweb.example.CounterTest
test_1(hu.advancedweb.example.CounterTest)  Time elapsed: 0.001 sec  <<< FAILURE!
java.lang.AssertionError: 
  14|      @Test
  15|      public void test_1() {
  16|          Counter counter = new Counter();  // counter=Counter [state=0]
  17|          
  18|          counter.increase();  // counter=Counter [state=1]
  19|          counter.increase();  // counter=Counter [state=2]
  20|          
  21|          int state = counter.get();  // state=2
  22|          
  23|*         assertEquals(state, 3);  // expected:<2> but was:<3>
  24|      }

        at org.junit.Assert.fail(Assert.java:88)
        at org.junit.Assert.failNotEquals(Assert.java:834)
        at org.junit.Assert.assertEquals(Assert.java:645)
        at org.junit.Assert.assertEquals(Assert.java:631)
        at hu.advancedweb.example.CounterTest.test_1(CounterTest.java:23)

Running hu.advancedweb.example.ListTest
Tests run: 3, Failures: 3, Errors: 0, Skipped: 0, Time elapsed: 0.025 sec <<< FAILURE! - in hu.advancedweb.example.ListTest
myTest(hu.advancedweb.example.ListTest)  Time elapsed: 0.006 sec  <<< FAILURE!
java.lang.AssertionError: 
  22|      @Test
  23|      public void myTest() {
  24|          Integer[] myArray = new Integer[] { 1, 4, 2, 4 };  // myArray=[1, 4, 2, 4]
  25|          List<Integer> myList = Arrays.asList(myArray);  // myList=[1, 4, 2, 4]
  26|          
  27|          Set<Integer> mySet = new HashSet<>(myList);  // mySet=[1, 2, 4]
  28|          mySet.remove(4);  // mySet=[1, 2]
  29|  
  30|*         assertTrue(mySet.contains(4));  // AssertionError
  31|      }

        at org.junit.Assert.fail(Assert.java:86)
        at org.junit.Assert.assertTrue(Assert.java:41)
        at org.junit.Assert.assertTrue(Assert.java:52)
        at hu.advancedweb.example.ListTest.myTest(ListTest.java:30)

test_1(hu.advancedweb.example.ListTest)  Time elapsed: 0.017 sec  <<< FAILURE!
java.lang.AssertionError: 
  33|      @Test
  34|      public void test_1() {
  35|          Integer[] myArray = new Integer[] { 1, 4, 2, 4 };  // myArray=[1, 4, 2, 4]
  36|          List<Integer> myList = Arrays.asList(myArray);  // myList=[1, 4, 2, 4]
  37|          
  38|          Set<Integer> mySet = new HashSet<Integer>(myList);  // mySet=[1, 2, 4]
  39|          mySet.remove(1);  // mySet=[2, 4]
  40|  
  41|*         assertEquals(myList.size(), mySet.size());  // expected:<4> but was:<2>
  42|      }

        at org.junit.Assert.fail(Assert.java:88)
        at org.junit.Assert.failNotEquals(Assert.java:834)
        at org.junit.Assert.assertEquals(Assert.java:645)
        at org.junit.Assert.assertEquals(Assert.java:631)
        at hu.advancedweb.example.ListTest.test_1(ListTest.java:41)

test_2(hu.advancedweb.example.ListTest)  Time elapsed: 0.001 sec  <<< FAILURE!
java.lang.AssertionError: 
  44|      @Test
  45|      public void test_2() throws Throwable {
  46|          Integer[] array = new Integer[] { 1, 4, 2, 3 };  // array=[1, 4, 2, 3]
  47|          List<Integer> list = Arrays.asList(array);  // list=[1, 4, 2, 3]
  48|          Collections.sort(list);  // array=[1, 2, 3, 4]
    |                                   // list=[1, 2, 3, 4]
  49|  
  50|*         assertArrayEquals(array, new Integer[] { 1, 4, 2, 3 });  // arrays first differed at element [1]; expected:<2> but was:<4>
  51|      }

        at org.junit.Assert.fail(Assert.java:88)
        at org.junit.Assert.failNotEquals(Assert.java:834)
        at org.junit.Assert.assertEquals(Assert.java:118)
        at org.junit.Assert.assertEquals(Assert.java:144)
        at org.junit.internal.ExactComparisonCriteria.assertElementsEqual(ExactComparisonCriteria.java:8)
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:53)
        at org.junit.Assert.internalArrayEquals(Assert.java:532)
        at org.junit.Assert.assertArrayEquals(Assert.java:283)
        at org.junit.Assert.assertArrayEquals(Assert.java:298)
        at hu.advancedweb.example.ListTest.test_2(ListTest.java:50)

Running hu.advancedweb.example.StringTest
Tests run: 2, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.021 sec <<< FAILURE! - in hu.advancedweb.example.StringTest
test_1(hu.advancedweb.example.StringTest)  Time elapsed: 0.018 sec  <<< FAILURE!
java.lang.AssertionError: 
  14|      @Test
  15|      public void test_1() {
  16|          String first = "Hello";  // first=Hello
  17|          String last = "World";  // last=World
  18|  
  19|          String concatenated = first + " " + last;  // concatenated=Hello World
  20|  
  21|*         assertEquals("Goodbye World", concatenated);  // expected:<[Goodbye] World> but was:<[Hello] World>
  22|      }

        at org.junit.Assert.assertEquals(Assert.java:115)
        at org.junit.Assert.assertEquals(Assert.java:144)
        at hu.advancedweb.example.StringTest.test_1(StringTest.java:21)

test_2(hu.advancedweb.example.StringTest)  Time elapsed: 0 sec  <<< FAILURE!
java.lang.AssertionError: 
  24|      @Test
  25|      public void test_2() {
  26|          String hello = "Hello World";  // hello=Hello World
  27|  
  28|          int indexOfSpace = hello.indexOf(" ");  // indexOfSpace=5
  29|          String lastPart = hello.substring(indexOfSpace);  // lastPart= World
  30|  
  31|*         assertEquals("World", lastPart);  // expected:<[]World> but was:<[ ]World>
  32|      }

        at org.junit.Assert.assertEquals(Assert.java:115)
        at org.junit.Assert.assertEquals(Assert.java:144)
        at hu.advancedweb.example.StringTest.test_2(StringTest.java:31)


```
