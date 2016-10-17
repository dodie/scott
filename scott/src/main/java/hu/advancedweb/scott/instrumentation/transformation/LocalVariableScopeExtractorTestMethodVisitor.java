package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import hu.advancedweb.scott.instrumentation.transformation.LocalVariableStateEmitterTestMethodVisitor.AccessedField;

/**
 * Tracks local variable scopes in test methods and passes this information to the next visitor.
 * 
 * @author David Csakvari
 */
public class LocalVariableScopeExtractorTestMethodVisitor extends MethodNode {
	
	/** Map line numbers to labels, as we got labels at local variable visits */
	private TreeMap<Integer, Label> lines = new TreeMap<>();
	
	/** Data collected from local variable visits. */
	private List<LocalVariableScopeLabels> scopes = new ArrayList<>();
	
	private Set<AccessedField> accessedFields = new LinkedHashSet<>();
	
	private Set<TryCatchBlock> tryCatchBlocks = new HashSet<>();
	
	/** Next MethodVisitor to be accepted at method end. */
	private LocalVariableStateEmitterTestMethodVisitor next;
	
	private String className;
	
	public LocalVariableScopeExtractorTestMethodVisitor(LocalVariableStateEmitterTestMethodVisitor next, final int access, final String name, final String desc, final String signature, final String[] exceptions, String className) {
		super(Opcodes.ASM5, access, name, desc, signature, exceptions);
		this.next = next;
		this.className = className;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		reset();
		return super.visitAnnotation(desc, visible);
	}
	
	private void reset() {
		scopes.clear();
		accessedFields.clear();
		lines.clear();
		tryCatchBlocks.clear();
	}
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		super.visitTryCatchBlock(start, end, handler, type);
		tryCatchBlocks.add(new TryCatchBlock(start, handler));
	}
	
	@Override
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
		lines.put(line, start);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
		scopes.add(new LocalVariableScopeLabels(index, name, desc, start, end));
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);
		
		final boolean isStatic;
		if (Opcodes.GETSTATIC == opcode || Opcodes.PUTSTATIC == opcode) {
			isStatic = true;
		} else {
			isStatic = false;
		}
		
		if (className.equals(owner)) {
			accessedFields.add(new AccessedField(owner, name, desc, isStatic));
		}
	}
	
	@Override
	public void visitEnd() {
		List<LocalVariableStateEmitterTestMethodVisitor.LocalVariableScope> localVariableScopes = new ArrayList<>();
		for (LocalVariableScopeLabels range : scopes) {

			int prevLine = 0; // if the LocalVariableScope start line is 0 it means input parameter
			int startLine = lines.firstKey();
			int endLine = lines.lastKey();
			
			TryCatchBlock inTry = null;
			Stack<TryCatchBlock> tryCatchBlockScopes = new Stack<>();
			for (Map.Entry<Integer, Label> entry : lines.entrySet()) {
				Label label = entry.getValue();
				
				if (label == range.start) {
					startLine = prevLine;
					
					if (!tryCatchBlockScopes.isEmpty()) {
						inTry = tryCatchBlockScopes.peek();
					}
				} else if (inTry == null && label == range.end) {
					endLine = prevLine;
				} else if (inTry != null && label == inTry.handler) {
					endLine = prevLine;
				}
				
				/*
				 * Fix for issue #14: Variable scopes in Try blocks previously had wrong end line number, 
				 * as they pointied to the end of the catch block, even if they were declared in the try block.
				 */
				for (TryCatchBlock tryCatchBlock : tryCatchBlocks) {
					if (label == tryCatchBlock.start) {
						tryCatchBlockScopes.push(tryCatchBlock);
					} else if (label == tryCatchBlock.handler) {
						tryCatchBlockScopes.pop();
					}
				}
				
				prevLine = entry.getKey();
			}
			
			if (startLine <= endLine) {
				localVariableScopes.add(new LocalVariableStateEmitterTestMethodVisitor.LocalVariableScope(range.var, range.name, VariableType.getByDesc(range.desc), startLine, endLine));
			} else {
				// Sometimes the end label is for an earlier line number than the start label, see Issue #17.
				localVariableScopes.add(new LocalVariableStateEmitterTestMethodVisitor.LocalVariableScope(range.var, range.name, VariableType.getByDesc(range.desc), endLine, startLine));
			}
				
		}
		next.setLocalVariableScopes(localVariableScopes);
		next.setAccessedFields(accessedFields);
		accept(next);
	}
	
	private static class LocalVariableScopeLabels {
		final int var;
		final String name;
		final String desc;
		final Label start;
		final Label end;
		
		public LocalVariableScopeLabels(int var, String name, String desc, Label start, Label end) {
			this.var = var;
			this.name = name;
			this.desc = desc;
			this.start = start;
			this.end = end;
		}
	}
	
	private static class TryCatchBlock {
		final Label start;
		final Label handler;
		
		public TryCatchBlock(Label start, Label handler) {
			this.start = start;
			this.handler = handler;
		}
	}
	
}
