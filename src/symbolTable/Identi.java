package symbolTable;

public class Identi {
	
	
	public String name;
    
    public Kind kind;
    
    public DataType type;

	private String lexeme;

	private Object dataType;

    

	public Identi() {
		
	}


	public Identi(String lexeme, Kind kind) {
		this.lexeme = lexeme;
		this.kind = kind;
		this.dataType = null;
		
	}

		// Getters 

	public String getName() {
		return name;
	}


	public Kind getKind() {
		return kind;
	}


	public DataType getType() {
		return type;
	}
    
    

}
