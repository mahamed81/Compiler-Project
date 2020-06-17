package parser;
import symbolTable.*;
import syntaxtree.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import static scanner.TokenType.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import scanner.*;


/**
 * The parser recognizes whether an inputs is acceptable and make sure they compile
 * properlty. 
 * 
 * @author Mahamed Ahmed 
 */
public class Parser {

	///////////////////////////////
	//    Instance Variables

	private Token lookahead;

	private MyScanner scanner;

	private SymbolTable symboltable;



	///////////////////////////////
	//       Constructors
	///////////////////////////////

	public Parser( String text, boolean isFilename) {

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
	 * executes the rule for the program symbol in the expression grammer to see
	 * the program symbol
	 */
	public ProgramNode program() {

		match(TokenType.PROGRAM);
		String name = lookahead.getLexeme(); // ----use this and add it to those
		// 4 stops thats use id
		match(TokenType.ID);
		if (!symboltable.addProgram(name))
			error("This name already exists in symbol table");
		ProgramNode program = new ProgramNode(name);
		match(TokenType.SEMI);
		// errors should occur if it is not the program token type
		program.setVariables(declarations());
		program.setFunctions(subprogram_declarations());
		program.setMain(compound_statement());
		match(TokenType.PERIOD);
		System.out.println(program.indentedToString(0));
		return program;
	}


	/**
	 * Executes the rule for the declarations non-terminal symbol in the expression
	 * grammar.
	 */


	public DeclarationsNode declarations() {
		DeclarationsNode declarations = new DeclarationsNode();
		if (lookahead.getType() == TokenType.VAR) {
			match(TokenType.VAR);
			ArrayList<String> IDList= identifier_list();
			for (String id : IDList) {
				declarations.addVariable(new VariableNode(id));
			}
			match(TokenType.COLON);
			type();
			match(TokenType.SEMI);
			declarations();
		}
		return declarations;
	}


	/**
	 * Executes the rule for the subprogram_declaration non-terminal symbol in
	 * the expression grammar.
	 */

	public SubProgramNode subprogram_declaration() {
		SubProgramNode subprogramDec = subprogram_head();
		subprogramDec.setVariables(declarations());
		subprogramDec.setFunctions(subprogram_declarations());
		subprogramDec.setMain(compound_statement());
		return subprogramDec;
	}

	/**
	 * Executes the rule for the identifier_list non-terminal symbol in the
	 * expression grammar.
	 */

	public ArrayList<String> identifier_list() {
		ArrayList<String> idList = new ArrayList<>();
		idList.add(lookahead.getLexeme());
		match(TokenType.ID);
		if (lookahead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			idList.addAll(identifier_list());
		}
		// else lambda case
		return idList;
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
	 * @return 
	 */
	public SubProgramNode subprogram_head() {
		SubProgramNode subprogramHead = null;
		if (lookahead.getType() == TokenType.FUNCTION) {
			match(TokenType.FUNCTION);
			String funcName = lookahead.getLexeme();
			if (!symboltable.addFunction(funcName, null)) error(funcName + " already exists in symbol table");
			subprogramHead = new SubProgramNode(funcName);
			match(TokenType.ID);
			arguments();
			match(TokenType.COLON);
			symboltable.addFunction(funcName, t);
			match(TokenType.SEMI);
		} else if (lookahead.getType() == PROCEDURE) {
			match(PROCEDURE);
			String procedureName = lookahead.getLexeme();
			subprogramHead = new SubProgramNode(procedureName);
			if (!symboltable.addProcedure(procedureName)) error(procedureName + " already exists in symbol table");
			match(TokenType.ID);
			arguments();
			symboltable.addProcedure(procedureName);
			match(TokenType.SEMI);
		} else
			error("subprogram_head");
		return subprogramHead;
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
		identifier_list();
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
	 * Executes the rule for the statement non-terminal symbol in
	 * the expression grammar.
	 */

	public StatementNode statement() {
		StatementNode state = null;
		if (lookahead.getType() == TokenType.ID) {
			if (!symboltable.doesExist(lookahead.getLexeme())) error(lookahead.getLexeme() + " has not been declared");
			if (symboltable.isVariable(lookahead.getLexeme())) {
				AssignmentStatementNode assign = new AssignmentStatementNode();
				assign.setLvalue(variable());
				match(TokenType.ASSIGN);
				assign.setExpression(expression());
				return assign;

			} else if (symboltable.isProcedure(lookahead.getLexeme())) {
				return procedure_statement();
			} else
				error(lookahead.getLexeme() + " not found in symbol table.");
		} else if (lookahead.getType() == TokenType.BEGIN)
			state = compound_statement();
		else if (lookahead.getType() == TokenType.IF) {
			IfStatementNode ifNode = new IfStatementNode();
			match(TokenType.IF);
			ifNode.setTest(expression());
			match(TokenType.THEN);
			ifNode.setThenStatement(statement());
			match(TokenType.ELSE);
			ifNode.setElseStatement(statement());

			return ifNode;

		} else if (lookahead.getType() == TokenType.WHILE) {
			WhileStatementNode whileNode = new WhileStatementNode();
			match(TokenType.WHILE);
			whileNode.setTest(expression());
			match(TokenType.DO);
			whileNode.setDoStatement(statement());
			return whileNode;
		} else {
			error("statement");
		}

		return state;
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
	 * @return 
	 */
	public StatementNode procedure_statement() {
		if(lookahead.getType() == TokenType.ID) {
			match(TokenType.ID);
		}
		else {
			match(TokenType.ID);
			match(TokenType.RIGHTPAR);
			expression_list();
			match(TokenType.LEFTPAR);
		}
		return null;
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
	 * @return 
	 */
	public ExpressionNode expression() {
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
		return null;
	}

	/**
	 * Executes the rule for the simple_expression non-terminal symbol in
	 * the expression grammar.
	 */

	public ExpressionNode simple_expression() {
		ExpressionNode express = null;
		if (lookahead.getType() == TokenType.ID || lookahead.getType() == TokenType.NUMBER
				|| lookahead.getType() == TokenType.LEFTPAR || lookahead.getType() == TokenType.NOT) {
			express = term();
			express = simple_part(express);
		} else if (lookahead.getType() == TokenType.PLUS || lookahead.getType() == TokenType.MINUS) {
			UnaryOperationNode unaryNode = sign();
			express = term();
			unaryNode.setExpression(simple_part(express));
			return express;
		} else
			error("simple_expression");
		return express;
	}



	/**
	 * Executes the rule for the simple_part non-terminal symbol in
	 * the expression grammar.
	 * @param express 
	 */
	public ExpressionNode simple_part(ExpressionNode positionLeft) {
		if (isAddop(lookahead.getType())) {
			OperationNode operation = new OperationNode(lookahead.getType());
			match(lookahead.getType());
			ExpressionNode right = term();
			operation.setLeft(positionLeft);
			operation.setRight(right);
			return simple_part(operation);

		} else {
			return positionLeft;
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
	 * @return 
	 */
	public ExpressionNode term() {
		ExpressionNode left = factor();
		return term_part(left);
	}


	/** Executes the rule for the term_part non-terminal symbol in
	 * the expression grammar.
	 */
	public ExpressionNode term_part(ExpressionNode posLeft) {
		if (isMulop(lookahead.getType())) {
			OperationNode operation = new OperationNode(lookahead.getType());
			match(lookahead.getType());
			ExpressionNode right = factor();
			operation.setLeft(posLeft);
			operation.setRight(term_part(right));
		}

		return posLeft;

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
	 * @return 
	 */
	public UnaryOperationNode sign() {
		if(lookahead.getType() == TokenType.PLUS) {
			match(TokenType.PLUS);
		}
		else if(lookahead.getType() == TokenType.MINUS) {
			match(TokenType.MINUS);
		}
		else {
			error("sign error");
		}
		return null;

	} 
	/**
	 * Executes the rule for the factor non-terminal symbol in
	 * the expression grammar.
	 */
	public ExpressionNode factor() {
		ExpressionNode express = null;
		express = new VariableNode(lookahead.getLexeme());
		match(TokenType.ID);

		if (lookahead.getType() == TokenType.LEFTBRACKET) {
			match(TokenType.LEFTBRACKET);
			expression();
			match(TokenType.RIGHTBRACKET);

		} else if (lookahead.getType() == TokenType.LEFTPAR) {
			match(TokenType.LEFTPAR);
			expression_list();
			match(TokenType.RIGHTPAR);
		} else if (lookahead.getType() == TokenType.NUMBER) {
			TokenType t;
			express = new ValueNode(lookahead.getLexeme());
			if (((List<String>) express).contains(".")) t = TokenType.REAL;
			else t = TokenType.INTEGER;
			match(TokenType.NUMBER);
			return express;
		}

		else if (lookahead.getType() == TokenType.LEFTPAR) {
			match(TokenType.LEFTPAR);
			express = expression();
			match(TokenType.RIGHTPAR);

		} else if (lookahead.getType() == TokenType.NOT) {
			UnaryOperationNode unaryNode = new UnaryOperationNode(NOT);
			match(TokenType.NOT);
			unaryNode.setExpression(factor());
			return unaryNode;
		} else
			error("factor");
		return express;
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
	public SymbolTable getSymbolTable() {
		return this.symboltable;


	}
}
