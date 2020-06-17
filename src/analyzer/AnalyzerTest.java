package analyzer;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import parser.Parser;
import scanner.TokenType;
import syntaxtree.AssignmentStatementNode;
import syntaxtree.ExpressionNode;
import syntaxtree.OperationNode;
import syntaxtree.ProgramNode;
import syntaxtree.ValueNode;
import syntaxtree.VariableNode;

public class AnalyzerTest {

	
	@Test
	void statement() {
		// ASSIGNMENTNODE
		System.out.println("\n-Basic AssignmentStatementNode-");
		System.out.println("test := 8*8+8*8");
		// expression node
		OperationNode op = new OperationNode(TokenType.PLUS);
		// nested statement
		OperationNode op2 = new OperationNode(TokenType.ASTERISK);
		op2.setLeft(new ValueNode("8"));
		op2.setRight(new ValueNode("8"));
		op.setLeft(op2);
		op.setRight(op2);
		// assignment statement node
		AssignmentStatementNode assign = new AssignmentStatementNode();
		assign.setLvalue(new VariableNode("Test"));
		assign.setExpression(op);

		int original = assign.indentedToString(0).split("\n").length;
		System.out.println("Original:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
		sa.statement(assign);
		int end = assign.indentedToString(0).split("\n").length;
		System.out.println("test := 128");
		System.out.println("New:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
		System.out.println("Difference: " + (original - end));

	
	}

	@Test
	void semanticVariable() {
		// Testing standard variable
		VariableNode var = new VariableNode("foo", TokenType.INTEGER);
		VariableNode result = sa.checkVariablesDeclared(var);
		String expected = "Name: foo, Type: INTEGER";
		assertEquals(result.toString(), expected);

		System.out.println("--ArrayNode[5+8]--");
		

	}

	@Test
	void semanticExpression() {
		System.out.println("--Fold Expression--");
		System.out.println("*PLUS*");
		OperationNode op = new OperationNode(Type.PLUS);
		op.setLeft(new ValueNode("5"));
		op.setRight(new ValueNode("10"));

		System.out.println(op.indentedToString(0));
		ExpressionNode val = sa.expression(op);
		System.out.println(val.indentedToString(0));

		System.out.println("*MINUS*");
		op.setOperation(TokenType.MINUS);
		op.setLeft(new ValueNode("5"));
		op.setRight(new ValueNode("10"));

		System.out.println(op.indentedToString(0));
		val = sa.expression(op);
		System.out.println(val.indentedToString(0));

		System.out.println("*ASTERISK*");
		op.setOperation(TokenType.ASTERISK);
		op.setLeft(new ValueNode("5"));
		op.setRight(new ValueNode("10"));

	}

	@Test
	void semanticAnalyzer() {
		
		Parser parse = new Parser(new File("inputs/prg.pas"));
		ProgramNode prob = parse.program();
		int original = prob.indentedToString(0).split("\n").length;
		System.out.println("Original:\n\t" + prob.indentedToString(0).replaceAll("\n", "\n\t"));
		int end = prob.indentedToString(0).split("\n").length;
		System.out.println("New:\n\t" + prob.indentedToString(0).replaceAll("\n", "\n\t"));
		System.out.println("Difference: " + (original - end));
	}
}
