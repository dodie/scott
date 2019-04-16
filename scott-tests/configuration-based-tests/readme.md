Scott Test Reporter - Configuration based tests
===============================================

This test suite excersizes Scott via its Java API by dynamically loading
class files and transforming them on the fly with different configuration.

This module also excersizes behavior not used directly by the Scott Test Reporter,
for example excluding short methods from the instrumentation.

It uses class files from the `configuration-example-classes` module by file path based lookup
rather than an ususal classpath based dependency. If the example classes were on the
classpath the test mechanism could not load them dynamically.
