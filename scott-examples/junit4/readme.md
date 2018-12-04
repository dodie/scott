Scott Test Reporter - JUnit 4 Example project
=============================================

This project contains the necessary setup configuration to use Scott with JUnit 4
(see [pom.xml](https://github.com/dodie/scott/blob/master/scott-examples/junit4/pom.xml))
and a bunch of failing tests to demonstrate the detailed failure messages.


Usage
-----
Run ``` mvn install ``` to see the tests failing.


Demo
----
The following snippets are from the console output of the example project:

**Tracking object state:**
```java
  CounterTest.test_1 
   9|      @Test
  10|      public void test_1() {
  11|          Counter counter = new Counter();  // counter=Counter [state=0]
  12|          
  13|          counter.increase();  // counter=Counter [state=1]
  14|          counter.increase();  // counter=Counter [state=2]
  15|          
  16|          int state = counter.get();  // state=2
  17|          
  18|*         assertEquals(state, 3);  // AssertionError: expected:<2> but was:<3>
  19|      }
```


**Reporting the current values of the related fields:**
```java
  ParameterizedTest.testAddition[3] 
  37|      @Test
  38|      public void testAddition() {
    |          //    => this.a=2
    |          //    => this.b=2
    |          //    => this.expectedSum=4
    |          
  39|          int sum = FaultyAdder.add(a, b);  // sum=5
  40|*         assertThat(sum, equalTo(expectedSum));  // AssertionError: Expected: <4>
    |                                                  //      but: was <5>
  41|      }
```


**Lambda support:**
```java
  LambdaTest.test_with_lambda 
  12|      @Test
  13|      public void test_with_lambda() throws Exception {
  14|          Function<String, String> generatePalindrome = input -> {  // generatePalindrome=hu.advancedweb.example.LambdaTest$$Lambda$1/1486371051@402a079c
    |              //    => input="cat"
    |              
  15|              StringBuilder sb = new StringBuilder();  // sb=
  16|              sb.append(input);  // sb=cat
  17|              sb.reverse();  // sb=tac
  18|              
  19|              String reversed = sb.toString();  // reversed="tac"
  20|              
  21|              String palindrome = input + reversed;  // palindrome="cattac"
  22|              return palindrome;
  23|          };
  24|          
  25|          String word = "cat";  // word="cat"
  26|          
  27|          String palindromized = generatePalindrome.apply(word);  // palindromized="cattac"
  28|          
  29|*         assertThat(palindromized, equalTo(word + word));  // AssertionError: Expected: "catcat"
    |                                                            //      but: was "cattac"
  30|      }
```


**Oh no, sorting the collection mutates the backing array!**
```java
  ListTest.test_2 
  39|      @Test
  40|      public void test_2() throws Throwable {
  41|          Integer[] array = new Integer[] { 1, 4, 2, 3 };  // array=[1, 4, 2, 3]
  42|          List<Integer> list = Arrays.asList(array);  // list=[1, 4, 2, 3]
  43|          Collections.sort(list);  // array=[1, 2, 3, 4]
    |                                   // list=[1, 2, 3, 4]
  44|  
  45|*         assertArrayEquals(array, new Integer[] { 1, 4, 2, 3 });  // ArrayComparisonFailure: arrays first differed at element [1]; expected:<2> but was:<4>
  46|      }
```

