package analyzer;

import java.io.PrintWriter;
import java.util.ArrayList;

import scanner.TokenType;
import syntaxtree.*;


public class SemanticAnalyzer {


	private ProgramNode pNode;
	private boolean isTagged;

	/**
	 * Getter for isTagged
	 * 
	 * @return
	 */
	public boolean isTagged() {
		return isTagged;
	}


	/**
	 * Constructor for a SemanticAnalyzer
	 * 
	 * @param pNode
	 */
	public SemanticAnalyzer(ProgramNode pNode) {
		this.pNode = pNode;
		this.isTagged = false;
	}

	/**
	 * Setter for isTagged
	 * 
	 * @param tag
	 */
	public void setTag(boolean tag) {
		this.isTagged = tag;
	}



	/**
	 * Getter for pNode of the Semantic Analyzer.
	 * 
	 * @return
	 */
	public ProgramNode getPNode() {
		return pNode;
	}	

	public void writeCodeToFile(String filename) {
		PrintWriter write;
		try {
			write = new PrintWriter(
					new BufferedWriter(new FileWriter(filename.substring(0, filename.length() - 4) + ".syntaxtree")));
			write.println(pNode.indentedToString(0));
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that checks the variable types that are used in the code. If there is
	 * tagged for checking if the types dont match
	 */
	private void checkVariablesTypes() {
		for (StatementNode sN : pNode.getMain().getStatements()) {
			statementType(sN);
		}
	}

	/**
	 * This method calls "checkVariablesDeclared()" and "checkVariablesTypes()"
	 * which is a method that checks to see if all to analyze the types and declared
	 * variables
	 */

	public void Analyze() {
		this.checkVariablesDeclared();
		this.checkVariablesTypes();

		if (!this.getPNode().getFunctions().getProcs().isEmpty()) {
			for (int i = 0; i < this.getPNode().getFunctions().getProcs().size(); i++) {
				SemanticAnalyzer sA = new SemanticAnalyzer(this.getPNode().getFunctions().getProcs().get(i));
				sA.Analyze();
				this.getPNode().getFunctions().getProcs().set(i, (SubProgramNode) sA.getPNode());
			}
		}
	}


	/**
	 * This method traverses through all statement nodes.
	 */
	private DataType statementType(StatementNode statementNode) {
		if (statementNode instanceof AssignmentStatementNode) {
			expressionType(((AssignmentStatementNode) statementNode).getLvalue());
			DataType dtL = ((AssignmentStatementNode) statementNode).getLvalue().getDataType();
			DataType dtE = expressionType(((AssignmentStatementNode) statementNode).getExpression());

			if (dtL != dtE) {
				if (dtL == DataType.REAL || dtE == DataType.REAL) {
					if (dtL == DataType.REAL) {
						((AssignmentStatementNode) statementNode).getExpression().setDataType(DataType.REAL);
					} 

					else if (dtE == DataType.REAL) {
						System.out.println("Variable " + ((AssignmentStatementNode) statementNode).getLvalue().getName()
								+ " wasn't declared as real, but is being assigned a real.");
						((AssignmentStatementNode) statementNode).getLvalue().setDataType(DataType.REAL);
						this.isTagged = true;
					}
				} 

				else if (dtL == DataType.INTEGER || dtE == DataType.INTEGER) {
					if (dtL == DataType.INTEGER) {
						System.out.println("Variable " + ((AssignmentStatementNode) statementNode).getLvalue().getName()
								+ " is being assigned to something with unknown datatype.");
						((AssignmentStatementNode) statementNode).getExpression().setDataType(DataType.INTEGER);
						this.isTagged = true;
					}
					if (dtE == DataType.INTEGER) {
						// error if passed
					}
				}
			}
		} else if (statementNode instanceof IfStatementNode) {
			expressionType(((IfStatementNode) statementNode).getTest());
			statementType(((IfStatementNode) statementNode).getThenStatement());
			statementType(((IfStatementNode) statementNode).getElseStatement());


		} else if (statementNode instanceof WhileStatementNode) {
			expressionType(((WhileStatementNode) statementNode).getTest());
			statementType(((WhileStatementNode) statementNode).getDoStatement());


		} else if (statementNode instanceof ProcedureStatementNode) {
			for (ExpressionNode eN : ((ProcedureStatementNode) statementNode).getExpressions()) {
				expressionType(eN);
			}

		} else if (statementNode instanceof ReadStatementNode) {
			DataType dT = expressionType(((ReadStatementNode) statementNode).getName());
			((ReadStatementNode) statementNode).getName().setDataType(dT);


		} else if (statementNode instanceof WriteStatementNode) {
			DataType dT = expressionType(((WriteStatementNode) statementNode).getName());

		} else if (statementNode instanceof CompoundStatementNode) {
			for (StatementNode sN : ((CompoundStatementNode) statementNode).getStatements()) {
				statementType(sN);
			}
		}
		return null;
	}



	/**
	 * This method traverses through all expression nodes.
	 */
	private DataType expressionType(ExpressionNode eNode) {
		if (eNode instanceof ValueNode) {
		} else if (eNode instanceof VariableNode) {

		} else if (eNode instanceof OperationNode) {
			DataType dtL = expressionType(((OperationNode) eNode).getLeft());
			DataType dtR = expressionType(((OperationNode) eNode).getRight());

			TokenType tType = ((OperationNode) eNode).getOperation();
			if (tType == TokenType.EQUAL || tType == TokenType.NOTEQUAL || tType == TokenType.LESSTHAN
					|| tType == TokenType.LESSTHANEQ || tType == TokenType.GREATERTHAN || tType == TokenType.GREATERTHANEQ) {
				eNode.setDataType(DataType.BOOLEAN);
				return DataType.BOOLEAN;
			}

			if (dtL == DataType.REAL || dtR == DataType.REAL) {
				eNode.setDataType(DataType.REAL);
				return DataType.REAL;
			} else if (dtL == DataType.INTEGER || dtR == DataType.INTEGER) {
				eNode.setDataType(DataType.INTEGER);
				return DataType.INTEGER;
			}

		} else if (eNode instanceof ProcedureNode) {
		}
		return eNode.getDataType();
	}


	/**
	 * Method checks the variable declared that are used in the code. If there
	 * are any variables that aren't declared but used, the code is flagged.
	 */
	private void checkVariablesDeclared() {
		ArrayList<VariableNode> variablesDeclared = new ArrayList<VariableNode>();
		variablesDeclared.addAll(pNode.getVariables().getVariable());

		if (this.pNode instanceof SubProgramNode) {
			variablesDeclared.addAll((((SubProgramNode) pNode).getParameters()).getParameter());
		}
		ArrayList<VariableNode> variablesUsed = new ArrayList<VariableNode>();

		for (StatementNode sN : pNode.getMain().getStatements()) {
			variablesUsed.addAll(statement(sN));
		}

		for (VariableNode sN : variablesUsed) {
			if (!variablesDeclared.contains(sN)) {
				System.out.println("Variable " + sN.getName() + " is used but was never declared.");
				this.isTagged = true;
			}
		}

		for (SubProgramNode pN : pNode.getFunctions().getProcs()) {
			SemanticAnalyzer sA = new SemanticAnalyzer(pN);
			sA.checkVariablesDeclared();
		}
	}

	/**
	 * This method traverses through all statement nodes.
	 * 
	 * @param statementNode
	 */
	private static ArrayList<VariableNode> statement(StatementNode statementNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<VariableNode>();

		if (statementNode instanceof AssignmentStatementNode) {
			vNodes.add(((AssignmentStatementNode) statementNode).getLvalue());
			vNodes.addAll(expression(((AssignmentStatementNode) statementNode).getExpression()));


		} else if (statementNode instanceof IfStatementNode) {
			vNodes.addAll(expression(((IfStatementNode) statementNode).getTest()));
			vNodes.addAll(statement(((IfStatementNode) statementNode).getThenStatement()));
			vNodes.addAll(statement(((IfStatementNode) statementNode).getElseStatement()));

		} else if (statementNode instanceof WhileStatementNode) {
			vNodes.addAll(expression(((WhileStatementNode) statementNode).getTest()));
			vNodes.addAll(statement(((WhileStatementNode) statementNode).getDoStatement()));

		} else if (statementNode instanceof ProcedureStatementNode) {
			for (ExpressionNode eN : ((ProcedureStatementNode) statementNode).getExpressions()) {
				vNodes.addAll(expression(eN));
			}
		} else if (statementNode instanceof ReadStatementNode) {
			vNodes.addAll(expression(((ReadStatementNode) statementNode).getName()));

		} else if (statementNode instanceof WriteStatementNode) {
			vNodes.addAll(expression(((WriteStatementNode) statementNode).getName()));
		}
		return vNodes;
	}


	/**
	 *  This method that traverses through all expression nodes.
	 * 
	 * @param eNode
	 */
	private static ArrayList<VariableNode> expression(ExpressionNode eNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<VariableNode>();
		if (eNode instanceof ValueNode) {
		} else if (eNode instanceof VariableNode) {
			vNodes.add((VariableNode) eNode);
		} else if (eNode instanceof OperationNode) {
			vNodes.addAll(expression(((OperationNode) eNode).getLeft()));
			vNodes.addAll(expression(((OperationNode) eNode).getRight()));
		} else if (eNode instanceof ProcedureNode) {
			for (ExpressionNode eN : ((ProcedureNode) eNode).getExpressions()) {
				vNodes.addAll(expression(eN));
			}
		}
		return vNodes;
	}

}
