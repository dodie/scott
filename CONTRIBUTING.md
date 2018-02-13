# Contributing

Contributions are welcome! Check the
[development guide](https://github.com/dodie/scott/tree/master/docs/development-guide.md) for some important notes on how to build and debug Scott.
If you are looking for issues that can get you started with the development, see [Issues marked with the help-wanted tag](https://github.com/dodie/scott/issues?q=is%3Aissue+label%3A%22help+wanted%22+is%3Aopen).

## Step 1. Fork and clone the repo

```
git clone git@github.com:your-username/scott.git
```

## Step 2. Compile and run the tests to make sure everything works as expected

```
cd scott
mvn clean install
```

## Step 3. Check how Scott render failing tests
Build the JUnit demo to get a sense what the end users of this library will see.

```
cd scott-examples/junit4
mvn clean install
```

You should see a bunch of failing tests rendered something like the following example:

```java
myTest(hu.advancedweb.example.ListTest) FAILED!
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
```


## Step 4. Implement your changes
Please use the following guidelines:

- Make sure to respect existing formatting conventions. (Follow the same coding style as the code that you are modifying.)
- Update documentation for the change you are making.
- Add tests for your modifications where possible.
- Write descriptive commit messages and add each logical change to a separate commit to make code review easier.

Make the whole test suite pass (Step 2), and check the final rendering (Step 3).


## Step 5. Push and [submit a Pull Request](https://github.com/dodie/scott/compare/)
Done! Now it's my turn to review your PR and get in touch with you. :)
