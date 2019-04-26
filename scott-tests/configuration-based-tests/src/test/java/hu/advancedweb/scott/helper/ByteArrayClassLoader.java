package hu.advancedweb.scott.helper;

import java.util.Map;

public class ByteArrayClassLoader extends ClassLoader {
	
	private Map<String, byte[]> classBytecodes;
	
	public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> classBytecodes) {
		super(parent);
		this.classBytecodes = classBytecodes;
	}

	@Override
	public Class<?> findClass(String name) {
		if (!classBytecodes.containsKey(name)) {
			throw new RuntimeException("Class not found: " + name);
		}
		byte[] bytes = classBytecodes.get(name);
		return defineClass(name, bytes, 0, bytes.length);
	}
	
}
