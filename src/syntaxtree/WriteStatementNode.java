package syntaxtree;

public class WriteStatementNode extends StatementNode {
	
	private ExpressionNode name;

	public WriteStatementNode(ExpressionNode eNode) {
		this.name = eNode;
	}
	
	
	public void setName(VariableNode name) {
		this.name = name;
	}
	
	public ExpressionNode getName() {
		return name;
	}
	@Override
    public String indentedToString( int level) {
        String answer = this.indentation( level);
        answer += "WriteStatementNode:" + "\n";
        answer += name.indentedToString( level + 1);
        return answer;
    }
}