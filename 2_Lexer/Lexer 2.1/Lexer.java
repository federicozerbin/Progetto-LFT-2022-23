/* VERSIONE 2.1
 * lexer base 
 *  @author  Federico Zerbin, 902129
*/

import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
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
    
    public Lexer(){ //costruttore mette in reserved in hashtable le keywords
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
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
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
            	
            case '/':
            	peek = ' ';
            	return Token.div;
            	
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

            default:  
                if (Character.isLetter(peek)) { 
                
                	String lexeme = "" + peek;
                	readch(br);
                	
			while (Character.isLetter(peek)){
				lexeme = lexeme + peek;
				readch(br);
			}
			
			//verifico in tabella se c'è se c'è ritorno quella
			//altrimenti ritorno new Word(Tag.ID, lexeme)
			if(keywordsTab.containsKey(lexeme)){
				return keywordsTab.get(lexeme);
			} else return new Word(Tag.ID, lexeme);
			
			
		} else if (Character.isDigit(peek)) {
			
			int value = 0;
			
			while (Character.isDigit(peek)){
			        
				value = value*10 + (peek-48); //peek è char
				readch(br);
			}
			
			return new NumberTok(Tag.NUM, value);
			

                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        //String path = "...path..."; // il percorso del file da leggere
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
