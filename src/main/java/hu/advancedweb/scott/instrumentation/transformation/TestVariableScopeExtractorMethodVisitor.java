package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class TestVariableScopeExtractorMethodVisitor extends MethodNode {
	
	TestVariableMutationEventEmitterMethodVisitor mv;
	public TestVariableScopeExtractorMethodVisitor(TestVariableMutationEventEmitterMethodVisitor mv, final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		super(Opcodes.ASM5, access, name, desc, signature, exceptions);
		this.mv = mv;
	}
	
	boolean isTestCase;
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		isTestCase = "Lorg/junit/Test;".equals(desc);
		if (isTestCase) {
			reset();
		}
		return super.visitAnnotation(desc, visible);
	}
	
	private void reset() {
		mv.resetLocalVariableScopes();		
	}
	

	Map<Integer, Label> lines = new TreeMap<>();
	List<LocalVariableRange> ranges = new ArrayList<>();
	
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
			ranges.add(new LocalVariableRange(index, name, start, end));
		}
	}
	
	@Override
	public void visitEnd() {
		if (isTestCase) {
			
			for (LocalVariableRange range : ranges) {
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
				mv.addLocalVariableScope(range.var, range.name, startLine, endLine);
			}
		}
		accept(mv);
	}
	
	private static class LocalVariableRange {
		final int var;
		final String name;
		final Label start;
		final Label end;
		
		public LocalVariableRange(int var, String name, Label start, Label end) {
			this.var = var;
			this.name = name;
			this.start = start;
			this.end = end;
		}
	}


}
