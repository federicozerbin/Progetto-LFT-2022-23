/*
* DFA "E3" riconosce il linguaggio di stringhe che contengono un numero di matricola (almeno una cifra) seguito subito da un cognome (almeno una lettera), 
* dove la combinazione di matricola e cognome corrisponde a studenti del turno 2 o del turno 3 del lab di LFT. 
* • Turno T2: cognomi la cui iniziale è compresa tra A e K, e il numero di matricola è pari;
* • Turno T3: cognomi la cui iniziale è compresa tra L e Z, e il numero di matricola è dispari;
* Note: 
*	ch compreso tra 47 e 58 => cifre in ASCII da 0 a 9
*	ch compreso tra 96 e 108 => lettere in ASCII da 'a' a 'k' minuscole
*	ch compreso tra 64 e 76 => lettere in ASCII da 'A' a 'K' maiuscole
*	ch compreso tra 107 e 123 => lettere in ASCII da 'l' a 'z' minuscole
*	ch compreso tra 75 e 91 => lettere in ASCII da 'L' a 'Z' maiuscole
*
* @author  Federico Zerbin, 902129
*/

public class E3{
	public static boolean scan(String s)
    {
		int state = 0;
		int i = 0;
		
		System.out.println("riconosco matricole T2 e T3...");

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);

			switch (state) {
			
				case 0:
					if (ch > 47 && ch < 58){
						if(ch % 2 == 0) state = 1;
						else state = 3;
					}
					else if (ch > 96 && ch < 123){
						state = 0;
					}
					else state = -1;
					break;
					
				case 1:
					if (ch > 47 && ch < 58){  
						if(ch % 2 != 0) state = 3;
					}
					else if ((ch > 64 && ch < 76) || (ch > 96 && ch < 108)){ //A-K, a-k
						state = 2;
					}
					else state = -1;
					break;
				
				case 2:
					if (ch > 96 && ch < 123){
						state = 2;
					}
					else state = -1;
					break;
				
				case 3:
					if ((ch > 75 && ch < 91) || (ch > 107 && ch < 123)){  //L-Z,l-z
						state = 4;
					}
					else if (ch > 47 && ch < 58){
						if(ch % 2 == 0) state = 1;
					}
					else state = -1;
					break;
					
				case 4:
					if (ch > 96 && ch < 123){
						state = 4;
					}
					else state = -1;
					break;
			}
		}
		return state == 2 || state == 4;
    }


    public static void main(String[] args)
    {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
