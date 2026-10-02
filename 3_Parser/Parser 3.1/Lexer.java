/* VERSIONE 2.3 CON IDENTIFICATORI E COMMENTI
*
* un identificatore è una sequenza non vuota di lettere, numeri, ed il simbolo di “underscore” _ ; * la sequenza non comincia con un numero e non può essere composta solo dal simbolo _.
*
* commenti riconosciuti:
* commenti delimitati con barra* e *barra;
* commenti che iniziano con doppia barra e che terminano con un a capo oppure con EOF.
* I commenti devono essere ignorati dal programma per l’analisi lessicale; in altre parole, per le parti dell’input che contengono commenti, non deve essere generato nessun token.
*  @author  Federico Zerbin, 902129
*/

import java.io.*; 
import java.util.*;

public class Lexer
{

    public static int line = 1;
    private char peek = ' ';
    
    private int state = 0;
    private boolean valid = false;
    private String lexeme = "";
    
    private boolean comment = false;
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
    
    //------ sistema di registrazione su hashtable delle keyword
    private Hashtable<String, Word> keywordsTab = new Hashtable();
    
    void reserveWord(Word w) { 
    	keywordsTab.put(w.lexeme, w); 
    }
    
    public Lexer(){ //costruttore mette in reserved in hashtable le keyword
	    reserveWord( Word.assign );
	    reserveWord( Word.to );
	    reserveWord( Word.conditional );
	    reserveWord( Word.option );
	    reserveWord( Word.dotok );
	    reserveWord( Word.elsetok );
	    reserveWord( Word.whiletok );
	    reserveWord( Word.begin );
	    reserveWord( Word.end );
	    reserveWord( Word.print );
	    reserveWord( Word.read );
	    
	    //keywordsTab.forEach((key, value) -> System.out.println(key + " : " + value));
    }
    

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') { /*gestione commenti come simboli speciali*/
        
            if (peek == '\n') line++;
            readch(br);
                        
    }
    //
         	    	   
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            
            case '(':
            	peek = ' ';
            	return Token.lpt;
            	
            case ')':
            	peek = ' ';
            	return Token.rpt;
            	
            case '[':
            	peek = ' ';
            	return Token.lpq; 
            	
            case ']':
            	peek = ' ';
            	return Token.rpq; 
            	
            case '{':
            	peek = ' ';
            	return Token.lpg; 
            	
            case '}':
            	peek = ' ';
            	return Token.rpg; 
            	
            case '+': 
            	peek = ' ';
            	return Token.plus;
            	
            case '-': 
            	peek = ' ';
            	return Token.minus;
            	
            case '*': 
            	peek = ' ';
            	return Token.mult;
            /*	
            case '/': 
            	readch(br);
	    	if (peek == ' ') 
                    peek = ' ';
                    return Token.div; 
            	 */
            case ';':   
		peek = ' ';
            	return Token.semicolon;
            	
            case ',':   
		peek = ' ';
            	return Token.comma;
	
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

	    case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
	    	
	    case '<':
	    	readch(br);
	    	if (peek != '=' & peek != '>') {
	    	    peek = ' ';
                    return Word.lt;
	    	} else if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                    } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                    } else {
                    System.err.println("Erroneous character"
                            + " after < : "  + peek );
                    return null;
                }
                
            case '>':
	    	readch(br);
	    	if (peek != '=') {
                    peek = ' ';
                    return Word.gt;
	    	} else if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                    } else {
                    System.err.println("Erroneous character"
                            + " after < : "  + peek );
                    return null;
                }
                
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }
  
            case (char)-1:
                return new Token(Tag.EOF);

            case '/':
                readch(br);  
				if(Character.isLetterOrDigit(peek)){ //è solo un simbolo div, singolo
					return Token.div;

				}else{
					switch (peek){		
                        case '/': //doppia barra
                            do {readch(br);} while(peek!='\n'); //leggo finchè non trovo \n e poi va a capo da solo
                            break;

                        case '*': //barra asterisco
                            do{
                                do {readch(br);} while(peek!='*'); //leggo finchè non trovo *
                                readch(br);

                                while(peek=='*') readch(br); //se ho *, continuo a leggere finchè non trovo /
                            } while(peek!='/');
                            break;

                        default: //se arrivo qui significa che non ho trovato / di chiusura
                            System.err.println("Erroneous comment"+ peek );
                            return null;

                        }
                        readch(br); //va avanti a leggere e passa al prossimo simbolo
						return lexical_scan(br);
                    }   
                
            default:  
            	
            	if (Character.isLetter(peek) || peek == '_') { 
            	
                	String lexeme = "" + peek;
                	readch(br);
                	
                	while (Character.isLetterOrDigit(peek) || peek == '_'){ /*nel while controllo avanzamento stati mentre costruisce lessema*/
				
				if (Character.isLetterOrDigit(peek)){ if (state == 0 || state == 2) state = 1;}
                	
                		else { if (state == 0) state = 2; }
                	
                		valid = state == 1;
                		
				lexeme = lexeme + peek;
				readch(br);
			}
 
			if (valid){
				if (keywordsTab.containsKey(lexeme)){
				return keywordsTab.get(lexeme);
				} else return new Word(Tag.ID, lexeme);
			} else {System.out.println("Invalid identifier: " +lexeme+ " caused an exception. state: "+state); return null;}
			
			
		} else if (Character.isDigit(peek)) {
			
			if (state == 1 || state == 2){state = 1;}; /*se all'interno di identificatore cambiano state*/
			valid = state == 1;
			
			int value = 0;
			
			while (Character.isDigit(peek)){
			        
				value = value*10 + (peek-48);
				readch(br);
			}
			
			return new NumberTok(Tag.NUM, value);
			

                } 
                
                else {
                 	System.out.println("Erroneous character: " + peek);
                	return null;}
               
               
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
       
        String path = "input.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
