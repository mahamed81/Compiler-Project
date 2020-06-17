package scanner;

// package simplescanner;

/**
*
* @author Mahamed Ahmed
*/
public class Token{
   public String lexeme;
   public TokenType type;

   public Token(TokenType tt, String l) {
       this.lexeme = l;
       this.type = tt;
   }

   /**
   * print out the token to the console
   */
   public String toString() {
	   return ("Token Type: " + type + "\tToken: " + lexeme);
   }
   /**
    * This method returns the lexeme 
    */

public String getLexeme() {
	return lexeme;
}

/**
 * This method sets the lexeme 
 */

public void setLexeme(String lexeme) {
	this.lexeme = lexeme;
}

/**
 * This method returns the token type 
 */
public TokenType getType() {
	return type;
}

/**
 * This method sets the type of token 
 */
public void setType(TokenType type) {
	this.type = type;
}



}
