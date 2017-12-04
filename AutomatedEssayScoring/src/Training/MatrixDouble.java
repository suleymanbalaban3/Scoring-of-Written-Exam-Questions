package Training;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class MatrixDouble {

	private static double determinant(double[][] matrix) {
		if (matrix.length != matrix[0].length)
			throw new IllegalStateException("invalid dimensions");

		if (matrix.length == 2)
			return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

		double det = 0;
		for (int i = 0; i < matrix[0].length; i++)
			det += Math.pow(-1, i) * matrix[0][i]
					* determinant(minor(matrix, 0, i));
		return det;
	}

	private static double[][] inverse(double[][] matrix) {
		double[][] inverse = new double[matrix.length][matrix.length];
		System.out.println("size x :" + matrix.length + "sizeY :" + matrix[0].length);
		// minors and cofactors
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				inverse[i][j] = Math.pow(-1, i + j)
						* determinant(minor(matrix, i, j));
		System.out.println("indevrse1 :" + inverse[0][0]);
		// adjugate and determinant
		double det = 1.0 / determinant(matrix);
		System.out.println("det :" + det);
		for (int i = 0; i < inverse.length; i++) {
			for (int j = 0; j <= i; j++) {
				double temp = inverse[i][j];
				inverse[i][j] = inverse[j][i] * det;
				inverse[j][i] = temp * det;
			}
		}System.out.println("indevrse2 :" + inverse[0][0]);

		return inverse;
	}

	private static double[][] minor(double[][] matrix, int row, int column) {
		double[][] minor = new double[matrix.length - 1][matrix.length - 1];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; i != row && j < matrix[i].length; j++)
				if (j != column)
					minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
		return minor;
	}

	private static double[][] multiply(double[][] a, double[][] b) {
		if (a[0].length != b.length)
			throw new IllegalStateException("invalid dimensions");

		double[][] matrix = new double[a.length][b[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				double sum = 0;
				for (int k = 0; k < a[i].length; k++)
					sum += a[i][k] * b[k][j];
				matrix[i][j] = sum;
			}
		}

		return matrix;
	}

	private static double[][] rref(double[][] matrix) {
		double[][] rref = new double[matrix.length][];
		for (int i = 0; i < matrix.length; i++)
			rref[i] = Arrays.copyOf(matrix[i], matrix[i].length);

		int r = 0;
		for (int c = 0; c < rref[0].length && r < rref.length; c++) {
			int j = r;
			for (int i = r + 1; i < rref.length; i++)
				if (Math.abs(rref[i][c]) > Math.abs(rref[j][c]))
					j = i;
			if (Math.abs(rref[j][c]) < 0.00001)
				continue;

			double[] temp = rref[j];
			rref[j] = rref[r];
			rref[r] = temp;

			double s = 1.0 / rref[r][c];
			for (j = 0; j < rref[0].length; j++)
				rref[r][j] *= s;
			for (int i = 0; i < rref.length; i++) {
				if (i != r) {
					double t = rref[i][c];
					for (j = 0; j < rref[0].length; j++)
						rref[i][j] -= t * rref[r][j];
				}
			}
			r++;
		}

		return rref;
	}

	private static double[][] transpose(double[][] matrix) {
		double[][] transpose = new double[matrix[0].length][matrix.length];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				transpose[j][i] = matrix[i][j];
		return transpose;
	}
	public static Vector<Vector<Double>> generateRegression(Vector<Vector<Double>>independValues, Vector<Vector<Double>>dependValues){
		double [][] x = new double[independValues.size()][independValues.get(0).size()];	
		double [][] y = new double[dependValues.size()][1];
		
		for (int i = 0; i < independValues.size(); i++) {
			for (int j = 0; j < independValues.get(i).size(); j++) {
				x[i][j] = independValues.get(i).get(j);
			}
		}
		for (int i = 0; i < dependValues.size(); i++) {
			for (int j = 0; j < dependValues.get(i).size(); j++) {
				y[i][j] = dependValues.get(i).get(j);
			}
		}
	
		double [][] res =  multiply(
				multiply(invert(multiply(transpose(x), x)), transpose(x)), y);
		Vector<Vector<Double>> result = new Vector<Vector<Double>>();
	
		for (int i = 0; i < res.length; i++) {
			Vector<Double> temp = new Vector<Double>();
			for (int j = 0; j < res[i].length; j++) {
				temp.add(res[i][j]);
			}
			result.add(temp);
		}
		return result;
	}
	public static void main(String[] args) {

		// example 3 - solving a normal equation for linear regression
		/*double[][] x = { { 4,5,4}, { 4,5,3},
				{4,9,8},{ 5,8,7},{ 5,5,9},
				{ 8,10,8},{9,7,13},{ 5,14,14},
				{ 14,6,12}, {9,9,9}};
		double[][] y = { { 5 }, { 4 }, { 9 }, {3}, {5}, {5}, {8},{5}, {5}, {12}};
*/
		double [][] x = new double[10][11];
		double [][] y = new double[10][1];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 11; j++) {
				double rangeMin = -5;
				double rangeMax = 5;
				Random r = new Random();
				double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
				x[i][j] = randomValue;
			}
			y[i][0] = func(x[i]);
		}
		for (int i = 0; i < 10; i++) {
			System.out.print(y[i][0] + " = ");
			for (int j = 0; j < 11; j++) {
				System.out.print(x[i][j] + "x"+(i+1) + "  ");
			}
			System.out.println();
		}
		/*double[][] transposeX = transpose(x);*/
		
		double[][] matrix = multiply(
				multiply(invert(multiply(transpose(x), x)), transpose(x)), y);

		for (double[] i : matrix)
			System.out.println(Arrays.toString(i));
	}
	public static double func(double [] values) {
		double res = 0.0;
		for (int i = 0; i < values.length; i++) {
			res += values[i];
		}
		return res;
	}
	public static double[][] invert(double a[][]) 
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) 
            b[i][i] = 1;
 
 // Transform the matrix into an upper triangle
        gaussian(a, index);
 
 // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                    	    -= a[index[j]][i]*b[index[i]][k];
 
 // Perform backward substitutions
        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }
 
// Method to carry out the partial-pivoting Gaussian
// elimination.  Here index[] stores pivoting order.
 
    public static void gaussian(double a[][], int index[]) 
    {
        int n = index.length;
        double c[] = new double[n];
 
 // Initialize the index
        for (int i=0; i<n; ++i) 
            index[i] = i;
 
 // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i) 
        {
            double c1 = 0;
            for (int j=0; j<n; ++j) 
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
 
 // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j) 
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i) 
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) 
                {
                    pi1 = pi0;
                    k = i;
                }
            }
 
   // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) 	
            {
                double pj = a[index[i]][j]/a[index[j]][j];
 
 // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;
 
 // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }
}