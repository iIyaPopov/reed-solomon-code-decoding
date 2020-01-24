import java.math.BigInteger;

public class InverseMatrix {
	private static int det;
	
	public static int[][] get (int[][] m, int modul) {
		det = Determinant.count (0, m[0].length, 0, m);
		System.out.println ("Det = " + det);
		BigInteger tmp = BigInteger.valueOf ((long) det);
		BigInteger inverse = tmp.modInverse (BigInteger.valueOf ((long) modul));
		Integer detInverse = new Integer (inverse.toString (10));
		int h = m.length;
		int w = m[0].length;
		int[][] res = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				res[i][j] = (m[i][j] * detInverse) % modul;
			}
		}
		return res;
	}
}