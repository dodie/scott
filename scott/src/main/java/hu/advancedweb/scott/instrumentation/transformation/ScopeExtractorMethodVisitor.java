package hu.advancedweb.scott.instrumentation.transformation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class ScopeExtractorMethodVisitor extends MethodNode {

	private List<Label> labels = new ArrayList<>();

	private TreeMap<Integer, Label> lines = new TreeMap<>();

	private List<LocalVariableScopeData> scopes = new ArrayList<>();

	private Set<AccessedField> accessedFields = new LinkedHashSet<>();

	private Set<TryCatchBlockLabels> tryCatchFinallyBlocks = new HashSet<>();

	private StateTrackingMethodVisitor next;

	private int lineNumber;
	
	private Integer methodStartLineNumber;

	private Map<VarAndStoreOpcode, Integer> lineNumberToFirstOccurrenceOfVariables;
	
	private Map<VarAndStoreOpcode, List<Label>> varAccesses;
	
	ScopeExtractorMethodVisitor(StateTrackingMethodVisitor next, final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		super(Opcodes.ASM7, access, name, desc, signature, exceptions);
		this.next = next;
		lineNumberToFirstOccurrenceOfVariables = new HashMap<>();
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
		tryCatchFinallyBlocks.clear();
	}

	@Override
	public void visitLabel(Label label) {
		super.visitLabel(label);
		labels.add(label);
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		super.visitTryCatchBlock(start, end, handler, type);
		tryCatchFinallyBlocks.add(new TryCatchBlockLabels(start, handler));
	}

	@Override
	public void visitLineNumber(int lineNumber, Label start) {
		this.lineNumber = lineNumber;
		if (this.methodStartLineNumber == null) {
			methodStartLineNumber = lineNumber;
		}
		super.visitLineNumber(lineNumber, start);
		lines.put(lineNumber, start);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		if (VariableType.isStoreOperation(opcode)) {
			putIfAbsent(lineNumberToFirstOccurrenceOfVariables, new VarAndStoreOpcode(var, opcode), this.lineNumber);
			putIfAbsent(varAccesses, new VarAndStoreOpcode(var, opcode), new ArrayList<Label>());
			
			// In some cases a single visitVarInsn happens before the first visitLabel. See Issue #57.
			if (!this.labels.isEmpty()) {
				varAccesses.get(new VarAndStoreOpcode(var, opcode)).add(this.labels.get(this.labels.size() - 1));
			}
		}
		super.visitVarInsn(opcode, var);
	}
	
	private static <K, V> void putIfAbsent(Map<K, V> map, K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
        }
    }

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int var) {
		super.visitLocalVariable(name, desc, signature, start, end, var);
		scopes.add(new LocalVariableScopeData(var, name, desc, new LocalVariableScopeData.Labels(start, end)));
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);

		final boolean isStatic = Opcodes.GETSTATIC == opcode || Opcodes.PUTSTATIC == opcode;

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
		next.setMethodStartLineNumber(methodStartLineNumber);
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
		
		VarAndStoreOpcode varAndStoreOpcode = new VarAndStoreOpcode(scope.var, VariableType.getByDesc(scope.desc).storeOpcode);
		// the lookup in lineNumberToFirstOccurrenceOfVariables has to be null-safe, because in some cases a single visitVarInsn happens before the first visitLabel. See Issue #57.
		if (lineNumberToFirstOccurrenceOfVariables.containsKey(varAndStoreOpcode) && startLine < lineNumberToFirstOccurrenceOfVariables.get(varAndStoreOpcode)) {
			/*
			 * For variables in nested scopes the start Label sometimes
			 * points to an earlier line, e.g. to the start of the method.
			 * In these cases the start line of the scope has to be
			 * corrected.
			 */
			startLine = lineNumberToFirstOccurrenceOfVariables.get(varAndStoreOpcode);
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
			
			VarAndStoreOpcode varAndStoreOpcode = new VarAndStoreOpcode(scope.var, VariableType.getByDesc(scope.desc).storeOpcode);
			// the lookup in varAccesses has to be null-safe, because in some cases a single visitVarInsn happens before the first visitLabel. See Issue #57.
			if (varAccesses.containsKey(varAndStoreOpcode) && varAccesses.get(varAndStoreOpcode).contains(label)) {
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
		Deque<TryCatchBlockLabels> tryCatchBlockScopes = new LinkedList<>();
		for (Label label : labels) {
			if (label == scope.labels.start) {
				/*
				 * This check must be done first.
				 * Sometimes, like in WeirdFormattingTest.simpleFormattingWithTryCatch,
				 * If a variable declaration happens right before a try block,
				 * they will be under the same label.
				 */
				if (tryCatchBlockScopes.isEmpty()) {
					return null;
				} else {
					return tryCatchBlockScopes.peek();
				}
			}

			for (TryCatchBlockLabels tryCatchBlock : tryCatchFinallyBlocks) {
				if (label == tryCatchBlock.start) {
					tryCatchBlockScopes.push(tryCatchBlock);
				}
			}

			for (TryCatchBlockLabels tryCatchBlock : tryCatchFinallyBlocks) {
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

		LocalVariableScopeData(int var, String name, String desc, Labels labels) {
			this.var = var;
			this.name = name;
			this.desc = desc;
			this.labels = labels;
		}
		
		private static class Labels {
			final Label start;
			final Label end;
			
			Labels(Label start, Label end) {
				this.start = start;
				this.end = end;
			}
		}
		
		private static class LineNumbers {
			final int startLine;
			final int endLine;
			
			LineNumbers(int startLine, int endLine) {
				this.startLine = startLine;
				this.endLine = endLine;
			}
		}
	}
	
	private static class TryCatchBlockLabels {
		final Label start;
		final Label handler;

		TryCatchBlockLabels(Label start, Label handler) {
			this.start = start;
			this.handler = handler;
		}
	}
	
	private static class VarAndStoreOpcode {
		final int var;
		final int storeOp;
		
		public VarAndStoreOpcode(int var, int storeOp) {
			this.var = var;
			this.storeOp = storeOp;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + storeOp;
			result = prime * result + var;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			VarAndStoreOpcode other = (VarAndStoreOpcode) obj;
			if (storeOp != other.storeOp)
				return false;
			if (var != other.var)
				return false;
			return true;
		}
	}

}
