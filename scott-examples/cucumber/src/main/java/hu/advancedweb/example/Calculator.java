package hu.advancedweb.example;

import java.util.Deque;
import java.util.LinkedList;

public class Calculator {

	private final Deque<String> stack = new LinkedList<String>();

	public void push(String arg) {
		stack.add(arg);
	}

	public String evaluate() {

		String op = stack.removeLast();
		Integer x = Integer.parseInt(stack.removeLast());
		Integer y = Integer.parseInt(stack.removeLast());

		Integer val;
		if (op.equals("-")) {
			val = x - y;
		} else if (op.equals("+")) {
			val = x + y;
		} else if (op.equals("*")) {
			val = x * y;
		} else if (op.equals("/")) {
			val = x / y;
		} else {
			throw new IllegalArgumentException();
		}

		stack.push(Integer.toString(val));

		return stack.getLast();
	}

	@Override
	public String toString() {
		return "[stack=" + stack + "]";
	}

}
