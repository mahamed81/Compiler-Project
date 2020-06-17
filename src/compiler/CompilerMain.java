package compiler;

import parser.Parser;
import scanner.TokenType;
import syntaxtree.*;


/**
 * test reading file and parsing it, and printing symboltable file
 * 
 *
 */

public class CompilerMain {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Error No file.");
			System.exit(1);
		}

		String filename = args[0]; // folder

		Parser parser;

		try {
			parser = new Parser(filename, true); // Initialize the parser using the constructor.
			ProgramNode pNode = parser.program(); // Call program() on the parser.
			parser.writeoutput(filename); // Print out a test.symboltable file to the directory of the project.

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}