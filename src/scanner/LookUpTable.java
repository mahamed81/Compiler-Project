package scanner;

// package simplescanner;
import java.util.HashMap;

public class LookUpTable extends HashMap<String, TokenType>{
    public LookUpTable() {
        this.put("(", TokenType.LEFTPAR);
        this.put(")", TokenType.RIGHTPAR);
        this.put("[", TokenType.LEFTBRACKET);
        this.put("]", TokenType.RIGHTBRACKET);
        this.put("{", TokenType.LEFTCURL);
        this.put("}",TokenType.RIGHTCURL);
        this.put("+", TokenType.PLUS);
        this.put("-", TokenType.MINUS);
        this.put("=", TokenType.EQUAL);
        this.put("<", TokenType.LESSTHAN);
        this.put("<=", TokenType.LESSTHANEQ);
        this.put(">=", TokenType.GREATERTHANEQ);
        this.put("*", TokenType.MULTIPLY);
        this.put("/", TokenType.DIVIDE);
        this.put(":", TokenType.COLON);
        this.put(";", TokenType.SEMI);
        this.put(",", TokenType.COMMA);
        this.put(".", TokenType.PERIOD);
        this.put(":=", TokenType.ASSIGN);
        this.put("<>", TokenType.NOTEQUAL);
        this.put("and", TokenType.AND);
        this.put("or", TokenType.OR);
        this.put("mod", TokenType.MOD);
        this.put("div", TokenType.DIV);
        this.put("var", TokenType.VAR);
        this.put("of", TokenType.OF);
        this.put("not", TokenType.NOT);
        this.put("if", TokenType.IF);
        this.put("else", TokenType.ELSE);
        this.put("then", TokenType.THEN);
        this.put("do", TokenType.DO);
        this.put("while", TokenType.WHILE);
        this.put("begin", TokenType.BEGIN);
        this.put("end", TokenType.END);
        this.put("program", TokenType.PROGRAM);
        this.put("array", TokenType.ARRAY);
        this.put("function", TokenType.FUNCTION);
        this.put("real", TokenType.REAL);
        this.put("integer", TokenType.INTEGER);
        this.put("procedure", TokenType.PROCEDURE);
        this.put("number", TokenType.NUMBER);
        this.put("id", TokenType.ID);
        this.put("error", TokenType.ERROR);
    }
}
