package hu.advancedweb.scott.instrumentation.transformation;

import org.junit.Rule;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import hu.advancedweb.scott.instrumentation.transformation.param.TransformationParameters;
import hu.advancedweb.scott.runtime.ScottReportingRule;

/**
 * Manitpulates the class through the appropriate MethodVisitors.
 * It is done in two traversals, hence it uses two method visitors:
 *  - the first round extracts local variable scopes
 *  - the second one does the instrumentation
 *  
 * @author David Csakvari
 */
public class LocalVariableStateEmitterTestClassVisitor extends ClassVisitor {
	
	private String className;
	private TransformationParameters transformationParameters;
	
	public LocalVariableStateEmitterTestClassVisitor(ClassVisitor cv, TransformationParameters transformationParameters) {
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
			if (transformationParameters.isRuleInjectionRequired) {
				MethodVisitor constructorTransformerMethodVisitor = new ConstructorTransformerMethodVisitor(methodVisitor, access, name, desc, signature, exceptions, className);
				return constructorTransformerMethodVisitor;
			} else {
				return methodVisitor;
			}
		} else if (transformationParameters.isMethodTrackingRequired(name, desc, signature)) {
		    LocalVariableStateEmitterTestMethodVisitor variableMutationEventEmitter = new LocalVariableStateEmitterTestMethodVisitor(methodVisitor, className, name, transformationParameters.isClearingTrackedDataInTheBeginningOfThisMethodRequired(name, desc, signature));
		    MethodVisitor variableExtractor = new LocalVariableScopeExtractorTestMethodVisitor(variableMutationEventEmitter, access, name, desc, signature, exceptions);
		    return variableExtractor;
		} else {
			return methodVisitor;
		}
	}
	
	@Override
	public void visitEnd() {
		if (transformationParameters.isRuleInjectionRequired) {
		    FieldVisitor fv = super.visitField(Opcodes.ACC_PUBLIC, "scottReportingRule", Type.getDescriptor(ScottReportingRule.class), null, null);
		    if (fv != null) {
		    	fv.visitAnnotation(Type.getDescriptor(Rule.class), true).visitEnd();
		    }
		}
		super.visitEnd();
	}
	
}