package hu.advancedweb.scott.instrumentation.transformation;

import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;
import hu.advancedweb.scott.instrumentation.transformation.param.InstrumentationActions;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import hu.advancedweb.scott.instrumentation.transformation.param.DiscoveryClassVisitor;


/**
 * Entry point for Scott's ASM based bytecode instrumentation.
 * 
 * @author David Csakvari
 */
public class ScottClassTransformer {

	/**
	 * Instrument the given class based on the configuration.
	 * @param classfileBuffer class to be instrumented
	 * @param configuration configuration settings
	 * @return instrumented class
	 */
	public byte[] transform(byte[] classfileBuffer, Configuration configuration) {
		InstrumentationActions instrumentationActions = calculateTransformationParameters(classfileBuffer, configuration);
		if (!instrumentationActions.includeClass) {
			return classfileBuffer;
		}
		return transform(classfileBuffer, instrumentationActions);
	}

	/**
	 * Based on the structure of the class and the supplied configuration, determine
	 * the concrete instrumentation actions for the class.
	 * @param classfileBuffer class to be analyzed
	 * @param configuration configuration settings
	 * @return instrumentation actions to be applied
	 */
	private InstrumentationActions calculateTransformationParameters(byte[] classfileBuffer, Configuration configuration) {
		DiscoveryClassVisitor discoveryClassVisitor = new DiscoveryClassVisitor(configuration);
		new ClassReader(classfileBuffer).accept(discoveryClassVisitor, 0);
		return discoveryClassVisitor.getTransformationParameters().build();
	}

	/**
	 * Perform the given instrumentation actions on a class.
	 * @param classfileBuffer class to be transformed
	 * @param instrumentationActions instrumentation actions to be applied
	 * @return instrumented class
	 */
	private byte[] transform(byte[] classfileBuffer, InstrumentationActions instrumentationActions) {
		ClassReader classReader = new ClassReader(classfileBuffer);
		ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
		ClassVisitor localVariableStateEmitterTestClassVisitor = new StateTrackingClassVisitor(classWriter,
				instrumentationActions);
		classReader.accept(localVariableStateEmitterTestClassVisitor, 0);
		return classWriter.toByteArray();
	}

}
