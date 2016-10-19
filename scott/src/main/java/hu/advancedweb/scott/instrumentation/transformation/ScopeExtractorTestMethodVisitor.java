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

/**
 * Tracks local variable scopes and field accesses in test methods
 * and passes this information to the next visitor.
 * 
 * @author David Csakvari
 */
public class ScopeExtractorTestMethodVisitor extends MethodNode {
	
	private TreeMap<Integer, Label> lines = new TreeMap<>();
	
	private List<LocalVariableScopeLabels> scopes = new ArrayList<>();
	
	private Set<AccessedField> accessedFields = new LinkedHashSet<>();
	
	private Set<TryCatchBlockLabels> tryCatchBlocks = new HashSet<>();
	
	private StateEmitterTestMethodVisitor next;
	
	private String className;
	
	public ScopeExtractorTestMethodVisitor(StateEmitterTestMethodVisitor next, final int access, final String name, final String desc, final String signature, final String[] exceptions, String className) {
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
		tryCatchBlocks.add(new TryCatchBlockLabels(start, handler));
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
		List<LocalVariableScope> localVariableScopes = new ArrayList<>();
		for (LocalVariableScopeLabels range : scopes) {
			if (range.name.equals("this")) {
				continue;
			}
			
			localVariableScopes.add(calculateScope(range));
		}
		next.setLocalVariableScopes(localVariableScopes);
		next.setAccessedFields(accessedFields);
		accept(next);
	}
	
	/**
	 * Calculate the start and end line numbers for variable scopes.
	 * If the LocalVariableScope start line is 0, then it is an input parameter,
	 * as it's scope start label appeared before the method body.
	 */
	private LocalVariableScope calculateScope(LocalVariableScopeLabels range) {
		int prevLine = 0;
		int startLine = lines.firstKey();
		int endLine = lines.lastKey();
		
		TryCatchBlockLabels inTry = null;
		Stack<TryCatchBlockLabels> tryCatchBlockScopes = new Stack<>();
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
			for (TryCatchBlockLabels tryCatchBlock : tryCatchBlocks) {
				if (label == tryCatchBlock.start) {
					tryCatchBlockScopes.push(tryCatchBlock);
				} else if (label == tryCatchBlock.handler) {
					tryCatchBlockScopes.pop();
				}
			}
			
			prevLine = entry.getKey();
		}
		
		final LocalVariableScope localScope;
		if (startLine <= endLine) {
			localScope = new LocalVariableScope(range.var, range.name, VariableType.getByDesc(range.desc), startLine, endLine);
		} else {
			// Sometimes the end label is for an earlier line number than the start label, see Issue #17.
			localScope = new LocalVariableScope(range.var, range.name, VariableType.getByDesc(range.desc), endLine, startLine);
		}
		
		return localScope;
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
	
	private static class TryCatchBlockLabels {
		final Label start;
		final Label handler;
		
		public TryCatchBlockLabels(Label start, Label handler) {
			this.start = start;
			this.handler = handler;
		}
	}
	
}
