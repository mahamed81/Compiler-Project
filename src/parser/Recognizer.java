package parser;
import symbolTable.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import scanner.*;


/**
 * The parser recognizes whether an input string of tokens
 * is an expression.
 * To use a parser, create an instance pointing at a file,
 * and then call the top-level function,exp().
 * If the functions returns without an error, the file
 * contains an acceptable expression.
 * @author Mahamed Ahmed 
 */
public class Recognizer {

	///////////////////////////////
	//    Instance Variables

	private Token lookahead;

	private MyScanner scanner;

	private SymbolTable symboltable;



	///////////////////////////////
	//       Constructors
	///////////////////////////////

	public Recognizer( String text, boolean isFilename) {

		symboltable = new SymbolTable();
		if( isFilename) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream("test");
			} catch (FileNotFoundException ex) {
				error( "No file");
			}
			InputStreamReader isr = new InputStreamReader( fis);
			scanner = new MyScanner( isr);

		}
		else {
			scanner = new MyScanner( new StringReader( text));
		}
		try {
			lookahead = scanner.nextToken();
		} catch (IOException ex) {
			error( "Scan error");
		}

	}

	///////////////////////////////
	//       Methods
	///////////////////////////////

	/**
	 * Executes the rule for program; terminal symbol in the expression grammar.
	 */

	public void program() {
		match(TokenType.PROGRAM);

		match(TokenType.ID);

		match(TokenType.SEMI);
		/*
		 * add to the symbol table if it's an ID token
		 */
		symboltable.add(lookahead.getLexeme(), Kind.PROGRAM);
		declarations();
		subprogram_declarations();

		compound_statement();

		match(TokenType.PERIOD);	

	}

	/**
	 * Executes the rule for the identifier_list non-terminal symbol in the
	 * expression grammar.
	 */

	public void identifer_list() {
		if(lookahead.getType() == TokenType.ID) {
			match(TokenType.ID);
		}
		else {
			match(TokenType.ID);
			match(TokenType.COMMA);
			identifer_list();
		}
	}

	/**
	 * Executes the rule for the declarations non-terminal symbol in the expression
	 * grammar.
	 */

	public void declarations() {
		if (this.lookahead.getType() == TokenType.VAR) {
			match(TokenType.VAR);
			identifer_list();
			match(TokenType.COLON);
			type();
			match(TokenType.SEMI);
			/*
			 * add to the symbol table if it's an ID token
			 */

			symboltable.add(lookahead.getLexeme(), Kind.VARIABLE);

			declarations();
		} else {
			// lambda
		}
	}


	/**
	 * Executes the rule for the type non-terminal symbol in the expression grammar.
	 */
	public void type() {

		standard_type();
		if (lookahead != null && lookahead.getType() == TokenType.ARRAY) {
			match(TokenType.ARRAY);
			match(TokenType.LEFTBRACKET);
			if (lookahead.getType() == TokenType.INTEGER) {
				match(TokenType.INTEGER);
			} else {
				match(TokenType.REAL);
			}
			match(TokenType.COLON);
			if (lookahead.getType() == TokenType.INTEGER) {
				match(TokenType.INTEGER);
			} else {
				match(TokenType.REAL);
			}
			match(TokenType.RIGHTBRACKET);
			match(TokenType.OF);
		}	
	}

	/**
	 * Executes the rule for the standard_type non-terminal symbol in the expression
	 * grammar.
	 */

	public void standard_type() {

		if (lookahead != null && lookahead.getType() == TokenType.INTEGER) {
			match(TokenType.INTEGER);

		} else if (lookahead != null && lookahead.getType() == TokenType.REAL) {
			match(TokenType.REAL);
		} else {
			error("standard_type");

		}
	}

	/**
	 * Executes the rule for the subprogram_declaration non-terminal symbol in
	 * the expression grammar.
	 */
	public void subprogram_declaration() {
		subprogram_head();
		declarations();
		compound_statement();
	}

	/**
	 * Executes the rule for the subprogram_declarations non-terminal symbol in
	 * the expression grammar.
	 */
	public void subprogram_declarations() {
		if(lookahead.getType() == TokenType.FUNCTION || lookahead.getType() == TokenType.PROCEDURE) {
			subprogram_declaration();
			match(TokenType.SEMI);
			subprogram_declarations();
		}
		else {
			//lamda
		}
	}

	/**
	 * Executes the rule for the subprogram_head non-terminal symbol in
	 * the expression grammar.
	 */
	public void subprogram_head() {
		if(lookahead.getType() == TokenType.FUNCTION)
		{
			match(TokenType.FUNCTION);
			match(TokenType.ID);
			arguments();
			match(TokenType.COLON);
			standard_type();
			match(TokenType.SEMI);
			/*
			 * add to the symbol table if it's an ID token
			 */

			symboltable.add(lookahead.getLexeme(), Kind.FUNCTION);
		}
		else if(lookahead.getType() == TokenType.PROCEDURE){
			match(TokenType.PROCEDURE);
			match(TokenType.ID);
			arguments();
			match(TokenType.SEMI);
			/*
			 * add to the symbol table if it's an ID token
			 */

			symboltable.add(lookahead.getLexeme(), Kind.PROCEDURE);
		}
		else
		{
			error("Error in subprogram_head.");
		}	
	}
	/**
	 * Executes the rule for the subprogram_head non-terminal symbol in
	 * the expression grammar.
	 */
	public void arguments() {
		if(lookahead.getType() == TokenType.LEFTPAR) {
			match(TokenType.LEFTPAR);
			parameter_list();
			match(TokenType.RIGHTPAR);
		}
		else {
			//lamda option
		}
	}



	/**
	 * Executes the rule for the parameter_list non-terminal symbol in the
	 * expression grammar.
	 * identifier_list : type |
	 * idenfiter_list : type ; parameter_list
	 */
	public void parameter_list() {
		identifer_list();
		match(TokenType.COLON);
		type();
		if(lookahead.getType() == TokenType.SEMI){
			match(TokenType.COLON);
			parameter_list();
		}
	}


	/**
	 * Executes the rule for the compound_statement non-terminal symbol in the
	 * expression grammar.
	 */
	public void compound_statement() {
		match(TokenType.BEGIN);
		optional_statements();
		match(TokenType.END);
	}



	public void optional_statements()  {
		if (lookahead != null && (lookahead.getType() == TokenType.ID 
				||lookahead.getType() == TokenType.BEGIN
				|| lookahead.getType() == TokenType.IF 
				|| lookahead.getType() == TokenType.WHILE)) {
			statement_list();
		} else {
			// lambda option
		}
	}


	/**
	 * Executes the rule for the statement_list non-terminal symbol in
	 * the expression grammar.
	 */
	public void statement_list() {
		if(lookahead.getType() == TokenType.BEGIN || lookahead.getType() 
				== TokenType.ID 
				|| lookahead.getType() == TokenType.IF 
				|| lookahead.getType() == TokenType.WHILE 
				|| lookahead.getType() == TokenType.WRITE
				|| lookahead.getType() == TokenType.READ) {
			statement();
			if(lookahead.getType() == TokenType.SEMI) {
				match(TokenType.SEMI);
				statement_list();
			}
			else {
				// nothing
			}
		}
	}



	/**
	 * Executes the rule for the statement non-terminal symbol in the expression
	 * grammar.
	 */

	/**
	 * Executes the rule for statement; non-terminal symbol in the expression
	 * grammar.
	 */
	public void statement()																	
	{																						
									
		if(lookahead.getType() == TokenType.ID) 												
		{																						
			if(symboltable.is(lookahead.getLexeme(), Kind.VARIABLE))						
			{																					
				variable();																	
				match(TokenType.ASSIGN);														
				expression();																	
			}																					
			else if(symboltable.is(lookahead.getLexeme(), Kind.PROCEDURE))					
			{																					
				procedure_statement();															
			}																					

		}																																									//
													
		else if(lookahead.getType() == TokenType.BEGIN) 										
		{																						
			compound_statement();																
		}																						
													
		else if(lookahead.getType() == TokenType.IF) 										
		{																					
			match(TokenType.IF);															
			expression();																	
			match(TokenType.THEN);															
			statement();																	
			match(TokenType.ELSE);															
			statement();																	
		}																					
												
		else if(lookahead.getType() == TokenType.WHILE)										
		{																						
			match(TokenType.WHILE);															
			expression();																		
			match(TokenType.DO);																
			statement();																	
		}																						
												
		else if(lookahead.getType() == TokenType.READ)										
		{																					
			match(TokenType.READ);																
			match(TokenType.LEFTPAR);												
			match(TokenType.ID);															
			match(TokenType.RIGHTPAR);												
		}																					
										
		else if(lookahead.getType() == TokenType.WRITE)											
		{																					
			match(TokenType.WRITE);															
			match(TokenType.LEFTPAR);												
			expression();																	
			match(TokenType.RIGHTPAR);												
		}																						
		else if(lookahead.getType() == TokenType.RETURN)										
		{																						
			match(TokenType.RETURN);															
			expression();																		
		}																					
	}						
	/**
	 * Executes the rule for the variable non-terminal symbol in
	 * the expression grammar.
	 */
	public void variable() {
		match(TokenType.ID);
		if(lookahead.getType() == TokenType.LEFTBRACKET) {
			match(TokenType.LEFTBRACKET);
			expression();
			match(TokenType.RIGHTBRACKET);
		}
	}

	/**
	 * Executes the rule for the procedure_statement non-terminal symbol in
	 * the expression grammar.
	 */
	public void procedure_statement() {
		if(lookahead.getType() == TokenType.ID) {
			match(TokenType.ID);
		}
		else {
			match(TokenType.ID);
			match(TokenType.RIGHTPAR);
			expression_list();
			match(TokenType.LEFTPAR);
		}
	}


	/**
	 * Executes the rule for the expression_list non-terminal symbol in
	 * the expression grammar.
	 */
	public void expression_list() {
		if(lookahead.getType() == TokenType.ID
				|| lookahead.getType() == TokenType.NUMBER 
				|| lookahead.getType()
				== TokenType.LEFTPAR 
				|| lookahead.getType() == TokenType.NOT
				|| lookahead.getType() == TokenType.PLUS 
				|| lookahead.getType() == TokenType.MINUS){
			expression();
			if(lookahead.getType() == TokenType.COMMA) {
				match(TokenType.COMMA);
				expression_list();
			}
			else {
				// nothing
			}
		}		
	}


	/**
	 * Checks if the token that is passed in is a =, <>, <, <=, >=, or >.
	 * 
	 */

	private boolean isRelop(TokenType input) {
		if(input == TokenType.EQUAL|| 
				input == TokenType.LESSTHAN|| 
				input == TokenType.GREATERTHAN ||
				input == TokenType.LESSTHANEQ||
				input == TokenType.GREATERTHANEQ)
			return true;
		else return false;		
	}




	/**
	 * Executes the rule for the expression non-terminal symbol in
	 * the expression grammar.
	 */
	public void expression() {
		if(lookahead.getType() == TokenType.ID
				|| lookahead.getType() == TokenType.NUMBER 
				|| lookahead.getType() == TokenType.LEFTPAR 
				|| lookahead.getType() == TokenType.NOT || lookahead.getType()
				== TokenType.PLUS || lookahead.getType()
				== TokenType.MINUS){
			simple_expression();
			if(isRelop(lookahead.getType())) {
				match(TokenType.EQUAL);
				match(TokenType.LESSTHAN);
				match(TokenType.GREATERTHAN);
				match(TokenType.LESSTHANEQ);
				match(TokenType.GREATERTHANEQ);
				simple_expression();
			}
		}
	}

	/**
	 * Executes the rule for the simple_expression non-terminal symbol in
	 * the expression grammar.
	 */
	public void simple_expression() {
		if(lookahead.getType() == TokenType.ID|| lookahead.getType() == TokenType.NUMBER 
				|| lookahead.getType() == TokenType.LEFTPAR
				|| lookahead.getType() == TokenType.NOT){
			term();
			simple_part();
		}
		else if (lookahead.getType() == TokenType.PLUS || lookahead.getType() == TokenType.MINUS) {
			sign();
			term();
			simple_part();
		}

	}




	/**
	 * Executes the rule for the simple_part non-terminal symbol in
	 * the expression grammar.
	 */
	public void simple_part() {
		if(isAddop(lookahead.getType())) {
			match(TokenType.PLUS);
			match(TokenType.MINUS);
			match(TokenType.OR);
			term();
			simple_part();
		}
		else {
			//lamda option
		}

	}

	/**
	 * Checks if the token that is passed in is a +, -, or OR.
	 * 
	 */

	private boolean isAddop(TokenType input) {
		if(input == TokenType.PLUS|| 
				input == TokenType.MINUS|| 
				input == TokenType.OR|| 
				input == TokenType.GREATERTHAN)
			return true;
		else return false;		

	}


	/**

    /**
	 * Executes the rule for the exp non-terminal symbol in
	 * the expression grammar.
	 */
	public void exp() {
		term();
		exp_prime();
	}

	/**
	 * Executes the rule for the exp&prime; non-terminal symbol in
	 * the expression grammar.
	 */
	public void exp_prime() {
		if( lookahead.getType() == TokenType.PLUS || 
				lookahead.getType() == TokenType.MINUS ) {
			addop();
			term();
			exp_prime();
		}
		else{
			// lambda option
		}
	}

	/**
	 * Executes the rule for the addop non-terminal symbol in
	 * the expression grammar.
	 */
	public void addop() {
		if( lookahead.getType() == TokenType.PLUS) {
			match( TokenType.PLUS);
		}
		else if( lookahead.getType() == TokenType.MINUS) {
			match( TokenType.MINUS);
		}
		else {
			error( "Addop");
		}
	}




	/**
	 * Executes the rule for the term non-terminal symbol in
	 * the expression grammar.
	 */
	public void term() {
		factor();
		term_prime();
	}


	/** Executes the rule for the term_part non-terminal symbol in
	 * the expression grammar.
	 */
	public void term_part() {
		if(isMulop(lookahead.getType())) {
			match(TokenType.ASTERISK);
			match(TokenType.SLASH);
			match(TokenType.DIV);
			match(TokenType.MOD);
			match(TokenType.AND);
			factor();
			term_part();	
		}
	}


	/**
	 * Executes the rule for the term&prime; non-terminal symbol in
	 * the expression grammar.
	 */
	public void term_prime() {
		if( isMulop(lookahead.type) ) {
			mulop();
			factor();
			term_prime();
		}
		else{
			// lambda option
		}
	}

	/**
	 * Determines whether or not the given token is a mulop token. DIV, MOD, AND....
	 * 
	 * @return true if the token is a mulop, false otherwise
	 */

	private boolean isMulop(TokenType input) {
		if(input == TokenType.ASTERISK || 
				input == TokenType.SLASH || 
				input == TokenType.DIV || 
				input == TokenType.MOD ||
				input ==TokenType.AND)
			return true;
		else return false;		

	}


	/**
	 * Executes the rule for the mulop non-terminal symbol in
	 * the expression grammar.
	 */
	public void mulop() {
		if( lookahead.getType() == TokenType.MULTIPLY) {
			match(TokenType.MULTIPLY);
		}
		else if( lookahead.getType() == TokenType.DIVIDE) {
			match(TokenType.DIVIDE);
		}
		else {
			error( "Mulop");
		}
	}

	/** Executes the rule for the sign non-terminal symbol in
	 * the expression grammar.
	 */
	public void sign() {
		if(lookahead.getType() == TokenType.PLUS) {
			match(TokenType.PLUS);
		}
		else if(lookahead.getType() == TokenType.MINUS) {
			match(TokenType.MINUS);
		}
		else {
			error("sign error");
		}

	} 
	/**
	 * Executes the rule for the factor non-terminal symbol in
	 * the expression grammar.
	 */
	public void factor() {
		// Executed this decision as a switch instead of an
		// if-else chain. Either way is acceptable.
		switch (lookahead.getType()) {
		case LEFTPAR:
			match(TokenType.LEFTPAR);
			exp();
			match(TokenType.RIGHTPAR);
			break;
		case NUMBER:
			match(TokenType.NUMBER);
			break;
		default:
			error("Factor");
			break;
		}
	}




	/**
	 * Matches the expected token.
	 * If the current token in the input stream from the scanner
	 * matches the token that is expected, the current token is
	 * consumed and the scanner will move on to the next token
	 * in the input.
	 * The null at the end of the file returned by the
	 * scanner is replaced with a fake token containing no
	 * type.
	 * @param expected The expected token type.
	 */
	public void match(TokenType expected) {
		System.out.println("match( " + expected + ")");
		if( this.lookahead.getType() == expected) {
			try {
				this.lookahead = scanner.nextToken();
				if( this.lookahead == null) {
					this.lookahead = new Token( null,"End of File");
				}
			} catch (IOException ex) {
				error( "Scanner exception");
			}
		}
		else {
			error("Match of " + expected + " found " + this.lookahead.getType()
			+ " instead.");
		}
	}


	/**
	 * Errors out of the parser.
	 * Prints an error message and then exits the program.
	 * @param message The error message to print.
	 */
	public void error( String message) {
		System.out.println( "Error " + message + " at line " + 
				this.scanner.getLine() + " column " + 
				this.scanner.getColumn());
		//System.exit( 1);
	}




	/**
	 * This method prints out a .symboltable file into the directory of the project.
	 */
	public void writeoutput(String s) {
		PrintWriter write;
		try {
			write = new PrintWriter(
					new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/" + s + ".symboltable")));
			write.println(this.symboltable.toString());
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns the symbol table of the parser.
	 * 
	 * @return SymbolTable
	 */
	public SymbolTable getSymbolTableToString() {
		return this.symboltable;



	}
}
