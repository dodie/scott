package hu.advancedweb.example;

public class Calculator {
	
	private Mode mode = Mode.INPUT;
	private int lastInput;
	private int result;
	
	private enum Mode {
		INPUT, ADD, MULTIPLY
	}
	
	public void enter(int number) {
		
		if (mode == Mode.INPUT) {
			this.result = number;
			this.lastInput = number;
		} else if (mode == Mode.ADD) {
			this.result = number + result;
			this.lastInput = number;
		} else if (mode == Mode.MULTIPLY) {
			this.result = number * result;
			this.lastInput = number;
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	public void pressPlus() {
		mode = Mode.ADD;
	}
	
	public void pressMultiply() {
		mode = Mode.MULTIPLY;
	}
	
	public void pressEnter() {
		enter(lastInput);
	}
	
	public int getResult() {
		return result;
	}
	
}
