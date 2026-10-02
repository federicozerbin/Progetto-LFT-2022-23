/* VERSIONE 5.1
*  @author  Federico Zerbin, 902129
*/

import java.io.*;
public class Traduttore {
    
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Traduttore(Lexer l, BufferedReader br) {
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

    public void prog() {        
	// ... completare ...
        int lnext_prog = code.newLabel();

        if(look.tag == 259 || look.tag == 268 || look.tag == 269 || look.tag == 265 || look.tag == 261 || look.tag == 123){

            statlist(lnext_prog);
            code.emit(OpCode.GOto, lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);

        }

        else error("Initial statement not found | Syntax error in initial statement");


        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        };
	// ... completare ...
        System.out.println(); //fine
        System.out.println("La sequenza di token in input è stata tradotta correttamente."); //fine
		System.exit(0);
    }

    
    private void statlist(int lnext_statlist){
    
    	switch(look.tag){
		case 259: //assign
			stat();
			statlistp(lnext_statlist);
			break;
			
		case 268: //print
            stat();
            statlistp(lnext_statlist);
			break;
			
		case 269: //read
            stat();
            statlistp(lnext_statlist);
			break;
			
		case 265: //while
            stat();
            statlistp(lnext_statlist);
			break;
			
		case 261: //conditional
            stat();
            statlistp(lnext_statlist);
			break;
		
		case 123: //graffa aperta
            stat();
            statlistp(lnext_statlist);
			break; 
		
		default: error("error in statements.");
		}
    
    }
    
    private void statlistp(int lnext_statlistp){
    	
    	switch(look.tag){
    	
            case ';': //new statlist
                    match(';');
                    stat();
                    statlistp(lnext_statlistp);
                    break;
            
            case '}': break; //epsilon
		    case Tag.EOF: break; //epsilon
		
		    default: error("error in ; between statements.");
		}
    }
    
    private void stat(){
        int lnext_stat = code.newLabel();
    	switch(look.tag){
		
		case 259: //ASSIGN
			match(259);
			expr();   //emette il val di NUM, "ldc 37"
			match(260); //matcha il to

            String lexeme_ID = ((Word)look).lexeme; //salvo l'id della var in lexeme_ID
            int address_ID_in_st = st.lookupAddress(lexeme_ID); //controllo se c'è nella symbol table (st)

				if (address_ID_in_st == -1) {
					code.emit(OpCode.istore, count); //se non c'è emetto istore valore di count
					st.insert(lexeme_ID, count++);   //e poi lo inserisco in st

				} else {
					code.emit(OpCode.istore, address_ID_in_st); //se c'è emetto istore col suo address
				}

            idlist(lnext_stat); //continuo con idlist
            code.emit(OpCode.GOto, lnext_stat);
            code.emitLabel(lnext_stat);
			break;
			
		case 268: //PRINT
			match(268);
			match('[');
			exprlist(lnext_stat);
            code.emit(OpCode.invokestatic,1); //emetto il metodo per la print stdout
			match(']');
            code.emit(OpCode.GOto, lnext_stat);
            code.emitLabel(lnext_stat);
			break;
			
		case 269: //READ
			match(269);
			match('[');

                String lexeme_ID_reading = ((Word)look).lexeme; //salvo l'id della var da leggere 
                int reading_address_ID_in_st = st.lookupAddress(lexeme_ID_reading); //controllo se c'è nella symbol table (st)
            
                if (reading_address_ID_in_st == -1) {
                    reading_address_ID_in_st = count; //se non c'è allora vale count
                    st.insert(lexeme_ID_reading, count++);   //e va inserito in st

                } //altrimenti è a posto, reading_address_ID è quello nella st
                
                code.emit(OpCode.invokestatic,0); //emetto il metodo per la read stdin
                code.emit(OpCode.istore, reading_address_ID_in_st);//poi deve fare lo store in address

            idlist(lnext_stat);
			match(']');
            code.emit(OpCode.GOto, lnext_stat);
            code.emitLabel(lnext_stat);
			break;
			
		case 265: //WHILE
            int while_cond = code.newLabel(); //label per ripetere while, nuova
			match(265);
			match('(');
            
            code.emitLabel(while_cond); 
            bexpr(lnext_stat); 
            //emette GOTO label false(prossima istruzione) ------ poi label del while

			match(')');
            stat();
            
            code.emit(OpCode.GOto, while_cond); //torna alla condizione while
            code.emitLabel(lnext_stat); //La prossima istruzione 
			break;
	
		case 261: //conditional
			match(261); //gestisce fattorizzabile
			match('[');
            optlist();
			match(']');
            stat_aux_conditional(lnext_stat);
			break;
			
		case 123: //graffa aperta
			match('{');
            statlist(lnext_stat);
			match('}');
			break;
		
		case Tag.EOF: break; 
		
		default: error("ERROR IN Stat");
		}
    }
    
