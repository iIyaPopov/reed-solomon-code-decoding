import java.util.*;

public class GaluaField {
	private int[] elements;
	private int[] ords;
	private int q;
	private int primitive;
	private int[] alphaDegrees;
	
	public GaluaField (int q) {
		this.q = q;
		this.elements = new int[this.q];
		this.ords = new int[this.q];
		this.primitive = this.q;
		for (int i = 0; i < this.q; i++) {
			this.elements[i] = i;
			this.ords[i] = getOrd (i);
			if (this.ords[i] == this.q - 1 && this.primitive > i) {
				this.primitive = i;
			}
		}
		this.alphaDegrees = new int[this.q];
		this.alphaDegrees[0] = 1;
		for (int i = 1; i < this.q; i++) {
			this.alphaDegrees[i] = (this.alphaDegrees[i-1] * this.primitive) % this.q;
		}
	}
	
	public int getAlphaDegreeElement (int degree) {
		return this.alphaDegrees[degree];
	}
	
	public int getPrimitive () {
		return this.primitive;
	}
	
	private int getOrd (int value) {
		if (value == 0) return -1;
		int tmp = 1;
		for (int i = 1; i < this.q; i++) {
			tmp = (tmp * value) % this.q;
			if (tmp == 1) {
				return i;
			}
		}
		return -1;
	}
	
	public int[] getPolynom (int degree) {
		int maxPolynomDegree = degree;
		int[] res = new int[maxPolynomDegree];
		res[0] = -this.alphaDegrees[0];
		res[1] = 1;
		int[] b = new int[2];
		for (int i = 1; i < maxPolynomDegree; i++) {
			b[0] = -this.alphaDegrees[i];
			b[1] = 1;
			res = HelpFunctions.polynomMultiply (res, b, this.q);
		}
		return res;
	}
	
	@Override
	public String toString () {
		StringBuilder str = new StringBuilder();
		str.append("GF (" + this.q + ")\n");
		str.append("ord (alpha) = " + (this.q - 1) + "\n");
		str.append("+---------------+---------------+\n");
		str.append("| Element\t| Ord\t\t|\n");
		str.append("+---------------+---------------+\n");
		for (int i = 0; i < this.q; i++) {
			if (this.ords[i] != -1) {
				str.append("| " + elements[i] + "\t\t| " + this.ords[i] + "\t\t|\n");
			} else {
				str.append("| " + elements[i] + "\t\t| " + "-" + "\t\t|\n");
			}
		}
		str.append("+-------------------------------+\n");
		str.append("Primitive element: " + this.primitive + "\n");
		
		str.append("+---------------+---------------+\n");
		str.append("| Alpha degrees\t| Element\t|\n");
		str.append("+---------------+---------------+\n");
		for (int i = 0; i < this.q; i++) {
			str.append("| " + i + "\t\t| " + this.alphaDegrees[i] + "\t\t|\n");
		}
		str.append("+-------------------------------+\n");
		return str.toString();
	}
}