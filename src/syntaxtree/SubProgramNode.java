package syntaxtree;

import java.util.ArrayList;




public class SubProgramNode extends ProgramNode {
	 private DataType dataType;
		private SubProgramType type;
		private ParameterStatementNode parameters;

		public ParameterStatementNode getParameters() {
			return parameters;
		}

		public void setParameters(ParameterStatementNode parameters) {
			this.parameters = parameters;
		}

		public SubProgramType getType() {
			return type;
		}

		public void setType(SubProgramType type) {
			this.type = type;
		}
		
		public DataType getDataType() {
			return dataType;
		}

		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}

		public SubProgramNode(String aName) {
			super(aName);
			dataType = null;
			setVariables(new DeclarationsNode());
			setFunctions(new SubProgramDeclarationsNode());
			setMain(new CompoundStatementNode());
		}
	

	@Override
	public String indentedToString(int level) {
		String answer = this.indentation( level);
       // answer += "SubProgram: " + getName() + "\n";
        answer += getParameters().indentedToString( level + 1);
        answer += getVariables().indentedToString( level + 1);
        answer += getFunctions().indentedToString( level + 1);
        answer += getMain().indentedToString( level + 1);
        return answer;
	}
}