    void stat_aux_conditional(int lnext_stat){ //parte non fattorizzabile di conditional
        switch(look.tag){
            case 264:  //else
            match(264); 
            stat(); 
            match(267);
            code.emitLabel(lnext_stat); //sarà il ramo FALSE della conditional
            break;

            case 267:
            match(267); //end
            code.emitLabel(lnext_stat); //sarà il ramo FALSE della conditional
            break;

            case Tag.EOF: break; 
		
		    default: error("ERROR IN stat_aux_conditional");
        }
    }
    
    private void idlist(int lnext_stat){ //check || inserisce in st i nuovi address per gli ID var creandoli con count
        int lnext_idlist = lnext_stat;
        switch(look.tag) {
            case Tag.ID:
                
                int address_ID = st.lookupAddress(((Word)look).lexeme);
                
                    if (address_ID==-1) { //non ha trovato l'id nella symbol table
                        address_ID = count; //ci assegna il count corrente e lo inserisce
                        st.insert(((Word)look).lexeme,count++);
                    }
                    
                    match(Tag.ID);
                    idlistp(lnext_idlist);
                    break;
                
            default: error("Error in ID");
            }

    }
    
    private void idlistp(int lnext_idlist){
    	
    	switch(look.tag){
    		
    		case 44 : match(44); //comma
                idlist(lnext_idlist); //se leggo , vuol dire che c'è un altro id
    			break;	
    		
            case ';': break;
            case ']': break;
            case '}': break;
            case 267: break; //end 
            case 262: break; //option
            case Tag.EOF: break; //EOF
            
                default: error("error in , between id list.");
    	}
    }
    
    private void optlist(){
    	//LISTA DI OPZIONI
        int lnext_optlist = code.newLabel(); //Crea nuova label per next di opzioni intermedie
        switch(look.tag){
            
            case 262: //option
                optitem(lnext_optlist); //optitem prende la nuova label
                code.emitLabel(lnext_optlist);
                optlistp(); 
                break;
            
            default: error("error in optlist");
            }
        }
    
    private void optlistp(){
    
    	switch(look.tag){
    		
    		case 262 : //option
            //le opzioni trovate sono più di una, devo ridare una label.
                int lnext_optlistp = code.newLabel();
                optitem(lnext_optlistp);
                code.emitLabel(lnext_optlistp);
                optlistp();
    			break;	
    			
                case ']' : break; //empty, no "option"
            
                default: error("error in between opt listing.");
    	}
    }
    
    private void optitem(int lnext_optlist){ //non NULL();
    	
    	match(262); //options
		match('(');
        bexpr(lnext_optlist);
		match(')');
        match(263); //do
		stat();
    }
    
