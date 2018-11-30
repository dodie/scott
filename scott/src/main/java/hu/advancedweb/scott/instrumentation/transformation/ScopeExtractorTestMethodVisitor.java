package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Tracks local variable scopes and field accesses in test methods and passes
 * this information to the next visitor.
 * 
 * @author David Csakvari
 */
public class ScopeExtractorTestMethodVisitor extends MethodNode {

	private List<Label> labels = new ArrayList<>();

	private TreeMap<Integer, Label> lines = new TreeMap<>();

	private List<LocalVariableScopeData> scopes = new ArrayList<>();

	private Set<AccessedField> accessedFields = new LinkedHashSet<>();

	private Set<TryCatchBlockLabels> tryCatchBlocks = new HashSet<>();

	private StateEmitterTestMethodVisitor next;

	private int lineNumber;

	private Map<Integer, Integer> lineNumerToFirstOccurrenceOfVariables;
	private Map<Integer, List<Label>> varAccesses;

	public ScopeExtractorTestMethodVisitor(StateEmitterTestMethodVisitor next, final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		super(Opcodes.ASM5, access, name, desc, signature, exceptions);
		this.next = next;
		lineNumerToFirstOccurrenceOfVariables = new HashMap<>();
		varAccesses = new HashMap<>();
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		reset();
		return super.visitAnnotation(desc, visible);
	}

	private void reset() {
		labels.clear();
		scopes.clear();
		accessedFields.clear();
		lines.clear();
		tryCatchBlocks.clear();
	}

	@Override
	public void visitLabel(Label label) {
		super.visitLabel(label);
		labels.add(label);
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		super.visitTryCatchBlock(start, end, handler, type);
		tryCatchBlocks.add(new TryCatchBlockLabels(start, handler));
	}

	@Override
	public void visitLineNumber(int lineNumber, Label start) {
		this.lineNumber = lineNumber;
		super.visitLineNumber(lineNumber, start);
		lines.put(lineNumber, start);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		if (VariableType.isStoreOperation(opcode)) {
			lineNumerToFirstOccurrenceOfVariables.putIfAbsent(var, this.lineNumber);
			varAccesses.putIfAbsent(var, new ArrayList<Label>());
			varAccesses.get(var).add(this.labels.get(this.labels.size() - 1));
		}
		super.visitVarInsn(opcode, var);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
		scopes.add(new LocalVariableScopeData(index, name, desc, new LocalVariableScopeData.Labels(start, end)));
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

		if (name.startsWith("this$")) {
			return;
		}

		accessedFields.add(new AccessedField(owner, name, desc, isStatic));
	}

	@Override
	public void visitEnd() {
		List<LocalVariableScope> localVariableScopes = new ArrayList<>();
		for (LocalVariableScopeData scope : scopes) {
			if (scope.name.equals("this")) {
				continue;
			}
			localVariableScopes.add(calculateScope(scope));
		}
		next.setLocalVariableScopes(localVariableScopes);
		next.setAccessedFields(accessedFields);

		accept(next);
	}

	/**
	 * Calculate the start and end line numbers for variable scopes. If the
	 * LocalVariableScope start line is 0, then it is an input parameter, as
	 * it's scope start label appeared before the method body.
	 */
	private LocalVariableScope calculateScope(LocalVariableScopeData scope) {
		final TryCatchBlockLabels enclosingTry = getEnclosingTry(scope);
		final Label start = scope.labels.start;
		final Label end = getTryFixedEndLabel(scope, enclosingTry);
		
		int startIndex = getIndex(start);
		int endIndex = getIndex(end);
		
		LocalVariableScopeData.LineNumbers scopeWithLineNumbers = getLineNumbers(scope, enclosingTry, start, end);
		
		return new LocalVariableScope(scope.var, scope.name, VariableType.getByDesc(scope.desc), scopeWithLineNumbers.startLine, scopeWithLineNumbers.endLine, startIndex, endIndex, getVarReferencesBeforeStart(scope));
	}

