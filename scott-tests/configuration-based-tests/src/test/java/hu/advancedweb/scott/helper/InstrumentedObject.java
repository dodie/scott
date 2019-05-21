package hu.advancedweb.scott.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

/**
 * Dynamically loads and instruments a class from the configuration-example-classes module based on the given FQN.
 * If the class requires additional classes from the project it can be specified as dependencies.
 * After the instrumentation an instance will be created from the class using it's default constructor.
 * 
 * @author David Csakvari
 */
public class InstrumentedObject {

	private static final String BASE_PATH = System.getProperty("user.dir") + File.separator + ".." + File.separator + "configuration-example-classes" + File.separator + "target" + File.separator + "classes" + File.separator;

	private final Class<?> clazz;
	private final Object instance;

	public InstrumentedObject(Class<?> clazz, Object instance) {
		this.clazz = clazz;
		this.instance = instance;
	}

	public static InstrumentedObject create(String fqn, Configuration configuration, String... dependencies) {
		try {
			byte[] originalClass = getBytes(fqn);
			byte[] instrumentedClass = new ScottClassTransformer().transform(originalClass, configuration);

			try {
				Map<String, byte[]> classBytecodes = new HashMap<>();
				classBytecodes.put(fqn, instrumentedClass);

				for (String dependency : dependencies) {
					classBytecodes.put(dependency, getBytes(dependency));
				}

				ByteArrayClassLoader ccl = new ByteArrayClassLoader(Thread.currentThread().getContextClassLoader(), classBytecodes);
				Class<?> clazz = ccl.loadClass(fqn);
				Object instance = clazz.getDeclaredConstructor().newInstance();

				return new InstrumentedObject(clazz, instance);
			} catch (Throwable e) {
				if (e instanceof VerifyError) {
					printBytecode(originalClass, instrumentedClass);
				}
				throw e;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] getBytes(String name) throws IOException {
		String relativeClassFilePath = name.replaceAll("\\.", File.separator) + ".class";
		return Files.readAllBytes(Paths.get(BASE_PATH + relativeClassFilePath));
	}

	private static void printBytecode(byte[] originalClass, byte[] instrumentedClass) {
		System.out.println("");
		System.out.println("Original Class");
		ClassFileStructurePrinter.viewByteCode(originalClass);
		System.out.println("");

		System.out.println("");
		System.out.println("Instrumented Class");
		ClassFileStructurePrinter.viewByteCode(instrumentedClass);
		System.out.println("");
	}
	
	public Object invokeMethod(String methodName) throws Exception {
		return clazz.getDeclaredMethod(methodName).invoke(instance);
	}

}
