package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecognizerTest {
	
	
	
	
	/**
	 * Testing the program() method 
	 * The first test should have no error
	 * The second test, an error the COLON should cause an error,
	 * 
	 */

	@Test
	public void testProgram() {
		
		String test = "program foo ; begin end .";
		System.out.println("---with \"" + test + "\" good ");
		Recognizer p = new Recognizer(test, false);
		
		try {
			p.program();
		}
		catch(Exception e) {
			fail(e.getMessage());		
		}
		
		test = "program foo : begin end .";
		System.out.println("---with \"" + test + "\" error expect ");
		p = new Recognizer(test, false);
		try {
			p.program();	
			fail("this pascal should fail");
		}
		catch(Exception e) {
			assertEquals(null, e.getMessage());	
		}	
		
	}
	
	
	/**
	 * Testing the declarations() method 
	 * For the first test, there should be no error.
	 * For the second test,it should throw an error 
	 */

	@Test
	public void testDeclarations() {
		
		String test = "var hey : real ; var ho: integer;";
		System.out.println("---with \"" + test + "\" good");
		Recognizer p = new Recognizer(test, false);
		
		try {
			p.declarations();
		}
		catch(Exception e) {
			fail(e.getMessage());		
		}

		test = "var fio : fun ;";
		System.out.println("---with \"" + test + "\" error expect ");
		p = new Recognizer(test, false);
		
		try {
			p.declarations();	
			fail("this pascal should fail");
		}
		catch(Exception e) {
			assertEquals(null, e.getMessage());	
		}	
		
	}

	/**
	 * Testing the subprogram_declaration() method 
	 * For the first test, there should be no errors.
	 * For the second test, we have VAR instead of COLON it should throw an error.
	 */
	
	
	@Test
	public void testSubprogram_declaration() {
		
		String test = "procedure ni ; var hi : integer ; begin end";
		System.out.println("---with \"" + test + "\" good");
		Recognizer p = new Recognizer(test, false);
		
		try {
			p.subprogram_declaration();
		}
		catch(Exception e) {
			fail(e.getMessage());		
		}

		test = "function foo var: integer ;";
		System.out.println("---with \"" + test + "\" expect errors");
		p = new Recognizer(test, false);
		
		try {
			p.subprogram_declaration();
			fail("this pascal should fail");
		}
		catch(Exception e) {
			assertEquals(null, e.getMessage());	
		}	
	}
	
	/**
	 * Testing the statement() method 
	 * For the first test, there should be no errors.
	 * For second test, we have an  EQUAL sign instead of an ASSIGN: error should be display
	 */

	@Test
	public void testStatement() {
		
		String test = "foo := -fun * 4";
		System.out.println("---with \"" + test + "\"good");
		Recognizer p = new Recognizer(test, false);
		
		try {
			p.statement();
		}
		catch(Exception e) {
			fail(e.getMessage());		
		}

		test = "while hey do hi = hi + 1";
		System.out.println("---with \"" + test + "\" expect errors");
		p = new Recognizer(test, false);
		p.statement();

		
		try {
			p.statement();
			fail("this pascal should fail");
		}
		catch(Exception e) {
			assertEquals(null, e.getMessage());	
		}	
		
	}
	/**
	 * Testing the simple_expression() method
	 * For first test, there should be no error.
	 * For the second test, we have + followed by a -, should result in error.
	 */
	
	@Test
	public void testSimple_expression() {
	
		String test = "film * 2 + fii";
		System.out.println("---with \"" + test + "\" good");
		Recognizer p = new Recognizer(test, false);
		
		try {
			p.simple_expression();
		}
		catch(Exception e) {
			fail(e.getMessage());		
		}

		test = "29 + -";
		System.out.println("---with \"" + test + "\" expect errors");
		p = new Recognizer(test, false);
		
		try {
			p.simple_expression();
			fail("this pascal should fail");
		}
		catch(Exception e) {
			assertEquals(null, e.getMessage());	
		}		
	}

	
	/**
	 * Testing the factor() method 
	 * For the first test, there should be no error.
	 * For the second test, there are two open brackets which should result an error.
	 */
	
	@Test
	public void testFactor() {
		
		String test = "10";
		System.out.println("---with \"" + test + "\"good");
		Recognizer p = new Recognizer(test, false);
		
		try {
			p.factor();		}
		catch(Exception e) {
			fail(e.getMessage());		
		}

		test = "[[";
		System.out.println("---with \"" + test + "\" expect errors");
		p = new Recognizer(test, false);
		
		try {
			p.factor();
			fail("this pascal should fail");
		}
		catch(Exception e) {
			assertEquals(null, e.getMessage());	
		}		
		
		
	}
	
	
	

}
