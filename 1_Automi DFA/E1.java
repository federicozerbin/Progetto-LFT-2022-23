/*
* Versione di TreZeri.java modificata:
* riconosce stringhe di 0 e 1 in cui non esiste una sequenza di 3 zeri consecutivi.
*
* @author  Federico Zerbin, 902129
*/
public class E1
{
    public static boolean scan(String s)
    {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);

			switch (state) {
				case 0:
					if (ch == '0')
						state = 1;
					else if (ch == '1')
						state = 0;
					else
						state = -1;
					break;

				case 1:
					if (ch == '0')
						state = 2;
					else if (ch == '1')
						state = 1;
					else
						state = -1;
					break;

				case 2:
					if (ch == '0')
						state = 3;
					else if (ch == '1')
						state = 0;
					else
						state = -1;
					break;

				case 3:
					if (ch == '0' || ch == '1')
						state = -1;
					break;
			}
		}
		return (state == 0 || state == 1 || state == 2);
    }

    public static void main(String[] args)
    {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