	private LocalVariableScopeData.LineNumbers getLineNumbers(LocalVariableScopeData scope, final TryCatchBlockLabels enclosingTry, final Label start, final Label end) {
		int startLine = lines.firstKey();
		int endLine = lines.lastKey();
		int prevLine = 0;
		for (Map.Entry<Integer, Label> entry : lines.entrySet()) {
			Label label = entry.getValue();

			if (label == start) {
				startLine = prevLine;
			} else if (enclosingTry == null && label == end) {
				endLine = prevLine;
			} else if (enclosingTry != null && label == enclosingTry.handler) {
				endLine = prevLine;
			}
			prevLine = entry.getKey();
		}

		if (startLine > endLine) {
			/* 
			 * Sometimes the end label is for an earlier line number than the 
			 * start label, see Issue #17.
			 */
			int tmp = startLine;
			startLine = endLine;
			endLine = tmp;
		}

		if (lineNumerToFirstOccurrenceOfVariables.containsKey(scope.var)) {
			if (startLine < lineNumerToFirstOccurrenceOfVariables.get(scope.var)) {
				/*
				 * For variables in nested scopes the start Label sometimes
				 * points to an earlier line, e.g. to the start of the method.
				 * In these cases the start line of the scope has to be
				 * corrected.
				 */
				startLine = lineNumerToFirstOccurrenceOfVariables.get(scope.var);
			}
		}
		
		return new LocalVariableScopeData.LineNumbers(startLine, endLine);
	}

	/**
	 * The initially recorded variable scopes in try blocks has wrong end line numbers.
	 * They are pointing to the end of the catch blocks, even if they were declared in
	 * the try block. This method calculates the correct end Label for a variable scope.
	 * Fix for issue #14.
	 */
	private Label getTryFixedEndLabel(LocalVariableScopeData scope, TryCatchBlockLabels enclosingTry) {
		if (enclosingTry == null) {
			return scope.labels.end;
		} else {
			if (getIndex(enclosingTry.handler) < getIndex(scope.labels.end)) {
				return enclosingTry.handler;
			} else {
				return scope.labels.end;
			}
		}
	}

	private List<Integer> getVarReferencesBeforeStart(LocalVariableScopeData scope) {
		List<Integer> prev = new ArrayList<>();
		for (Label label : labels) {
			if (label == scope.labels.start) {
				break;
			}
			if (varAccesses.get(scope.var).contains(label)) {
				prev.add(getIndex(label));
			}
		}

		return prev;
	}
	
	private int getIndex(Label label) {
		int index = labels.indexOf(label);
		if (index == -1) {
			return Integer.MAX_VALUE;
		}
		return index;
	}

	private TryCatchBlockLabels getEnclosingTry(LocalVariableScopeData scope) {
		Stack<TryCatchBlockLabels> tryCatchBlockScopes = new Stack<>();
		for (int i = 0; i < labels.size(); i++) {
			Label label = labels.get(i);

			if (label == scope.labels.start) {
				/*
				 * This check must be done first.
				 * Sometimes, like in WeirdFormattingTest.simpleFormattingWithTryCatch,
				 * If a variable declaration happens right before a try block,
				 * they will be under the same label.
				 */
				if (tryCatchBlockScopes.empty()) {
					return null;
				} else {
					return tryCatchBlockScopes.peek();
				}
			}

			for (TryCatchBlockLabels tryCatchBlock : tryCatchBlocks) {
				if (label == tryCatchBlock.start) {
					tryCatchBlockScopes.push(tryCatchBlock);
				}
			}

			for (TryCatchBlockLabels tryCatchBlock : tryCatchBlocks) {
				if (label == tryCatchBlock.handler) {
					tryCatchBlockScopes.pop();
				}
			}
		}

		return null;
	}
	
	private static class LocalVariableScopeData {
		final int var;
		final String name;
		final String desc;
		final Labels labels;

		public LocalVariableScopeData(int var, String name, String desc, Labels labels) {
			this.var = var;
			this.name = name;
			this.desc = desc;
			this.labels = labels;
		}
		
		private static class Labels {
			final Label start;
			final Label end;
			
			public Labels(Label start, Label end) {
				this.start = start;
				this.end = end;
			}
		}
		
		private static class LineNumbers {
			final int startLine;
			final int endLine;
			
			public LineNumbers(int startLine, int endLine) {
				this.startLine = startLine;
				this.endLine = endLine;
			}
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
