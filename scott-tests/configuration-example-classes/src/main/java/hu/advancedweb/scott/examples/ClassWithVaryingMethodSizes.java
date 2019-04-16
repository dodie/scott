package hu.advancedweb.scott.examples;

/**
 * Note that this file is sensitive to formatting.
 * 
 * @author David Csakvari
 */
public class ClassWithVaryingMethodSizes {

	public String methodWith1LineBodyInlineDeclaration() { return "Hello from methodWith1LineBodyInlineDeclaration"; }
	
	public String methodWith1LineBody() {
		return "Hello from methodWith1LineBody";
	}
	
	public String methodWith3LineBody() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hello from mediumMethod");
		return sb.toString();
	}
	
	public String methodWith9LineBody() {
		StringBuilder sb = new StringBuilder();
		sb.append("H");
		sb.append("e");
		sb.append("l");
		sb.append("l");
		sb.append("o");
		sb.append(" ");
		sb.append("from longMethod");
		return sb.toString();
	}

}
