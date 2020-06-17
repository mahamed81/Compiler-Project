package syntaxtree;

import org.junit.Test;

import scanner.TokenType;

import static org.junit.Assert.assertEquals;

/**
 * JUnit testing for the SyntaxTree.
 *
 */
public class SyntaxTreeTest {

	/**
	 * Created a SyntaxTree test for the bitcoin
	 */
	@Test
	public void testBitcoinExample() {
		
		ProgramNode pNode = new ProgramNode("sample");
		
		// DeclarationsNode
		DeclarationsNode dNode = new DeclarationsNode();
		
		VariableNode bitcoins = new VariableNode("bitcoins");
		VariableNode dollars = new VariableNode("dollars");
		VariableNode yen = new VariableNode("yen");
		
		
		dNode.addVariable( bitcoins );
		dNode.addVariable( dollars );
		dNode.addVariable( yen );
		
		
		pNode.setVariables(dNode);
		
		
		// CompoundStatementNode
		CompoundStatementNode cSNode = new CompoundStatementNode();
		
		AssignmentStatementNode asnDollars = new AssignmentStatementNode();
		asnDollars.setLvalue( dollars );
		asnDollars.setExpression(new ValueNode("1000000"));
		
		AssignmentStatementNode asnYen = new AssignmentStatementNode();
		asnYen.setLvalue( yen );
		OperationNode onMultiply = new OperationNode(TokenType.MULTIPLY);
		onMultiply.setLeft( dollars );
		onMultiply.setRight(new ValueNode("102"));
		asnYen.setExpression(onMultiply);
		
		AssignmentStatementNode asnBitcoins = new AssignmentStatementNode();
		asnBitcoins.setLvalue( bitcoins );
		OperationNode onDivide = new OperationNode(TokenType.DIV);
		onDivide.setLeft( dollars );
		onDivide.setRight(new ValueNode("400"));
		asnBitcoins.setExpression(onDivide);
		
		
		cSNode.addStatement(asnDollars);
		cSNode.addStatement(asnYen);
		cSNode.addStatement(asnBitcoins);
		
		pNode.setMain(cSNode);
		
		// SubProgramDeclarationsNode
		SubProgramDeclarationsNode sPDNode = new SubProgramDeclarationsNode();
		
		pNode.setFunctions(sPDNode);
		
		// Compare the strings
		String expected = "Program: sample\n" + 
				"|-- Declarations\n" + 
				"|-- --- Name: bitcoins\n" + 
				"|-- --- Name: dollars\n" + 
				"|-- --- Name: yen\n" + 
				"|-- SubProgramDeclarations\n" + 
				"|-- Compound Statement\n" + 
				"|-- --- Assignment\n" + 
				"|-- --- --- Name: dollars\n" + 
				"|-- --- --- Value: 1000000\n" + 
				"|-- --- Assignment\n" + 
				"|-- --- --- Name: yen\n" + 
				"|-- --- --- Operation: MULTIPLY\n" + 
				"|-- --- --- --- Name: dollars\n" + 
				"|-- --- --- --- Value: 102\n" + 
				"|-- --- Assignment\n" + 
				"|-- --- --- Name: bitcoins\n" + 
				"|-- --- --- Operation: DIV\n" + 
				"|-- --- --- --- Name: dollars\n" + 
				"|-- --- --- --- Value: 400\n";
		
		String returned = pNode.indentedToString(0);
		
		assertEquals(expected, returned);
	}
}