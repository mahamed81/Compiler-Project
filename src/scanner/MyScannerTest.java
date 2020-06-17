package scanner;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class MyScannerTest {

	
	/**
     * Test of Yytext method, of class ExpScanner.
     */
	@Test
	public void testYytext() throws IOException {
        System.out.println("yytext");
        FileInputStream fis = null;
        try {
        	fis = new FileInputStream("test/simple.pas");
        } catch (FileNotFoundException ex) {
        }
        InputStreamReader isr = new InputStreamReader( fis);
        MyScanner instance = new MyScanner(isr);
        
        instance.nextToken();
        String expected = "3";
        String result = instance.yytext();
        assertEquals(expected, result);
        
        instance.nextToken();
        expected = "+";
        result = instance.yytext();
        assertEquals(expected, result);
        
        instance.nextToken();
        expected = "5";
        result = instance.yytext();
        assertEquals(expected, result);

        instance.nextToken();
        expected = "*";
        result = instance.yytext();
        assertEquals(expected, result);
        
        instance.nextToken();
        expected = "2";
        result = instance.yytext();
        assertEquals(expected, result);

	}
	
	 /**
     * Test of nextToken method, of class Scanner.
     */
	@Test
	public void testnextToken() throws Exception {
		System.out.println("nextToken");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("test/simple.pas");
		} catch (FileNotFoundException ex) {
		}
		InputStreamReader isr = new InputStreamReader (fis);
		
		MyScanner instance = new MyScanner(isr);
		
//		scanner.TokenType expected = scanner.TokenType.NUM;
//		scanner.TokenType result = instance.nextToken().getType();
//		assertEquals(expected, result);
//		
//		expected = scanner.TokenType.PLUS;
//        result = instance.nextToken().getType();
//        assertEquals(expected, result);
//        
//        expected = scanner.TokenType.NUM;
//        result = instance.nextToken().getType();
//        assertEquals(expected, result);
//        
//        expected = scanner.TokenType.MULTIPLY;
//        result = instance.nextToken().getType();
//        assertEquals(expected, result);
//        
//        expected = scanner.TokenType.NUM;
//        result = instance.nextToken().getType();
//        assertEquals(expected, result);
//        
//		
       
     
		
	}}