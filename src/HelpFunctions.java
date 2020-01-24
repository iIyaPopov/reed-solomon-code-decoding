public class HelpFunctions {
	public static int[] polynomMultiply (int[] a, int[] b, int modul) {
		int size = a.length + b.length - 1;
		int[] res = new int[size];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				res[i+j] += a[i] * b[j];
			}
		}
		for (int i = 0; i < size; i++) {
			res[i] = res[i] % modul;
			if (res[i] < 0) res[i] += modul;
		}
		return res;
	}
	
	public static int[] polynomAdder (int[] a, int[] b, int scalar, int modul) {
		for (int i = 0; i < b.length; i++) {
			b[i] = (b[i] * scalar) % modul;
		}
		int[] x = a;
		int[] y = b;
		if (a.length < b.length) {
			x = b;
			y = a;
		}
		int[] res = x;
		for (int i = 0; i < y.length; i++) {
			res[i] = (res[i] + y[i]) % modul;
			if (res[i] < 0) res[i] += modul;
		}
		return res;
	}
	
	public static boolean isPrime (int a) {
		for (int i = 2; i <= (int) Math.sqrt(a); i++) {
			if (a % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static String polynomToString (int[] polynom) {
		StringBuilder str = new StringBuilder ();
		str.append ("x^" + (polynom.length - 1));
		for (int i = polynom.length - 2; i > 1; i--) {
			if (polynom[i] < 0) {
				str.append (" - " + Math.abs (polynom[i]) + "x^" + i);
			} else {
				str.append (" + " + Math.abs (polynom[i]) + "x^" + i);
			}
		}
		if (polynom.length >= 1) {
			if (polynom[1] < 0) {
				str.append (" - " + Math.abs (polynom[1]) + "x");
			} else {
				str.append (" + " + Math.abs (polynom[1]) + "x");
			}
			if (polynom[0] < 0) {
				str.append (" - " + Math.abs (polynom[0]));
			} else {
				str.append (" + " + Math.abs (polynom[0]));
			}
		}
		return str.toString ();
	}
	
	public static int[] transform (int num, int q, int k) {
		if (num > Math.pow (q, k)) {
			throw new RuntimeException ("Error transform num to int[]");
		}
		int[] res = new int[k];
		if (num < q) {
			res[0] = num;
			return res;
		}
		while (num >= q) {
			num -= q;
			res[1]++;
			for (int i = 1; i < k - 1; i++) {
				if (res[i] >= q) {
					res[i] -= q;
					res[i+1]++;
				}
			}
		}
		res[0] = num;
		return res;
	}
	
	public static int getFunctionResult (int[] coef, int x, int p) {
		int res = 0;
		for (int i = 0; i < coef.length; i++) {
			//res = res.add (x.modPow (int.valueOf ((long) i+1), p).multiply (coef[i]));
			res += (Math.pow (x, i+1) * coef[i]) % p;
		}
		res = (res + p) % p;
		return res;
	}
}