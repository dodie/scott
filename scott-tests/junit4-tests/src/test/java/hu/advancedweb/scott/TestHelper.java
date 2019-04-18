package hu.advancedweb.scott;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.advancedweb.scott.runtime.track.StateData;
import hu.advancedweb.scott.runtime.track.StateRegistry;

class TestHelper {
	
	static String getLastRecordedStateForVariable(String variableName) {
		List<StateData> states = new ArrayList<StateData>(StateRegistry.getLocalVariableStates());
		Collections.reverse(states);
		
		for (StateData localVariableState : states) {
			if (localVariableState.name.equals(variableName)) {
				return localVariableState.value;
			}
		}
		
		return null;
	}
	
	public static String getLastRecordedStateForField(String fieldName) {
		List<StateData> states = new ArrayList<StateData>(StateRegistry.getFieldStates());
		Collections.reverse(states);
		
		for (StateData localVariableState : states) {
			if (localVariableState.name.equals(fieldName)) {
				return localVariableState.value;
			}
		}
		
		return null;
	}
	
	public static String wrapped(String original) {
		char wrappingChar = '"';
		return new StringBuilder().append(wrappingChar).append(original).append(wrappingChar).toString();
	}


}
