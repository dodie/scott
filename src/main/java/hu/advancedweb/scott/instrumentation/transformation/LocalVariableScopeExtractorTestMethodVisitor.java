package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/**
 * Tracks local variable scopes in test methods and passes this information to the next visitor.
 * 
 * @author David Csakvari
 */
public class LocalVariableScopeExtractorTestMethodVisitor extends MethodNode {
	
	/** True if the current method is a test case. */
	private boolean isTestCase;
	
	/** Map line numbers to labels, as we got labels at local variable visits */
	private Map<Integer, Label> lines = new TreeMap<>();
	
	/** Data collected from local variable visits. */
	private List<LocalVariableScopeLabels> scopes = new ArrayList<>();
	
	/** Next MethodVisitor to be accepted at method end. */
	private LocalVariableStateEmitterTestMethodVisitor next;
	
	
	public LocalVariableScopeExtractorTestMethodVisitor(LocalVariableStateEmitterTestMethodVisitor next, final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		super(Opcodes.ASM5, access, name, desc, signature, exceptions);
		this.next = next;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		isTestCase = "Lorg/junit/Test;".equals(desc);
		
		if (isTestCase) {
			reset();
		}
		
		return super.visitAnnotation(desc, visible);
	}
	
	private void reset() {
		scopes.clear();
		lines.clear();
	}
	
	@Override
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
		if (isTestCase) {
			lines.put(line, start);
		}
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
		
		if (isTestCase) {
			scopes.add(new LocalVariableScopeLabels(index, name, start, end));
		}
	}
	
	@Override
	public void visitEnd() {
		if (isTestCase) {
			List<LocalVariableStateEmitterTestMethodVisitor.LocalVariableScope> localVariableScopes = new ArrayList<>();
			for (LocalVariableScopeLabels range : scopes) {
				int prevLine = 0;
				int startLine = 0;
				int endLine = Integer.MAX_VALUE;
				for (Map.Entry<Integer, Label> entry : lines.entrySet()) {
					if (entry.getValue() == range.start) {
						startLine = prevLine;
					}
					
					if (entry.getValue() == range.end) {
						endLine = prevLine;
					}
					
					prevLine = entry.getKey();
				}
				localVariableScopes.add(new LocalVariableStateEmitterTestMethodVisitor.LocalVariableScope(range.var, range.name, startLine, endLine));
			}
			next.setLocalVariableScopes(localVariableScopes);
		}
		accept(next);
	}
	
	private static class LocalVariableScopeLabels {
		final int var;
		final String name;
		final Label start;
		final Label end;
		
		public LocalVariableScopeLabels(int var, String name, Label start, Label end) {
			this.var = var;
			this.name = name;
			this.start = start;
			this.end = end;
		}
	}

}
