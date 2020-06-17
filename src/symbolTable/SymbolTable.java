package symbolTable;

import java.util.HashMap;
import symbolTable.Identi;
import java.util.Map.Entry;
import java.util.Set;
/*
 * The symbol table will store information about identifiers found in a pascal program. 
 * Each entry for an identifier in the Symbol Table will need to contain appropriate information about the identifier:
 *  its lexeme, the kind of identifier (program, variable, array, or function) and other information appropriate to the kind of identifier. 
 *  For example, a variable should have its type information.
 * @author Mahamed Ahmed
 */


public class SymbolTable {

	private HashMap<String , Identi> symbols = new HashMap<String, Identi>();

	public SymbolTable() {
		symbols = new HashMap<String, Identi>();
	}


	/**
	 * This method addProgram checks to see if there exists a name
	 * of the type variable, returns false if found, else it add's in to the hashmap
	 * @param name of variable that is being checked by hashmap
	 * @return True if there is no existance of the name.
	 */

	public boolean addProgram(String name) {
		Identi s = new Identi();

		if (symbols.containsKey(name)) {
			return false;
		}
		else {
			s.name = name;
			s.kind = Kind.PROGRAM;
			symbols.put(name, s);

			return true;
		}
	}


	/**
	 * This isProgram method checks to see if the kind of id matches
	 * the enum type of variable 
	 * @param name -The variable id name that is being checked in the hash map
	 * @return True if there is a match 
	 */
	public boolean isProgram(String name) {
		Identi data = (Identi) symbols.get(name);
		if(data == null) {
			return false;
		}
		else if(data.kind == Kind.PROGRAM){
			return true;
		}
		return false;
	}


	/**
	 * This method addVariable checks to see if there exists a name
	 * of the type variable, returns false if found, else it add's in to the hashmap
	 * @param name- name of variable that is being checked by hashmap
	 * @return True if there is no existance of the name.
	 */

	public boolean addVariable(String name) {
		Identi s = new Identi();
		if (symbols.containsKey(name)) {
			return false;
		}
		else {
			s.name = name;
			s.kind = Kind.VARIABLE;
			symbols.put(name, s);

			return true;
		}
	}

	/**
	 * This isVariable method checks to see if the kind of id matches
	 * the enum type of variable 
	 * @param name -The variable id name that is being checked in the hash map
	 * @return True if there is a match 
	 */
	public boolean isVariable(String name) {
		Identi data = (Identi) symbols.get(name);
		if(data == null) {
			return false;
		}
		else if(data.kind == Kind.VARIABLE){
			return true;
		}
		return false;
	}



	/**
	 * Adds lexeme and kind into our SymbolTable.
	 */
	public void add(String lexeme, Kind kind) {
		this.symbols.put(lexeme, new Identi(lexeme, kind));
	}

	/**
	 * This method addArray checks to see if there exists a name
	 * of the type variable, returns false if found, else it add's in to the hashmap
	 * @param name- name of variable that is being checked by hashmap
	 * @return True if there is no existance of the name.
	 */

	public boolean addArray(String name) {
		Identi s = new Identi();
		if (symbols.containsKey(name)) {
			return false;
		}
		else {
			s.name = name;
			s.kind = Kind.PROGRAM;
			symbols.put(name, s);
			return true;
		}
	}


	/**
	 * This isArray method checks to see if the kind of id matches
	 * the enum type of variable 
	 * @param name -The variable id name that is being checked in the hash map
	 * @return True if there is a match 
	 */
	public boolean isArray(String name) {
		Identi data = (Identi) symbols.get(name);
		if(data == null) {
			return false;
		}
		else if(data.kind == Kind.PROGRAM){
			return true;
		}
		return false;
	}

	/**
	 * This method addFunction checks to see if there exists a name
	 * of the type variable, returns false if found, else it add's in to the hashmap
	 * @param name- name of variable that is being checked by hashmap
	 * @return True if there is no existance of the name.
	 */

	public boolean addFunction(String name) {
		Identi s = new Identi();
		if (symbols.containsKey(name)) {
			return false;
		}
		else {
			s.name = name;
			s.kind = Kind.FUNCTION;
			symbols.put(name, s);

			return true;
		}
	}


	/**
	 * This isFunction method checks to see if the kind of id matches
	 * the enum type of variable 
	 * @param name -The variable id name that is being checked in the hash map
	 * @return True if there is a match 
	 */
	
	public boolean isFunction(String name) {
		Identi data = (Identi) symbols.get(name);
		if(data == null) {
			return false;
		}
		else if(data.kind == Kind.FUNCTION){
			return true;
		}
		return false;
	}
	
	public boolean is(String name, Kind kind)
	{
		if(symbols.containsKey(name))
		{
			if(symbols.get(name).getKind() == kind)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false; 
		}
	}
	

	/**
	 * This method addProcedure checks to see if there exists a name
	 * of the type variable, returns false if found, else it add's in to the hashmap
	 * @param name- name of variable that is being checked by hashmap
	 * @return True if there is no existance of the name.
	 */

	public boolean addProcedure(String name) {
		Identi s = new Identi();
		if (symbols.containsKey(name)) {
			return false;
		}
		else {
			s.name = name;
			s.kind = Kind.PROCEDURE;
			symbols.put(name, s);
			return true;
		}
	}


	/**
	 * This isProcedure method checks to see if the kind of id matches
	 * the enum type of variable 
	 * @param name -The variable id name that is being checked in the hash map
	 * @return True if there is a match 
	 */
	public boolean isProcedure(String name) {
		Identi data = (Identi) symbols.get(name);
		if(data == null) {
			return false;
		}
		else if(data.kind == Kind.PROCEDURE){
			return true;
		}
		return false;
	}



	/**
	 * The toString method, it will be used to print out the
	 * SymbolTable into a file.
	 */
	@Override
	public String toString() {
		String s = String.format("%-10s\t\t%-10s\t\t%-10s\n", "Key", "Entry");
		s += "-----------------------------------------------------------------------\n";

		for (Entry<String, Identi> entry : this.symbols.entrySet()) {
			s += String.format("%-14s\t\t%-14s\t\t%-14s\n", entry.getKey(), entry.getValue(), entry.getValue());
		}
		return s;
		
	}





}






