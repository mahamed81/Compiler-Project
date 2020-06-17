package codegeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import scanner.TokenType;
import syntaxtree.*;

/**
 * This class will write the syntaxtree out as MIPS Assembly Language.
 * 
 @author mahamedahmed
 */

public class CodeGeneration {
	private static int currentTRegister = 0;
	private static int currentFRegister = 0;
	private static String x = new String();

	private static String generateHex() {
		Random random = new Random();
		int val = random.nextInt();
		return Integer.toHexString(val);
	}

	
	/**
	 * This method prints out a symboltable file into the current directory
	 */

	public static void writeCodeToFile(String filename, ProgramNode root) {
		PrintWriter write;
		try {
			write = new PrintWriter(
					new BufferedWriter(new FileWriter(filename.substring(0, filename.length() - 4) + ".asm")));
			write.println(writeCodeForRoot(root));
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * write the code to the root node in the assembly code
	 */
	public static String writeCodeForRoot(ProgramNode root) {
		String code = "";
		code += "    .data\n\n" + "promptuser:    .asciiz \"Enter value: \"" + "\n" + "newline:       .asciiz \"\\n\""
				+ "\n";
		if (root.getVariables() != null) {
			code += writeCode(root.getVariables());
		}

		code += "\n\n    .text\nmain:\n";

		if (root.getMain() != null) {
			code += writeCode(root.getMain());
		}
		code += "\njr $ra";
		return (code);
	}
	
	public static String writeCode(ParameterStatementNode node) {
		String code = "";
		ArrayList<VariableNode> variables = node.getParameter();
		for (VariableNode vN : variables) {
			code += String.format("%-10s     .word 0\n", vN.getName() + ":");
		}
		return (code);
	}

	public static String writeCode(SubProgramNode node) {
		String code = "";
		if (node.getFunctions() != null) {
			for (SubProgramNode spN : node.getFunctions().getProcs()) {
				code += writeCode(spN);
			}
		}
		if (node.getMain() != null) {
			code += writeCode(node.getMain());
		}
		return (code);
	}

	public static String writeCode(DeclarationsNode node) {
		String code = "";
		ArrayList<VariableNode> variables = node.getVariable();
		for (VariableNode vN : variables) {
			code += String.format("%-10s     .word 0\n", vN.getName() + ":");
		}
		return (code);
	}

	public static String writeCode(CompoundStatementNode node) {
		String code = "";
		ArrayList<StatementNode> statements = node.getStatements();
		for (StatementNode sN : statements) {
			code += writeCode(sN);
		}
		return (code);
	}
	
	//STATEMENT NODES
	 

		public static String writeCode(StatementNode node) {
			String nodeCode = null;

			if (node instanceof AssignmentStatementNode) {
				nodeCode = writeCode((AssignmentStatementNode) node);
			} else if (node instanceof ProcedureStatementNode) {
				nodeCode = writeCode((ProcedureStatementNode) node);
			} else if (node instanceof CompoundStatementNode) {
				nodeCode = writeCode((CompoundStatementNode) node);
			} else if (node instanceof IfStatementNode) {
				nodeCode = writeCode((IfStatementNode) node);
			} else if (node instanceof WhileStatementNode) {
				nodeCode = writeCode((WhileStatementNode) node);
			} else if (node instanceof ReadStatementNode) {
				nodeCode = writeCode((ReadStatementNode) node);
			} else if (node instanceof WriteStatementNode) {
				nodeCode = writeCode((WriteStatementNode) node);
			}
			return (nodeCode);
		}

	
		public static String writeCode(AssignmentStatementNode node) {
			String code = "# Assignment-Statement\n";
			ExpressionNode expression = node.getExpression();
			if (expression.getDataType() == DataType.REAL) {

				String rightRegister = "$f" + currentFRegister;
				code += writeCode(expression, rightRegister);
				code += "sw      $f" + currentFRegister + ",   " + node.getLvalue() + "\n";
			} else {
				String rightRegister = "$t" + currentTRegister;
				code += writeCode(expression, rightRegister);
				code += "sw      $t" + currentTRegister + ",   " + node.getLvalue() + "\n";
			}

			return (code);
		}

		public static String writeCode(ProcedureStatementNode node) {
			String code = null;

			return ("\n");
		}

		public static String writeCode(IfStatementNode node) {
			String code = "\n# If-Statement\n";
			x = generateHex();
			String register = "$t" + currentTRegister;
			code += writeCode(node.getTest(), register);
			code += "beq     " + register + ",   $zero, IfStatementFailID" + x + "\n";

			code += writeCode(node.getThenStatement());
			code += "j	IfStatementPassID" + x + "\n";

			code += "IfStatementFailID" + x + ":\n";
			code += writeCode(node.getElseStatement());
			code += "IfStatementPassID" + x + ":\n";
			return (code);
		}
		public static String writeCode(WhileStatementNode node) {
			String code = "\n# While-Statement\n";
			x = generateHex();
			String register = "$t" + currentTRegister;
			code += "WhileID" + x + ":\n";
			code += writeCode(node.getTest(), register);
			code += "beq     " + register + ",   $zero, WhileCompleteID" + x + "\n";

			code += writeCode(node.getDoStatement());
			code += "j       WhileID" + x + "\n";

			code += "WhileCompleteID" + x + ":\n";

			return (code);
		}
		public static String writeCode(ReadStatementNode node) {
			String code = "\n# Read-Statement\n";

			code += "li      $v0,   4\n";
			code += "la      $a0,   promptuser\n";
			code += "syscall\n";

			DataType dT = node.getName().getDataType();
			if (dT == DataType.INTEGER) {
				code += "li      $v0,   5\n";
				code += "syscall\n";
				code += "sw      $v0,   " + node.getName().getName() + "\n";
			} else if (dT == DataType.REAL) {
				
				code += "li      $v0,   6\n";
				code += "syscall\n";
				code += "sw      $v0,   " + node.getName().getName() + "\n";

			}

			return (code);
		}
	
	
		public static String writeCode(WriteStatementNode node) {
			String code = "\n# Write-Statement\n";

			if (node.getName() instanceof OperationNode) {
				if (node.getName().getDataType() == DataType.INTEGER) {
					code += writeCode(node.getName(), "$s0");
					code += "li      $v0,   1\n";
					code += "move    $a0,   $s0\n";
					code += "syscall\n";
				}
			} else if (node.getName() instanceof VariableNode) {
				code += writeCode(node.getName(), "$s0");
				code += "li      $v0,   1\n";
				code += "move    $a0,   " + node.getName() + "\n";
				code += "syscall\n";
			}
			code += "# New Line\n" + "li      $v0,   4\n" + "la      $a0,   newline\n" + "syscall\n";

			return code;
		}
	
		// EXPRESSION NODES

		/**
		 * Writes code for the given expression node
		 */
		public static String writeCode(ExpressionNode node, String reg) {
			String nodeCode = null;
			if (node instanceof OperationNode) {
				nodeCode = writeCode((OperationNode) node, reg);
			} else if (node instanceof ValueNode) {
				nodeCode = writeCode((ValueNode) node, reg);
			} else if (node instanceof VariableNode) {
				nodeCode = writeCode((VariableNode) node, reg);
			}
			return (nodeCode);
		}
		
		/**
		 * Writes code for an operations node
		 */
		public static String writeCode(OperationNode opNode, String resultRegister) {
			String code;
			String leftRegister = "";
			String rightRegister = "";

			ExpressionNode left = opNode.getLeft();

			if (left.getDataType() == DataType.REAL) {
				leftRegister = "$f" + currentFRegister++;
			} else {
				leftRegister = "$t" + currentTRegister++;
			}

			code = writeCode(left, leftRegister);
			ExpressionNode right = opNode.getRight();
			if (left.getDataType() == DataType.REAL) {
				
				rightRegister = "$f" + currentFRegister++;
			} else {
				rightRegister = "$t" + currentTRegister++;
			}

			code += writeCode(right, rightRegister);
			TokenType kindOfOp = opNode.getOperation();
			if (kindOfOp == TokenType.PLUS) {
				code += "add     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
			}
			if (kindOfOp == TokenType.MINUS) {
				code += "sub     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
			}
			if (kindOfOp == TokenType.MULTIPLY) {
				code += "mult    " + leftRegister + ",   " + rightRegister + "\n";
				code += "mflo    " + resultRegister + "\n";
			}
			if (kindOfOp == TokenType.DIV) {
				code += "div     " + leftRegister + ",   " + rightRegister + "\n";
				code += "mflo    " + resultRegister + "\n";
			}
			if (kindOfOp == TokenType.LESSTHAN) {
				code += "slt     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
			}
			if (kindOfOp == TokenType.GREATERTHAN) {
				code += "slt     " + resultRegister + ",   " + rightRegister + ",   " + leftRegister + "\n";
			}
			if (kindOfOp == TokenType.LESSTHANEQ) {
				code += "addi    " + rightRegister + ",   " + rightRegister + ",   1\n";
				code += "slt     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
			}
			if (kindOfOp == TokenType.GREATERTHANEQ) {
				code += "addi    " + leftRegister + ",   " + leftRegister + ",   1\n";
				code += "slt     " + resultRegister + ",   " + rightRegister + ",   " + leftRegister + "\n";
			}
			if (kindOfOp == TokenType.EQUAL) {
				x = generateHex();
				code += "beq     " + rightRegister + ",   " + leftRegister + ",   EqualID" + x + "\n";
				code += "li      " + resultRegister + ",   " + "0\n";
				code += "j       EndEqualID" + x + "\n";
				code += "EqualID" + x + ":\n";
				code += "li      " + resultRegister + ",   " + "1\n";
				code += "EndEqualID" + x + ":\n";
			}
			if (kindOfOp == TokenType.NOTEQUAL) {
				
				x = generateHex();
				code += "beq     " + rightRegister + ",   " + leftRegister + ",   NotEqualID" + x + "\n";
				code += "li      " + resultRegister + ",   " + "1\n";
				code += "j       EndNotEqualID" + x + "\n";
				code += "NotEqualID" + x + ":\n";
				code += "li      " + resultRegister + ",   " + "0\n";
				code += "EndNotEqualID" + x + ":\n";
			}

			currentTRegister = 0;
			currentFRegister = 0;
			return (code);
		}

		/**
		 * Writes code for a value node
		 */
		public static String writeCode(ValueNode valNode, String resultRegister) {
			String value = valNode.getAttribute();
			String code = "addi    " + resultRegister + ",   $zero, " + value + "\n";
			return (code);
		}
		public static String writeCode(VariableNode vNode, String resultRegister) {
			String name = vNode.getName();
			String code = "lw      " + resultRegister + ",   " + name + "\n";
			return (code);
		}
	
}