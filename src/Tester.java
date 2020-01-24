import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class Tester {
	public static void main(String[] args) {
		String CODE_BOOK_FILENAME = "../code_book.txt";
		Scanner sc = new Scanner (System.in);
		/*System.out.println ("Input algorithm parameters:");
		System.out.print ("n = ");
		int n = sc.nextInt ();
		System.out.print ("d = ");
		int d = sc.nextInt ();*/
		int n = 10;
		int d = 5;
		ReedSolomonCode rsc = new ReedSolomonCode (n, d);
		System.out.println (rsc);
		
		/*try {
			rsc.printCodeBookToFile (CODE_BOOK_FILENAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace ();
		}*/

		int[] msg = {4, 7, 1, 3, 1, 2};
		System.out.println ("msg: " + Arrays.toString (msg));
		int[] codeWord = rsc.encode (msg);
		System.out.println ("Code word: " + Arrays.toString (codeWord));
		int[] cw = {0, 7, 1, 6, 0, 0, 8, 4, 5, 1};
		System.out.println ("c: " + Arrays.toString (cw));
		int[] error = {0, 0, 6, 0, 0, 0, 0, 0, 0, 3};
		int modul = n + 1;
		cw = HelpFunctions.polynomAdder (cw, error, 1, modul);
		System.out.println ("e: " + Arrays.toString (error));
		System.out.println ("b: " + Arrays.toString (cw));
		int[] syndrom = rsc.decode (cw);
		System.out.println ("s: " + Arrays.toString (syndrom));
		sc.close ();
		
		
		PetersonDecoder pd = new PetersonDecoder (rsc.getT (), rsc.getRoot ());
		int[] errorVector = pd.decode (syndrom, modul);
		int[] res = HelpFunctions.polynomAdder (cw, errorVector, -1, modul);
		System.out.println ("Result: " + Arrays.toString (res));
		System.out.println ("Result syndrom: " + Arrays.toString (rsc.decode (res)));
	}
}