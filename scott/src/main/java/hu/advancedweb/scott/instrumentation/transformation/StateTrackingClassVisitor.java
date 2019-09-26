package hu.advancedweb.scott.instrumentation.transformation;

import org.junit.Rule;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import hu.advancedweb.scott.instrumentation.transformation.param.InstrumentationActions;
import hu.advancedweb.scott.runtime.ScottReportingRule;

/**
 * Manitpulates the class through the appropriate MethodVisitors.
 * 
 * @see ConstructorTransformerMethodVisitor
 * @see ScopeExtractorMethodVisitor
 * @see StateTrackingMethodVisitor
 * @author David Csakvari
 */
public class StateTrackingClassVisitor extends ClassVisitor {
	
	private String className;
	private InstrumentationActions instrumentationActions;
	
	public StateTrackingClassVisitor(ClassVisitor cv, InstrumentationActions instrumentationActions) {
		super(Opcodes.ASM7, cv);
		this.instrumentationActions = instrumentationActions;
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
			if (instrumentationActions.isJUnit4RuleInjectionRequired) {
				return new ConstructorTransformerMethodVisitor(methodVisitor, access, name, desc, signature, exceptions, className);
			} else {
				return methodVisitor;
			}
		} else if (instrumentationActions.isMethodTrackingRequired(name, desc, signature)) {
		    StateTrackingMethodVisitor variableMutationEventEmitter = new StateTrackingMethodVisitor(methodVisitor, instrumentationActions, className, name, desc);
		    return new ScopeExtractorMethodVisitor(variableMutationEventEmitter, access, name, desc, signature, exceptions);
		} else {
			return methodVisitor;
		}
	}
	
	@Override
	public void visitEnd() {
		if (instrumentationActions.isJUnit4RuleInjectionRequired) {
			FieldVisitor fv = super.visitField(Opcodes.ACC_PUBLIC, "scottReportingRule", Type.getDescriptor(ScottReportingRule.class), null, null);
			fv.visitAnnotation(Type.getDescriptor(Rule.class), true).visitEnd();
		}

		if (instrumentationActions.isJUnit5ExtensionInjectionRequired) {
			AnnotationVisitor av0 = super.visitAnnotation("Lorg/junit/jupiter/api/extension/ExtendWith;", true);
			AnnotationVisitor av1 = av0.visitArray("value");
			av1.visit(null, Type.getType("Lhu/advancedweb/scott/runtime/ScottJUnit5Extension;"));
			av1.visitEnd();
			av0.visitEnd();
		}

		super.visitEnd();
	}
	
}
