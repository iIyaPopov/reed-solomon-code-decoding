import java.util.Arrays;

public class Determinant {
	
	public static int count (int rowNum, int colNum, int depth, int[][] m) {
		int h = m.length;
		int w = m[0].length;
		m = swapCols (colNum, m);
		if (depth == m.length - 2) {
			return (m[h-2][0] * m[h-1][1] - m[h-2][1] * m[h-1][0]);
		} else {
			int res = 0;
			for (int j = 0; j < w - depth; j++) {
				int tmp = count (rowNum + 1, j, depth + 1, m);
				res += (double) m[rowNum][j] * Math.pow (-1.0, j) * tmp;
			}
			return res;
		}
	}
	
	public static int[][] swapCols (int colNum, int[][] m) {
		int h = m.length;
		int w = m[0].length;
		int[][] res = new int[h][w];
		for (int j = 0; j < w; j++) {
			for (int i = 0; i < h; i++) {
				if (j == colNum) {
					res[i][w-1] = m[i][j];
				} else if (j >= colNum) {
					res[i][j-1] = m[i][j];
				} else {
					res[i][j] = m[i][j];
				}
			}
		}
		return res;
	}
}