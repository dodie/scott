package hu.advancedweb.scott.instrumentation.transformation;

import org.objectweb.asm.Opcodes;

/**
 * Opcodes and descriptions for each variable type.
 * @author David Csakvari
 */
enum VariableType {
	INTEGER(Opcodes.ILOAD, Opcodes.ISTORE, "I"),
	LONG(Opcodes.LLOAD, Opcodes.LSTORE, "J"),
	FLOAT(Opcodes.FLOAD, Opcodes.FSTORE, "F"),
	DOUBLE(Opcodes.DLOAD, Opcodes.DSTORE, "D"),
	REFERENCE(Opcodes.ALOAD, Opcodes.ASTORE, "Ljava/lang/Object;"),
	SHORT(Opcodes.ILOAD, Opcodes.ISTORE, "S"),
	BYTE(Opcodes.ILOAD, Opcodes.ISTORE, "B"),
	CHAR(Opcodes.ILOAD, Opcodes.ISTORE, "C"),
	BOOLEAN(Opcodes.ILOAD, Opcodes.ISTORE, "Z")
	;

	final int loadOpcode;
	final int storeOpcode;
	final String desc;
	
	VariableType(int loadOpcode, int storeOpcode, String desc) {
		this.loadOpcode = loadOpcode;
		this.storeOpcode = storeOpcode;
		this.desc = desc;
	}
	
	static VariableType getByStoreOpCode(final int opcode) {
		for (int i = 0; i < VariableType.values().length; i++) {
			VariableType variableType = VariableType.values()[i];
			if (variableType.storeOpcode == opcode) {
				return variableType;
			}
		}
		return null;
	}
	
	static boolean isStoreOperation(final int opcode) {
		for (int i = 0; i < VariableType.values().length; i++) {
			VariableType variableType = VariableType.values()[i];
			if (variableType.storeOpcode == opcode) {
				return true;
			}
		}
		return false;
	}
	
	static VariableType getByDesc(final String desc) {
		if (desc.startsWith("L") || desc.startsWith("[")) {
			return VariableType.REFERENCE;
		} else {
			for (int i = 0; i < VariableType.values().length; i++) {
				VariableType variableType = VariableType.values()[i];
				if (variableType.desc.equals(desc)) {
					return variableType;
				}
			}
		}
		return null;
	}
}
