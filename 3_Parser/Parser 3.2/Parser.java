/* VERSIONE 3.2
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
		} else error("syntax error expected symbol " + t + " doesn't match peek: " + look.tag);
    }

    public void start() {
		prog();	
		match(Tag.EOF);		
	    System.out.println("sequenza di token in input corrisponde alla grammatica."); //fine
		System.exit(0);
    }

    private void prog(){

        switch(look.tag){
            case 259: //assign
                statlist();
                break;
                
            case 268: //print
                statlist();
                break;
                
            case 269: //read
                statlist();
                break;
                
            case 265: //while
                statlist();
                break;
            
            case 261: //conditional
                statlist();
                break;
                
            
            case 123: //graffa aperta
                statlist();
                break;
                
            case Tag.EOF: break; 
            
            default: error("error initial statement not found.");
            }
    }
    
    private void statlist(){
    
    	switch(look.tag){
		case 259: //assign
			stat();
			statlistp();
			break;
			
		case 268: //print
			stat();
			statlistp();
			break;
			
		case 269: //read
			stat();
			statlistp();
			break;
			
		case 265: //while
			stat();
			statlistp();
			break;
			
		case 261: //conditional
			stat();
			statlistp();
			break;
		
		case 123: //graffa aperta
			stat();
			statlistp();
			break; 
		
		default: error("error in statements.");
		}
    
    }
    
    private void statlistp(){
    	
    	switch(look.tag){
    	
            case ';': //new statlist
                    match(';');
                    stat();
                    statlistp();
                    break;
            
            case '}': break; //epsilon
		    case Tag.EOF: break; //epsilon
		
		    default: error("error in ; between statements.");
		}
    }
    
    private void stat(){
    	switch(look.tag){
		
		case 259: //assign
			match(259);
			expr();
			match(260); 
            idlist();//to
			break;
			
		case 268: //print
			match(268);
			match('[');
			exprlist();
			match(']');
			break;
			
		case 269: //read
			match(269);
			match('[');
			idlist();
			match(']');
			break;
			
		case 265: //while
			match(265);
			match('(');
            bexpr();
			match(')');
            stat();
			break;
	
		case 261: //conditional
			match(261); //gestisce fattorizzabile
			match('[');
            optlist();
			match(']');
            stat_aux_conditional();

			break;
			
		case 123: //graffa aperta
			match('{');
            statlist();
			match('}');
			break;
		
		case Tag.EOF: break; 
		
		default: error("ERROR IN Stat");
		}
    }
    
    void stat_aux_conditional(){ //parte non fattorizzabile di conditional
        switch(look.tag){
            case 264:  //else
            match(264); 
            stat(); 
            match(267);
            break;

            case 267:
            match(267); //end
            break;

            case Tag.EOF: break; 
		
		    default: error("ERROR IN stat_aux_conditional");
        }
    }
    
    private void idlist(){
    	match(257); //ID
    	idlistp();
    }
    
    private void idlistp(){
    	
    	switch(look.tag){
    		
    		case 44 : match(44); //comma
                idlist();
    			break;	
    		
            case ';': break;
            case ']': break;
            case 267: break; //end 
            case 262: break; //option
            case Tag.EOF: break; //EOF
            
                default: error("error in , between id list.");
    	}
    }
    
    private void optlist(){
    	
   	switch(look.tag){
		
		case 262: //option
			optitem();
			optlistp();
			break;
		
		default: error("error in optlist");
		}
    }
    
    private void optlistp(){
    
    	switch(look.tag){
    		
    		case 262 : //option
                optitem();
    			break;	
    			
                case ']' : break; //empty, no "option"
            
                default: error("error in between opt listing.");
    	}
    }
    
    private void optitem(){ //non NULL();
    	
    	match(262); //option
		match('(');
        bexpr();
		match(')');
        match(263); //do
		stat();

    }
    
    private void bexpr(){
    
    	match(258); // relop
    	expr();
        expr();
		if(look.tag == Tag.EOF) error("ended after bexpr"); 
	
    }
    
    private void expr(){
    	switch(look.tag){
		case '+': 
			match('+');
			match('(');
			exprlist();
			match(')');
			break;
    				
		case '*': 
			match('*');
			match('(');
			exprlist();
			match(')');
			break;
    				
		case '-': 
			match('-');
            expr();
            expr();
			break;
    			
			case '/': 	
			match('/');
			expr();
            expr();
			break;
    					
		case Tag.NUM: 
			match(Tag.NUM);
			break;
				
		case Tag.ID: 
			match(Tag.ID);
			break;
    					
		case Tag.EOF: break; 
		
		default: error("ERROR IN Expr!");
	}
    }
    
    
    private void exprlist(){
    
    	switch(look.tag){
    		case '+': 
    			expr();
    			exprlistp();
    			break;
    		
    		case '-': 
    			expr();
    			exprlistp();
    			break;
    			
    		case '/': 
    			expr();
    			exprlistp();
    			break;
    			
    		case '*': 
    			expr();
    			exprlistp();
    			break;
    			
    		case Tag.NUM: 
			expr();
    			exprlistp();
			break;
				
		case Tag.ID: 
			expr();
    			exprlistp();
			break;
    			
    		case Tag.EOF: break; 
		
		default: error("ERROR IN Exprlist!");    		
    	}
    }
    
    private void exprlistp(){
    	
    	switch(look.tag){
    		
    		case 44 : 
                match(44); //comma
    		    exprlist();
    			break;	
    			
    		case ']': break; 
            case ')': break; 
	
    		default: error("error in , between listing expressions");  
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
