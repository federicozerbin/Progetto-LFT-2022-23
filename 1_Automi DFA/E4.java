/*
* DFA "E4" è una modifica del DFA "E3". Riconosce le stesse stringhe ma accetta anche la presenza del carattere 
* spazio (ch = 32) tra matricola e cognome, all'inizio e alla fine della stringa.
*
* @author  Federico Zerbin, 902129
*/

public class E4{
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	
	System.out.println("riconosco matricole T2 e T3 con spazi...");

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    
		    case 0:
		    	if (ch > 47 && ch < 58){
				if(ch % 2 == 0)
		    			state = 1;
		    		else state = 3;
				}
				else if (ch > 96 && ch < 123 || ch == 32){ //aggiunto se spazio
					state = 0;
				}
				else state = -1;
				break;
				
			case 1:
				if (ch > 47 && ch < 58){  
					if(ch % 2 != 0)
							state = 3;
				}
				
				else if ((ch > 64 && ch < 76) || (ch > 96 && ch < 108)){ //A-K, a-k
					state = 2;
				}
				
				else if(ch == 32)
					state = 5;
					
				else state = -1;
				break;
			 
			case 2:
				if ((ch > 96 && ch < 123) || ch == 32){ //aggiunto se spazio
					state = 2;
				}
				
				else state = -1;
				break;
				
			case 3:
				if ((ch > 75 && ch < 91) || (ch > 107 && ch < 123)){  //L-Z,l-z
					state = 4;
				}
				
				else if (ch > 47 && ch < 58){
					if(ch % 2 == 0)
							state = 1;
				}
				else if(ch == 32)
					state = 5;
				else state = -1;
				break;
			
			case 4:
				if ((ch > 96 && ch < 123) || ch == 32){ //aggiunto se spazio
					state = 4;
				}
				
				else state = -1;
				break;
			
			case 5:
				if(ch == 32)
					state = 5;
				else if ((ch > 64 && ch < 76) || (ch > 96 && ch < 108)){ //A-K, a-k
					state = 2;
				}
				else if ((ch > 75 && ch < 91) || (ch > 107 && ch < 123)){  //L-Z,l-z
					state = 4;
				}
				else state = -1;
		}
	}
	return state == 2 || state == 4;
    }


    public static void main(String[] args)
    {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
