package hu.advancedweb.scott.instrumentation.transformation;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Opcodes and descriptions for each variable type.
 * @author David Csakvari
 */
enum VariableType {
	INTEGER(Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN, "I"),
	LONG(Opcodes.LLOAD, Opcodes.LSTORE, Opcodes.LRETURN, "J"),
	FLOAT(Opcodes.FLOAD, Opcodes.FSTORE, Opcodes.FRETURN, "F"),
	DOUBLE(Opcodes.DLOAD, Opcodes.DSTORE, Opcodes.DRETURN, "D"),
	REFERENCE(Opcodes.ALOAD, Opcodes.ASTORE, Opcodes.ARETURN, "Ljava/lang/Object;"),
	SHORT(Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN, "S"),
	BYTE(Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN, "B"),
	CHAR(Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN, "C"),
	BOOLEAN(Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN, "Z"),
	VOID(-1, -1, Opcodes.RETURN, "V")
	;

	final int loadOpcode;
	final int storeOpcode;
	final int returnOpcode;
	final String desc;
	
	VariableType(int loadOpcode, int storeOpcode, int returnOpcode, String desc) {
		this.loadOpcode = loadOpcode;
		this.storeOpcode = storeOpcode;
		this.returnOpcode = returnOpcode;
		this.desc = desc;
	}
	
	static boolean isStoreOperation(final int opcode) {
		for (VariableType variableType : VariableType.values()) {
			if (variableType.storeOpcode == opcode) {
				return true;
			}
		}
		return false;
	}
	
	static boolean isReturnOperation(final int opcode) {
		for (VariableType variableType : VariableType.values()) {
			if (variableType.returnOpcode == opcode) {
				return true;
			}
		}
		return false;
	}
	
	static VariableType getByReturnOpCode(final int opcode) {
		for (VariableType variableType : VariableType.values()) {
			if (variableType.returnOpcode == opcode) {
				return variableType;
			}
		}
		return null;
	}
	
	static VariableType getReturnTypeFromMethodDesc(final String methodDesc) {
		return getByDesc(Type.getReturnType(methodDesc).getDescriptor());
	}
	
	static VariableType getByDesc(final String desc) {
		if (desc.startsWith("L") || desc.startsWith("[")) {
			return VariableType.REFERENCE;
		} else {
			for (VariableType variableType : VariableType.values()) {
				if (variableType.desc.equals(desc)) {
					return variableType;
				}
			}
		}
		return null;
	}
}
