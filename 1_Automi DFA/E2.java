/*
* DFA "E2" riconosce il linguaggio degli identificatori in un linguaggio in stile Java: 
* un identificatore è una sequenza non vuota di lettere, numeri, ed il simbolo di “underscore” _ 
* non comincia con un numero e che non può essere composto solo dal simbolo _. 
*
* Note: 
*	ch compreso tra 47 e 58 => cifre in ASCII da 0 a 9
*	ch copreso tra 96 e 123 => lettere in ASCII da 'a' a 'z' minuscole
*
* @author  Federico Zerbin, 902129
*/

public class E2{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	
	System.out.println("riconosco identificatori Java... ...");

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    
		    case 0:
		    	if (ch > 96 && ch < 123){
				state = 1;
			}
			
			else if (ch > 47 && ch < 58){
		    		state = -1;
			}
			
			else if (ch == 95){
				state = 2; 
			}
			else state = -1;
			break;
				
		   case 1:
			if (ch > 96 && ch < 123){
				state = 1;
			}
			
			else if (ch > 47 && ch < 58){
		    		state = 1;
			}
			
			else if (ch == 95){
				state = 1; 
			}
			else state = -1;
			break;
			 
		    case 2:
			if (ch > 96 && ch < 123){
				state = 1;
			}
			
			else if (ch > 47 && ch < 58){
		    		state = 1;
			}
			
			else if (ch == 95){
				state = 2; 
			}
			else state = -1;
			break;
		}
	}
	return state == 1;
    }


    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
