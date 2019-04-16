package hu.advancedweb.scott.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import hu.advancedweb.scott.instrumentation.transformation.ScottClassTransformer;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

public class CustomClassLoader extends ClassLoader {
	
	static final String BASE_PATH = System.getProperty("user.dir") + File.separator + ".." + File.separator + "configuration-example-classes" + File.separator + "target" + File.separator + "classes" + File.separator;
	
	public static Class<?> loadAndTransform(String name, Configuration configuration) {
		try {
			String relativeClassFilePath = name.replaceAll("\\.", File.separator) + ".class";
			
			byte[] originalClass = Files.readAllBytes(Paths.get(BASE_PATH + relativeClassFilePath));
			byte[] instrumentedClass = new ScottClassTransformer().transform(originalClass, configuration);
			
			CustomClassLoader ccl = new CustomClassLoader(Thread.currentThread().getContextClassLoader(), instrumentedClass);
			return ccl.loadClass(name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private byte[] bytes;
	
	public CustomClassLoader(ClassLoader parent, byte[] bytes) {
		super(parent);
		this.bytes = bytes;
	}

	@Override
	public Class<?> findClass(String name) {
		return defineClass(name, bytes, 0, bytes.length);
	}
	
}
