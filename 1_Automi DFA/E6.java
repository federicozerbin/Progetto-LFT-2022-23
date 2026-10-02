/*
* DFA "E6" riconosce il linguaggio di stringhe formate da 'a' e 'b' in cui nelle ultime 3 posizioni è 
* presente almeno una a. accetta anche stringhe con length < 3 solo se un simbolo è 'a'.
*
* Nota: 
* l'unico modo che ho trovato fattibile per progettare l'automa E6 è stato quello di "pensarlo al contrario".
* Una volta ricevuta la stringa in input e memorizzata in s, la lettura da parte del DFA parte dall'ultima lettera di s e procede a ritroso.
* è quindi sufficiente scorrere al massimo tre stati e verificare in questi la presenza di 'a'.
* @author  Federico Zerbin, 902129
*/

public class E6
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = s.length() - 1;

	while (state >= 0 && i >= 0) {
	    final char ch = s.charAt(i--);

	    switch (state) {

			case 0:
				if (ch == 'a')
					state = 1;
				else if (ch == 'b')
					state = 2;
				else
					state = -1;
				break;

			case 1:
				if (ch == 'a' || ch == 'b')
					state = 1;
				else
					state = -1;
				break;

			case 2:
				if (ch == 'a')
					state = 1;
				else if (ch == 'b')
					state = 3;
				else
					state = -1;
				break;

			case 3:
				if (ch == 'a')
					state = 1;
				else if (ch == 'b')
					state = -1;
				else
					state = -1;
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
