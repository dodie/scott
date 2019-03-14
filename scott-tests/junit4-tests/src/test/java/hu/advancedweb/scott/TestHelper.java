package hu.advancedweb.scott;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.advancedweb.scott.runtime.track.ExceptionData;
import hu.advancedweb.scott.runtime.track.ReturnData;
import hu.advancedweb.scott.runtime.track.ReturnValueData;
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
	
	static boolean containsReturnData(String value) {
		List<ReturnData> returnDatas = new ArrayList<ReturnData>(StateRegistry.getRetrunValues());
		Collections.reverse(returnDatas);
		
		for (ReturnData returnData : returnDatas) {
			if (returnData instanceof ReturnValueData) {
				if (value.equals(((ReturnValueData) returnData).value)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	static boolean containsReturn() {
		List<ReturnData> returnDatas = new ArrayList<ReturnData>(StateRegistry.getRetrunValues());
		Collections.reverse(returnDatas);
		
		for (ReturnData returnData : returnDatas) {
			if (!(returnData instanceof ReturnValueData)) {
				return true;
			}
		}
		
		return false;
	}
	
	static boolean containsUnhandledException(Class<? extends Throwable> clazz, String message) {
		List<ExceptionData> exceptions = new ArrayList<ExceptionData>(StateRegistry.getUnhandledExceptions());
		Collections.reverse(exceptions);
		
		for (ExceptionData exception : exceptions) {
			if (clazz.isInstance(exception.throwable) && message.equals(exception.throwable.getMessage())) {
				return true;
			}
		}
		
		return false;
	}

	public static String wrapped(String original) {
		char wrappingChar = '"';
		return new StringBuilder().append(wrappingChar).append(original).append(wrappingChar).toString();
	}


}
