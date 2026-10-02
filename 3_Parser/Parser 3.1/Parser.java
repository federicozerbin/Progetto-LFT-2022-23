/* VERSIONE 3.1 
*  @author  Federico Zerbin, 902129
*/

import java.io.*;
public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
	} else error("syntax error");
    }

    public void start() {
	 	switch(look.tag){
			case '(':
				expr(); 			
				match(Tag.EOF); 		
				System.out.println("sequenza di token in input corrisponde alla grammatica."); //fine
				System.exit(0);
				break;

			case Tag.NUM:
				expr(); 			
				match(Tag.EOF); 		
				System.out.println("sequenza di token in input corrisponde alla grammatica."); //fine
				System.exit(0);
				break;

			default: error("no initial expression found. it must start with '(' or a NUM. ");

		}
    }

    private void expr() {	//prima procedura. {expr} --> {term}{exprp} si aspetta di trovare term.
	
		switch(look.tag){
			case '(':
				term();
				exprp();
				break;
			
			case Tag.NUM:
				term();
				exprp();
				break;
			
			default: error("error, bad syntax in expr: no '(' | NUM found.");
		}
    }

    private void exprp() {
	switch(look.tag){
		case '+':
			match('+');
			term();
			exprp();
			break;
			
		case '-':
			match('-');
			term();
			exprp();
			break;
		
		case ')': break; //epsilon
		case Tag.EOF: break; //epsilon
		default: error("error: bad syntax in exprp, no '+' | '-' symbols found.");
		break;
	}
    }

    private void term() { //con term si aspetta fact;
       	switch(look.tag){
		case '(':
			fact();
			termp();
			break;
		
		case Tag.NUM:
			fact();
			termp();
			break;
		
		default: error("error: bad syntax in term, no '(' | NUM found.");
		}
    }

    private void termp() {
    	switch(look.tag){
		case '*':
			match('*');
			fact();
			termp();
			break;
			
		case '/':
			match('/');
			fact();
			termp();
			break;

		case '+': break;
		case '-': break;
		case ')': break;
		case Tag.EOF: break; 
		default: error("error: bad syntax in termp, no '*' | '/' found. peek = " + look.tag);
		}
    }

    private void fact() {
    	switch(look.tag){
		case '(':
			match('(');
			expr();
			match(')');
			break;
			
		case Tag.NUM:
			match(Tag.NUM);
			break;
		
			default: error("error: bad syntax in fact, no '(' | NUM symbols found.");
		}
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}

