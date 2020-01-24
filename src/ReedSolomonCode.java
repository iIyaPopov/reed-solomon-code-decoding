import java.util.*;
import java.io.*;

public class ReedSolomonCode {
	private GaluaField GF;
	private int n;
	private int k;
	private int d;
	private int t;
	private int[] polynom;
	private int[] roots;
	
	public ReedSolomonCode(int n, int d) {
		this.n = n;
		this.k = n - d + 1;
		this.d = d;
		this.t = (int) Math.floor (d / 2);
		int q = n + 1;
		if (!HelpFunctions.isPrime (q)) {
			throw new RuntimeException ("q is bad. Input new n. (Note q = n + 1)");	
		}
		this.GF = new GaluaField(q);
		this.roots = new int[this.d - 1];
		int shift = 1;
		for (int i = 0; i < this.d - 1; i++) {
			this.roots[i] = this.GF.getAlphaDegreeElement (i + shift);
		}
		this.polynom = getPolynom (this.roots);
		System.out.println (this.GF);
	}
	
	public int[] encode (int[] a) {
		int modul = n + 1;
		return HelpFunctions.polynomMultiply (a, this.polynom, modul);
	}
	
	public int[] addError (int[] codeWord, int[] error) {
		int[] res = new int[codeWord.length];
		int q = n + 1;
		for (int i = 0; i < codeWord.length; i++) {
			res[i] = (codeWord[i] + error[i]) % q;
		}
		return res;
	}
	
	public int[] decode (int[] codeWord) {
		int[] res = getSyndrom (codeWord);
		return res;
	}
	
	private int[] getPolynom (int[] roots) {
		int[] res = new int[2];
		res[0] = -roots[0];
		res[1] = 1;
		int[] tmp = new int[2];
		int modul = this.n + 1;
		tmp[1] = 1;
		for (int i = 1; i < roots.length; i++) {
			tmp[0] = -roots[i];
			res = HelpFunctions.polynomMultiply (res, tmp, modul);
		}
		return res;
	}
	
	public int getRoot () {
		return this.GF.getPrimitive ();
	}
	
	private int[] getSyndrom (int[] codeWord) {
		int[] res = new int[this.d - 1];
		int q = this.n + 1;
		for (int i = 0; i < this.d - 1; i++) {
			res[i] = func (codeWord, this.roots[i], q) % q;
		}
		return res;
	}
	
	private int func (int[] codeWord, int root, int modul) {
		int res = 0;
		for (int i = 0; i < codeWord.length; i++) {
			res += (Math.pow (root, i) * codeWord[i]) % modul;
		}
		return res;
	}
	
	public void printCodeBookToFile (String filename) throws FileNotFoundException {
		StringBuilder str = new StringBuilder ();
		int q = this.n + 1;
		int count = (int) Math.pow (q, this.k);
		for (int i = 0; i < count; i++) {
			int[] tmp = HelpFunctions.transform (i, q, this.k);
			int[] code = encode (tmp);
			str.append (Arrays.toString(tmp) + "\t" + Arrays.toString (code) + "\n");
		}
		PrintWriter pw = new PrintWriter (filename);
		pw.print (str.toString ());
		pw.close ();
	}
	
	public int getT() {
		return this.t;
	}
	
	@Override
	public String toString () {
		StringBuilder str = new StringBuilder ();
		str.append ("(n, k, d, t) = (" + this.n + ", " + this.k + ", " + this.d + ", " + this.t + ")\n");
		str.append ("GF (" + (this.n + 1) + ")\n");
		str.append ("Polynom: " + HelpFunctions.polynomToString (this.polynom) + "\n");
		str.append ("Roots of polynom: " + Arrays.toString (roots));
		return str.toString ();
	}
}