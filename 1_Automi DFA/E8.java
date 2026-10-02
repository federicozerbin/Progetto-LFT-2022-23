/*
* Il DFA "E8" riconosce costanti numeriche in virgola mobile costituite da due segmenti (il secondo è opzionale):
* • il primo segmento è una sequenza di cifre numeriche che 
* (1) può essere preceduta da un segno + o meno - , 
* (2) può essere seguita da un segno punto . , che a sua volta deve essere seguito da una sequenza non vuota di cifre numeriche;
* • il secondo segmento inizia con il simbolo e, che a sua volta è seguito da una sequenza di cifre numeriche che soddisfa i
* punti (1) e (2) scritti per il primo segmento. Si nota che, sia nel primo segmento, sia in un eventuale secondo segmento, un
* segno punto . non deve essere preceduto per forza da una cifra numerica.
* 
* @author  Federico Zerbin, 902129
*/

public class E8 
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
			case 0:
				if (ch == '-' || ch == '+')
					state = 1;
				else if (ch > 47 && ch < 58)
					state = 2;
				else if(ch == '.')
					state = 3;
				else 
					state = -1;
				break;

			case 1:
				if (ch > 47 && ch < 58)
					state = 2;
				else if(ch == '.')
					state = 3;
				else 
					state = -1;
				break;

			case 2:
				if (ch == 'e')
					state = 6;
				else if (ch > 47 && ch < 58)
					state = 2;
				else if(ch == '.')
					state = 4;
				else 
					state = -1;
				break;

			case 3:
				if (ch > 47 && ch < 58)
					state = 9;
				else 
					state = -1;
				break;

			case 4:
				if (ch > 47 && ch < 58)
					state = 5;
				else 
					state = -1;
				break;

			case 5:
				if (ch > 47 && ch < 58)
					state = 5;
				else 
					state = -1;
				break;

			case 6:
				if (ch == '-' || ch == '+')
					state = 7;
				else if (ch > 47 && ch < 58)
					state = 8;
				else 
					state = -1;
				break;

			case 7:
				if (ch > 47 && ch < 58)
					state = 8;
				else 
					state = -1;
				break;

			case 8:
				if (ch > 47 && ch < 58)
					state = 8;
				else if (ch == '.')
					state = 4;
				else
					state = -1;
				break;

			case 9:
				if (ch > 47 && ch < 58)
					state = 9;
				else if(ch == 'e')
					state = 6;
				else 
					state = -1;
				break;
	    
	    }
	}
	return state == 2 || state == 5 || state == 8 || state == 9 ;
    }

    public static void main(String[] args)
    {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
