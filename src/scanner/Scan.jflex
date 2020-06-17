/**
 * This file will provide the specification for our Mini Pascal programming lnaguage,
 * which we will inout into JFlex to generate the java code for out Pascal Scanner.
 */

/* Declarations */
package scanner;

%%
%public                     // make output class public
%class    MyScanner    /* Names the produced java file */
%function nextToken    /* Renames the yylex() function */
%type     Token        /* Defines the return type of the scanning function */
%eofval{
  return null;
%eofval}
%{
  LookUpTable lut = new LookUpTable();
%}

%{
  /**
   * Gets the line number of the most recent lexeme.
   * @return The current line number.
   */
  public int getLine() { return yyline;}
  
  /**
   * Gets the column number of the most recent lexeme.
   * This is the number of chars since the most recent newline char.
   * @return The current column number.
   */
  public int getColumn() { return yycolumn;}
  
  /** The lookup table of token types for symbols. */
  private LookupTable table = new LookupTable();
%}



/* Patterns */

other               = .               // any character
letter              = [A-Za-z]        // select one letter A-Z or a-z
digit               = [0-9]           // select one digit 0-9
id                  = {letter}+({letter} | {digit})* // atleast one letter followed by
                                                    // zero more more characters and digits
digits              = {digit}{digit}* // atwill be used to least one digit followed by zero or more digits
optional_fraction   = (\.{digits})?   // accepts zero or one fraction
optional_exponent   = ((E[\+|\-]?){digits})?
number              = {digits}{optional_fraction}{optional_exponent} // TODO: handle case with letter at the end of num i.e. variables cannot start with a number.
oneSymbol           = [\(\)\{\}\[\]\+\-\=\<\>\*\/\:\;\,\.]
symbol              = {oneSymbol}[<=|>=|:=|<>]
whitespace          = [ \n\t\r\f]
commentSyntax       = [^\{\}]
comment             = \{{commentSyntax}*\}
%%

/* Lexical Rules */

{number}         {   /* handles numbers */
                String lexeme = yytext();   //Take text from Scanner
                return (new Token(TokenType.NUMBER, lexeme));
              }

{id}          {   /* handles ID's and keywords */
                TokenType tt = lut.get(yytext());
                String lexeme = yytext();   //Take text from Scanner

                // if lexeme was found in the look up table then it is a keyword
                if (tt != null)
                  return (new Token(tt, lexeme));
                // else lexeme is an ID
                return (new Token(TokenType.ID, lexeme));
              }

{symbol}      {   /* handles symbols */
                String lexeme = yytext();   //Take text from Scanner
                TokenType tt = lut.get(yytext());
                return (new Token(tt, lexeme));
              }

{whitespace}  {   /* Ignore Whitespace */

              }

{other}       {   /* Illigal character */
                return (new Token(TokenType.ERROR, yytext()));
              }
