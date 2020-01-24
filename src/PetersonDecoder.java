import java.math.BigInteger;
import java.util.Arrays;

public class PetersonDecoder {
	private int t;
	private int[] codeWord;
	private int root;
	private int modul;
	private int[] syndrom;
	
	public PetersonDecoder (int t, int root) {
		this.t = t;
		this.root = root;
	}
	
	public int[] decode (int[] syndrom, int modul) {
		int v = this.t;
		this.syndrom = syndrom;
		this.modul = modul;
		Jama.Matrix m = null;
		for (;;) {
			if (v < 1) {
				throw new RuntimeException ("NOT FOUND");
			}
			m = new Jama.Matrix (v, v);
			for (int i = 0; i < v; i++) {
				for (int j = 0; j < v; j++) {
					m.set (i, j, syndrom[i+j]);
				}
			}
			if (m.det () % modul != 0) {
				break;
			}
			v--;
		}
		System.out.print ("M:");
		m.print (3, 1);
		int det = (int) Math.floor (m.det () + 0.5);
		System.out.println ("Det = " + det);
		Jama.Matrix im = m.inverse ().times (det);
		System.out.print ("Inverse M:");
		im.print (3, 1);
		
		//Нахождение обратного элемента по модулю к det
		BigInteger tmp = BigInteger.valueOf ((long) det);
		Integer iDet = new Integer (tmp.modInverse (BigInteger.valueOf ((long) modul)).toString (10));
		System.out.println ("Inverse det = " + iDet);
		im = im.times (iDet);
		
		//Приведение всех чисел в положительные
		for (int i = 0; i < im.getRowDimension (); i++) {
			for (int j = 0; j < im.getColumnDimension (); j++) {
				im.set (i, j, im.get(i, j) % modul);
				im.set (i, j, (im.get(i, j) + modul) % modul);
			}
		}
		System.out.print ("Inverse M:");
		im.print (3, 1);
		
		//Нахождение многочлена локаторов ошибок
		//System.out.println (v);
		Jama.Matrix _s = new Jama.Matrix (v, 1);
		for (int i = v; i < 2*v; i++) {
			_s.set (i - v, 0, -syndrom[i]);
		}
		System.out.print ("s:");
		_s.print (3, 1);
		Jama.Matrix l = im.times (_s);
		for (int i = 0; i < l.getRowDimension (); i++) {
			for (int j = 0; j < l.getColumnDimension (); j++) {
				l.set (i, j, l.get(i, j) % modul);
				l.set (i, j, (l.get(i, j) + modul) % modul);
			}
		}
		System.out.print ("Lambdas:");
		l.print (3, 1);
		
		//Нахождение корней многочлена локаторов ошибок
		int size = l.getRowDimension ();
		int[] polynom = new int[size + 1];
		polynom[0] = 1;
		for (int i = 0; i < size; i++) {
			polynom[size - i] = (int) l.get (i, 0);
		}
		System.out.println ("Locator polynom: " + Arrays.toString (polynom));
		int[] locatorRoots = new int[modul-1];
		int[] positions = new int[modul-1];
		int count = 0;
		for (int i = 0; i < modul - 1; i++) {
			if (HelpFunctions.getFunctionResult (polynom, (int) Math.pow (root, i), modul) == 0) {
				count++;
				locatorRoots[i] = 1;
				int pos = -i + (modul - 1);
				positions[pos] = 1;
			}
		}
		if (count == 0) {
			throw new RuntimeException ("Locators polynom: founded 0 roots");
		}
		int[] numPositions = new int[count];
		for (int i = 0, j = 0; i < modul - 1; i++) {
			if (positions[i] == 1) {
				numPositions[j++] = i;
			}
		}
		//System.out.println (Arrays.toString (locatorRoots));
		System.out.println ("Error positions: " + Arrays.toString (numPositions));
		int[] errorSize = findErrorSize (numPositions, root, v);
		return errorSize;
	}
	
	private int[] findErrorSize (int[] positions, int root, int v) {
		Jama.Matrix m = new Jama.Matrix (v, v);
		for (int i = 0; i < v; i++) {
			for (int j = 0; j < v; j++) {
				m.set (i, j, Math.pow (root, positions[j] * (i+1)) % this.modul);
			}
		}
		System.out.print ("lambda:");
		m.print (3, 1);
		int det = (int) m.det ();
		Jama.Matrix im = m.inverse ().times (det);
		
		//Нахождение обратного элемента по модулю к det
		BigInteger tmp = BigInteger.valueOf ((long) det);
		Integer iDet = new Integer (tmp.modInverse (BigInteger.valueOf ((long) modul)).toString (10));
		System.out.println ("Inverse det = " + iDet);
		
		//System.out.print ("Inverse lambda:");
		//im.print (3, 1);
		im = im.times (iDet);
		
		//Приведение всех чисел в положительные
		for (int i = 0; i < im.getRowDimension (); i++) {
			for (int j = 0; j < im.getColumnDimension (); j++) {
				im.set (i, j, im.get(i, j) % modul);
				im.set (i, j, (im.get(i, j) + modul) % modul);
			}
		}
		System.out.print ("Inverse lambda:");
		im.print (3, 1);
		
		//Нахождение многочлена локаторов ошибок
		Jama.Matrix _s = new Jama.Matrix (v, 1);
		for (int i = 0; i < v; i++) {
			_s.set (i, 0, syndrom[i]);
		}
		System.out.print ("s:");
		_s.print (3, 1);
		Jama.Matrix l = im.times (_s);
		for (int i = 0; i < l.getRowDimension (); i++) {
			for (int j = 0; j < l.getColumnDimension (); j++) {
				l.set (i, j, l.get(i, j) % modul);
				l.set (i, j, (l.get(i, j) + modul) % modul);
			}
		}
		System.out.print ("Error size:");
		l.print (3, 1);
		
		//Значения ошибок
		int size = modul - 1;
		int[] res = new int[size];
		for (int i = 0, j = 0; i < size; i++) {
			if (j < positions.length && positions[j] == i) {
				res[i] = (int) Math.floor (l.get (j++, 0) + 0.5);
			}
		}
		System.out.println ("Founded error vector: " + Arrays.toString (res));
		//l.print (3, 1);
		return res;
	}
}