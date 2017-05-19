package hu.advancedweb.example;

public class Counter {
	
	int state;
	
	public int get() {
		return state;
	}
	
	public void increase() {
		state++;
	}
	
	public void decrease() {
		state--;
	}

	@Override
	public String toString() {
		return "Counter [state=" + state + "]";
	}
	
}
