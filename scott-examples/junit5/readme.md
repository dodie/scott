Scott Test Reporter - JUnit 5 Example project
===========================================

This project contains the necessary setup configuration to use Scott with JUnit 5,
(see [pom.xml](https://github.com/dodie/scott/blob/master/scott-examples/junit5/pom.xml))
for details. This example works with Java source level 1.8 and 1.9

Check out the [JUnit 4 example project](https://github.com/dodie/scott/blob/master/scott-examples/junit4)
as well for another bunch of failing tests that demonstrates the detailed failure messages.


Usage
-----
Run ``` mvn install ``` to see the tests failing.


Demo
----

**Asserting exceptions:**
```java
  48|      @Test
  49|      @DisplayName("😱")
  50|      void exceptionTesting() {
  51|*         assertThrows(NullPointerException.class, () -> {  // AssertionFailedError: Expected java.lang.NullPointerException to be thrown, but nothing was thrown.
  52|              List<String> set = new ArrayList<>();  // set=[]
  53|              set.add("I");  // set=[I]
  54|              set.add("will");  // set=[I, will]
  55|              set.add("not");  // set=[I, will, not]
  56|              set.add("explode!");  // set=[I, will, not, explode!]
  57|              assertTrue(set.size() > 1);
  58|          });
  59|      }
```

**Grouped assertions:**
```java
  34|      @Test
  35|      @DisplayName("This is a test with multiple assertions.")
  36|      public void testWithGroupedAssertions() {
  37|          Integer[] myArray = new Integer[] { 1, 4, 2, 4 };  // myArray=[1, 4, 2, 4]
  38|          List<Integer> myList = Arrays.asList(myArray);  // myList=[1, 4, 2, 4]
  39|          
  40|          Set<Integer> mySet = new HashSet<>(myList);  // mySet=[1, 2, 4]
  41|          mySet.remove(4);  // mySet=[1, 2]
  42|  
  43|*         assertAll("mySet is too small",   // MultipleFailuresError: mySet is too small (2 failures)
    |                                            //     It does not contain a whole lot of numbers!
    |                                            //     It does not even contain 4!
    |                  //    => mySet=[1, 2]
    |                  //    => mySet=[1, 2]
    |                  
  44|                  () -> assertTrue(mySet.size() > 2, "It does not contain a whole lot of numbers!"),
  45|                  () -> assertTrue(mySet.contains(4), "It does not even contain 4!"));
  46|      }
```

**Nested classes:**
```java
  NestedClassTest.test 
  19|      @Test
  20|      void test() {
    |          //    => this.value=1
    |          
  21|          String dot = ".";  // dot="."
  22|          value += dot;  // this.value="1."
  23|*         assertEquals("1", value);  // AssertionFailedError: expected: <1> but was: <1.>
  24|      }

  NestedClassTest$NestedClass.test 
  36|          @Test
  37|          void test() {
    |              //    => this.nestedValue=a
    |              //    => (in enclosing NestedClassTest) value=12
    |              
  38|              String dot = ".";  // dot="."
  39|              nestedValue += dot;  // this.nestedValue="a."
  40|              value += dot + nestedValue;  // (in enclosing NestedClassTest) value="12.a."
  41|  
  42|*             assertEquals("12.a", value);  // AssertionFailedError: expected: <12.a> but was: <12.a.>
  43|          }
```

**Exceeding timeout:**
```java
  61|      @Test
  62|      void timeoutExceedingTest() {
  63|*         assertTimeout(ofMillis(2), () -> {  // AssertionFailedError: execution exceeded timeout of 2 ms by 9998 ms
  64|              String calculate = "slow";  // calculate="slow"
  65|              calculate += "operation";  // calculate="slowoperation"
  66|              Thread.sleep(10000L);
  67|          });
  68|      }
```

**Not exceeding timeout:**
```java
  71|      @Test
  72|      void timeoutNotExceededWithResult() {
  73|          String actualResult = assertTimeout(ofMinutes(2), () -> {  // actualResult="result"
  74|              return "result";
  75|          });
  76|*         assertEquals("no result", actualResult);  // AssertionFailedError: expected: <no result> but was: <result>
  77|      }
```

**Simple test with message supplier:**
```java
  23|      @Test
  24|      public void testWithMessageSupplier() {
  25|          String first = "Hello";  // first="Hello"
  26|          String last = "World";  // last="World"
  27|  
  28|          String concatenated = first + " " + last;  // concatenated="Hello World"
  29|  
  30|*         assertEquals("Goodbye World", concatenated,   // AssertionFailedError: Incorrect message. ==> expected: <Goodbye World> but was: <Hello World>
  31|                  () -> "Incorrect message.");
  32|      }
```