    private void bexpr(int lnext_cond){
        int label_false = lnext_cond; //di default FALSE è istruzione dopo il conditional
        switch(look.tag){

            case Tag.RELOP:
                int label_true = code.newLabel(); //true = SALTO A NEW LABEL
                //!!! false = RAMO SUCCESSIVO già prima di boolean
                String b_operator_lexeme = ((Word)look).lexeme; //salvo il RELOP a seconda del lessema prende un ramo if

                match(258); // matcha RELOP
                expr(); //si occuperanno di fare il push degli operandi
                expr();

                if (b_operator_lexeme.equalsIgnoreCase(Word.or.lexeme)) code.emit(OpCode.ior, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.and.lexeme)) code.emit(OpCode.iand, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.lt.lexeme)) code.emit(OpCode.if_icmplt, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.gt.lexeme)) code.emit(OpCode.if_icmpgt, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.eq.lexeme)) code.emit(OpCode.if_icmpeq, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.le.lexeme)) code.emit(OpCode.if_icmple, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.ne.lexeme)) code.emit(OpCode.if_icmpne, label_true);
                if (b_operator_lexeme.equalsIgnoreCase(Word.ge.lexeme)) code.emit(OpCode.if_icmpge, label_true);
                code.emit(OpCode.GOto, label_false); // dopo if SALTO stampa sempre goto false
                code.emitLabel(label_true); 
                break;

            case Tag.EOF: error("ended after bexpr"); 

            default: error("error in bexpr"); 
        }
    }
    
    private void expr(){
        int lnext_expr = code.newLabel();
    	switch(look.tag){
		case '+': 
			match('+');
			match('(');
			exprlist(lnext_expr);
			match(')');
            code.emit(OpCode.iadd);
			break;
    				
		case '*': 
			match('*');
			match('(');
			exprlist(lnext_expr);
			match(')');
            code.emit(OpCode.imul);
			break;
    				
        case '-':
            match('-');
            expr();
            expr();
            code.emit(OpCode.isub);
            break;
    			
			case '/': 	
			match('/');
			expr();
            expr();
            code.emit(OpCode.idiv);
			break;
    					
		case Tag.NUM: /// se è tag NUM allora il token è un numbertok
            code.emit(OpCode.ldc, ((NumberTok)look).value ); //emetto il numero come costante sulla pila
            match(Tag.NUM);
			break;
				
		case Tag.ID: //se è id NON va emesso ma va usato come arg di iload e istore
			
            int address_ID = st.lookupAddress(((Word)look).lexeme); //verificare la sua presenza in symbol table
            if (address_ID==-1) { //non c'è
                address_ID = count; //lo inserisco nella colonna #count indirizzo della tabella
                st.insert(((Word)look).lexeme, count++);
            }  
            code.emit(OpCode.iload, address_ID);//nelle operazioni, vanno emessi prima iload var1 e var2 poi OP

            match(Tag.ID);
			break;
    					
		case Tag.EOF: break; 
		
		default: error("ERROR IN Expr!");
	}
    }
    
    
    private void exprlist(int lnext_expr){
    
    	switch(look.tag){
    		case '+': 
    			expr();
    			exprlistp(lnext_expr);
    			break;
    		
    		case '-': 
    			expr();
    			exprlistp(lnext_expr);
    			break;
    			
    		case '/': 
    			expr();
    			exprlistp(lnext_expr);
    			break;
    			
    		case '*': 
    			expr();
    			exprlistp(lnext_expr);
    			break;
    			
    		case Tag.NUM: 
			expr();
    			exprlistp(lnext_expr);
			break;
				
		case Tag.ID: 
			expr();
    			exprlistp(lnext_expr);
			break;
    			
    		case Tag.EOF: break; 
		
		default: error("ERROR IN Exprlist!");    		
    	}
    }
    
    private void exprlistp(int lnext_expr){
    	
    	switch(look.tag){
    		
    		case 44 : 
                match(44); //comma
    		    exprlist(lnext_expr);
    			break;	
    			
    		case ']': break; 
            case ')': break; 
	
    		default: error("error in , between listing expressions");  
    	}
    }
		
    public static void main(String [] args){
        Lexer lex = new Lexer();

        String path = "input.lft";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            Traduttore translator = new Traduttore(lex, br);
            translator.prog();
            br.close();
        } catch(IOException e) {e.printStackTrace();}
    }
}
