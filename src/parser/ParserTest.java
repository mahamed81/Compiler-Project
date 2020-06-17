package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	/**
	 * testing the program function for
	 * the input: program foo; begin end.
	 */

	@Test
	public void testProgram() {
		System.out.println("Test program");
		String input = "program foo: begin end. ";
		System.out.println("Token to be parsed " + input);
		Parser instance = new Parser(input, false);
		instance.program();
		System.out.println("Program test : It Parsed!\n");
	}
	
	/**
	 * testing the declaration function for
	 * the input: var fee: integer.
	 */
	
	@Test
	public void testDeclaration() {
		System.out.println("Test Declaration");
		String input = "var fee: integer; ";
		System.out.println("Token to be parsed " + input);
		Parser instance = new Parser(input, false);
		instance.declarations();
		System.out.println("Declaration test : It Parsed!\n");

	}
	
	
	/**
	 * This test will be testing the subprogram_declaration function for
	 * the varieties of inputs as listed below.
	 */
	@Test
	public void testSubprogram_Declaration() {
		System.out.println("test Subprogram_Declaration");
		String input = "function doSomething(fee: integer): integer; " + "var fo: integer; " + "begin " + "fo := 2*fee "
				+ "end";
		System.out.println("String to be parsed: " + input);
		Parser instance = new Parser(input, false);
		instance.subprogram_declaration();
		System.out.println("subprogram_declaration test : It Parsed!\n");

		input = "function doSomething(fee, fi: integer): integer; " + "var fo: integer; "
				+ "function doSomethingElse: integer; " + "begin " + "fo := fee + fi; " + "fo := 2 " + "end " + "; "
				+ "begin " + "fo := 2 * fo " + "end";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.subprogram_declaration();
		System.out.println("It Parsed!\n");
	}
	

	/**
	 * testing the simple_expression functions
	 * for different inputs 
	 */
	@Test
	public void testSimple_expression() {
		System.out.println("test simple_expression");
		String input = "foo + fi";
		System.out.println("String to be parsed: " + input);
		Parser instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("simple_expression test : It Parsed!\n");

		input = "-20";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("simple_expression : It Parsed!\n");


		input = "(fi+foo)";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("simple_expression : It Parsed!\n");

		input = "(3+4)";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("simple_expression : It Parsed!\n");

	}
	/**
	 * testing the factor function for
	 * numerous inputs as listed below. 
	 */
	@Test
	public void Testfactor() {
		
		System.out.println("Test factor");
		String input = "foo";
		System.out.println("String to be parsed: " + input);
		Parser instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("factor : It Parsed!\n");

		input = "5";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("factor : It Parsed!\n");

		input = "not foo";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("factor : It Parsed!\n");

		input = "(3+4)";
		System.out.println("String to be parsed: " + input);
		instance = new Parser(input, false);
		instance.simple_expression();
		System.out.println("factor : It Parsed!\n");

	
	}
	
	
	
	
}
