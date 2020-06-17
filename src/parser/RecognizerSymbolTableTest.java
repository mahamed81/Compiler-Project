package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecognizerSymbolTableTest {

	@Test
	public void test() {


		// The variableT hold the strings for the Pascal Language . 

		String variableT = 
				"program pro ; "						
						+ "var yo, yu, yea : integer ; "											
						+ "function fun : real ; "						
						+ "var vu : real ; "							
						+ "begin "																													
						+ "vu"															
						+ ":= "																																		
						+ "3 "																			 												
						+ "end "										
						+ "; "									
						+ "begin end"					
						+ ".";					

		Recognizer variable = new Recognizer(variableT, false);

		try
		{
			variable.program();
		}
		catch(Exception e)
		{
			fail(e.getMessage());
		}

		//Printing out Variable Test's Symbol Table
		System.out.println(variable.getSymbolTableToString());


		// The procedureT hold the strings for the Pascal Language .
		String procedureT = 
				"program pro ; "						
						+ "var yo, yu, yea : integer ; "																										
						+ "procedure pro ; "																							
						+ "begin "																											 
						+ "pro "																																		
						+ "end "											
						+ "; "										
						+ "begin end"								
						+ ".";											

		Recognizer procedure = new Recognizer(procedureT, false);

		try
		{
			procedure.program();
			System.out.println("String matches Pascal Language.");
		}
		catch(Exception e)
		{

			fail(e.getMessage());
		}

		//Printing out Variable Test's Symbol Table
		System.out.println(procedure.getSymbolTableToString());

	}

}


