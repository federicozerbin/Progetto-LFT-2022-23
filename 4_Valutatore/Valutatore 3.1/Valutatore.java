/* VERSIONE 4.1
*  @author  Federico Zerbin, 902129
*/

import java.io.*; 

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
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
        int expr_val;
        switch(look.tag){
			case '(':
                expr_val = expr();
                match(Tag.EOF);
                System.out.println("Correct syntax of the expression checked.");
                System.out.println("Value of input expression is " + expr_val);
				System.exit(0);

			case Tag.NUM:
                expr_val = expr();
                match(Tag.EOF);	
                System.out.println("Correct syntax of the expression checked.");
                System.out.println("Value of input expression is " + expr_val);	
				System.exit(0);

			default: error("no initial expression found. it must start with '(' or a NUM. ");

		}
    }

    private int expr() { 
	    int term_val, exprp_val;

        switch(look.tag){
            case '(':
                term_val = term();  //exprp_i = term_val
                exprp_val = exprp(term_val);
                return exprp_val;
            
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                return exprp_val;
            
            default: error("error, bad syntax in expr: no '(' | NUM found.");
        }

	    return -1;
    }

    private int exprp(int exprp_i) {
	int term_val, exprp_val;
        switch(look.tag){
            case '+':
                match('+');
                term_val = term();  //exprp_i = term_val
                exprp_val = exprp(exprp_i + term_val);
                return exprp_val;
                
            case '-':
                match('-');
                term_val = term();  //exprp_i = term_val
                exprp_val = exprp(exprp_i - term_val);
                return exprp_val;
            
            case ')': return exprp_i; //epsilon
            case Tag.EOF: return exprp_i; //epsilon
            default: error("error: bad syntax in exprp, no '+' | '-' symbols found.");
            break;
        }
        
        return -1;
    }

    private int term() { 
        int fact_val, termp_val;
        switch(look.tag){
            case '(':
                fact_val = fact();  //termp_i = fact_val
                termp_val = termp(fact_val);
                return termp_val;
            
            case Tag.NUM:
                fact_val = fact();  
                termp_val = termp(fact_val);
                return termp_val;
            
            default: error("error: bad syntax in term, no '(' | NUM found.");
            }
            return -1;
    }
    
    private int termp(int termp_i) { 
        int fact_val, termp_val;
        switch(look.tag){
            case '*':
                match('*');
                fact_val = fact();  
                termp_val = termp(termp_i*fact_val);
                return termp_val;
                
            case '/':
                match('/');
                fact_val = fact();  
                termp_val = termp(termp_i/fact_val);
                return termp_val;
    
            case '+': return termp_i;
            case '-': return termp_i;
            case ')': return termp_i;
            case Tag.EOF: return termp_i;
            default: error("error: bad syntax in termp, no '*' | '/' found. peek = " + look.tag);
            }
            return -1;
    }
    
    private int fact() { 
        int expr_val, fact_val;
        switch(look.tag){
            case '(':
                match('(');
                expr_val = expr();
                match(')');
                fact_val = expr_val;
                return fact_val;
                
            case Tag.NUM: //look is Token, here is also NumberTok
                NumberTok number = (NumberTok)look;
                fact_val = number.value; //ci va valore value di numbertok
                match(Tag.NUM);
                return fact_val;
            
                default: error("error: bad syntax in fact, no '(' | NUM symbols found.");
            }
            return -1;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}