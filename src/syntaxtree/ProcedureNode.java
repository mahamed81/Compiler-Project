package syntaxtree;

import java.util.ArrayList;

public class ProcedureNode extends ExpressionNode {
	
	private String name;
    private ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
	private String dataType;
    
    public ProcedureNode(String name) {
    	this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addExpression( ExpressionNode expression) {
        this.expressions.add( expression);
    }
	
	public ArrayList<ExpressionNode> getExpressions() {
		return this.expressions;
	}
	
	@Override
	public String indentedToString(int level) {
		String answer = this.indentation( level);
        answer += "ProcedureNode: " + getName() + " (" + this.dataType + ")" + "\n";
        for( ExpressionNode expression : expressions) {
            answer += expression.indentedToString( level + 1);
        }
        return answer;
	}

}