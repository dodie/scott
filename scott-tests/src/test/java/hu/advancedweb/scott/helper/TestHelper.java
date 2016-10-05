package hu.advancedweb.scott.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.advancedweb.scott.runtime.track.LocalVariableState;
import hu.advancedweb.scott.runtime.track.LocalVariableStateRegistry;

public class TestHelper {
	
	public static String getLastRecordedStateFor(String variableName) {
		List<LocalVariableState> states = new ArrayList<LocalVariableState>(LocalVariableStateRegistry.getLocalVariableStates());
		Collections.reverse(states);
		
		for (LocalVariableState localVariableState : states) {
			String nameOfLocalVariableState = LocalVariableStateRegistry.getLocalVariableName(localVariableState.key, localVariableState.lineNumber);
			if (nameOfLocalVariableState.equals(variableName)) {
				return localVariableState.value;
			}
		}
		
		return null;
	}

}
