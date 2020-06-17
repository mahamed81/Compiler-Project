package syntaxtree;
import scanner.TokenType;



// nikaylahwoody
public class UnaryOperationNode extends ExpressionNode {

	private ExpressionNode expression;
	private TokenType operation;
	
	public UnaryOperationNode(TokenType operation) {
	        this.operation = operation;
		   }
	public ExpressionNode getExpression() {
		        return (this.expression);
		   }
	 public void setExpression(ExpressionNode node) {
		         this.expression = node;
		     }
	 public TokenType getOperation() {
		         return (this.operation);
		     }
	 public void setOperation(TokenType op) {
		         this.operation = op;
		     }
	 @Override
	 public String toString() {
		         return operation.toString();
		     }
	 @Override
	 public String indentedToString(int level) {
		         String ans = this.indentation(level);
		         ans += "Unary Operation: " + this.operation + "\n";
		         ans += expression.indentedToString(level + 1);
		         return (ans);
}
	 @Override
	 public boolean equals(Object o) {
		         boolean ans = false;
		         if (o instanceof syntaxtree.OperationNode) {
		             syntaxtree.UnaryOperationNode other = (syntaxtree.UnaryOperationNode) o;
		             if ((this.operation == other.operation) && (this.expression.equals(other.expression)))
		            	 ans = true;
		         }
		         return ans;
	 }
}