package testagent;

import java.lang.instrument.Instrumentation;

/*
http://www.eclemma.org/jacoco/trunk/doc/implementation.html
http://www.javabeat.net/introduction-to-java-agents/
https://blog.newrelic.com/2014/09/29/diving-bytecode-manipulation-creating-audit-log-asm-javassist/
http://www.slideshare.net/ashleypuls/bytecode-manipulation

http://stackoverflow.com/questions/25602876/javassist-source-code-and-empty-lines

asm - visit source
http://asm.ow2.org/asm40/javadoc/user/org/objectweb/asm/ClassVisitor.html#visitSource(java.lang.String, java.lang.String)

http://www.geekyarticles.com/2011/10/manipulating-java-class-files-with-asm.html

http://docs.oracle.com/javase/7/docs/technotes/guides/jar/jar.html
 */


/**
 * Usage:
 * java -cp /home/dodi/.m2/repository/org/ow2/asm/asm/5.0.4/asm-5.0.4.jar:. -javaagent:/home/dodi/workspace_experiments/testagent/target/testagent-0.0.1-SNAPSHOT.jar testclient.Example
 * at testclient target/classes
 * @author dodi
 *
 */
public class MyAgent {
	public static void premain(String agentArgument, Instrumentation instrumentation) {
		instrumentation.addTransformer(new MyClassTransformer());
	}
}


/*

- Instrument test methods
  - detect if a class is a test class, if not, leave it alone
  - find and augment test methods:
    - track value assignments
    - object tostring representations in assertions (only in assertions?)
  - interact with junit runners:
    - how can one augment the detailed error messages?
    - is it possible to load the source of the test only at failure-time, not for every test?
    
  - It seems that Junit does not provide infrastructure for this kind of job, so Im gona use a static helper class 
    to store the collected data, and a run listener to process it
 */

