/*
* DFA "E7" riconosce la stringa "fede", e tutte le stringhe ottenute da questa con la sostituzione 
* di uno dei suoi simboli con un altro (qualsiasi char).
*
* @author  Federico Zerbin, 902129
*/

public class E7 
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
			case 0:
				if (ch == 'f')
					state = 1;
				else
					state = 5;
				break;

			case 1:
				if (ch == 'e')
					state = 2;
				else
					state = 8;
				break;

			case 2:
				if (ch == 'd')
					state = 3;
				else
					state = 10;
				break;

			case 3:
				if(ch == 'e')
					state = 4;
				else
					state = 4;
				break;

			case 5:
				if (ch == 'e')
					state = 6;
				else
					state = -1;
				break;

			case 6:
				if (ch == 'd')
					state = 7;
				else
					state = -1;
				break;

			case 7:
				if (ch == 'e')
					state = 4;
				else
					state = -1;
				break;

			case 8:
				if (ch == 'd')
					state = 9;
				else
					state = -1;
				break;

			case 9:
				if (ch == 'e')
					state = 4;
				else
					state = -1;
				break;

			case 10:
				if (ch == 'e')
					state = 4;
				else
					state = -1;
				break;
	    }
	}
	return state == 4;
    }

    public static void main(String[] args)
    {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
