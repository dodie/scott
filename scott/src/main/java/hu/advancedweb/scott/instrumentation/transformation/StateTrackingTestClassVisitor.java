package hu.advancedweb.scott.instrumentation.transformation;

import org.junit.Rule;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import hu.advancedweb.scott.instrumentation.transformation.param.TransformationParameters;
import hu.advancedweb.scott.runtime.ScottReportingRule;

/**
 * Manitpulates the class through the appropriate MethodVisitors.
 * 
 * @see ConstructorTransformerMethodVisitor
 * @see ScopeExtractorTestMethodVisitor
 * @see StateEmitterTestMethodVisitor
 * @author David Csakvari
 */
public class StateTrackingTestClassVisitor extends ClassVisitor {
	
	private String className;
	private TransformationParameters transformationParameters;
	
	public StateTrackingTestClassVisitor(ClassVisitor cv, TransformationParameters transformationParameters) {
		super(Opcodes.ASM5, cv);
		this.transformationParameters = transformationParameters;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.className = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {                               
		MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

		if (name.equals("<init>")) {
			if (transformationParameters.isJUnit4RuleInjectionRequired) {
				MethodVisitor constructorTransformerMethodVisitor = new ConstructorTransformerMethodVisitor(methodVisitor, access, name, desc, signature, exceptions, className);
				return constructorTransformerMethodVisitor;
			} else {
				return methodVisitor;
			}
		} else if (transformationParameters.isMethodTrackingRequired(name, desc, signature)) {
		    StateEmitterTestMethodVisitor variableMutationEventEmitter = new StateEmitterTestMethodVisitor(methodVisitor, className, name, transformationParameters.isClearingTrackedDataInTheBeginningOfThisMethodRequired(name, desc, signature));
		    MethodVisitor variableExtractor = new ScopeExtractorTestMethodVisitor(variableMutationEventEmitter, access, name, desc, signature, exceptions);
		    return variableExtractor;
		} else {
			return methodVisitor;
		}
	}
	
	@Override
	public void visitEnd() {
		if (transformationParameters.isJUnit4RuleInjectionRequired) {
			FieldVisitor fv = super.visitField(Opcodes.ACC_PUBLIC, "scottReportingRule", Type.getDescriptor(ScottReportingRule.class), null, null);
			fv.visitAnnotation(Type.getDescriptor(Rule.class), true).visitEnd();
		}

		if (transformationParameters.isJUnit5ExtensionInjectionRequired) {
			AnnotationVisitor av0 = super.visitAnnotation("Lorg/junit/jupiter/api/extension/ExtendWith;", true);
			AnnotationVisitor av1 = av0.visitArray("value");
			av1.visit(null, Type.getType("Lhu/advancedweb/scott/runtime/ScottJUnit5Extension;"));
			av1.visitEnd();
			av0.visitEnd();
		}

		super.visitEnd();
	}
	
}
