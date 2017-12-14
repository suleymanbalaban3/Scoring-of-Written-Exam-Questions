package Training;

import java.util.Arrays;
import java.util.Vector;

public class Matrix {
	public Vector<Vector<Double>>x;
	public Vector<Vector<Double>>y;
	
	public Matrix(Vector<Vector<Double>>x, Vector<Vector<Double>>y) {
		this.x = x;
		this.y = y;
	}
	private double determinant(Vector<Vector<Double>> matrix) {
		if (matrix.size() != matrix.get(0).size())
			throw new IllegalStateException("invalid dimensions");

		if (matrix.size() == 2)
			return matrix.get(0).get(0) * matrix.get(1).get(1) - matrix.get(0).get(1) * matrix.get(1).get(0);
		System.err.println("buradadeterminant");
		double det = 0;
		for (int i = 0; i < matrix.get(0).size(); i++)
			det += Math.pow(-1, i) * matrix.get(0).get(i)
					* determinant(minor(matrix, 0, i));
		return det;
	}

	private Vector<Vector<Double>> inverse(Vector<Vector<Double>> matrix) {
		Vector<Vector<Double>> inverse = new Vector<Vector<Double>>();
		
		for (int i = 0; i < matrix.size(); i++) {
			Vector<Double> temp = new Vector<>(matrix.size());
			inverse.add(temp);
		}
		for (int i = 0; i < matrix.size(); i++)					// minors and cofactors
			for (int j = 0; j < matrix.get(i).size(); j++)
				inverse.get(i).add(j, Math.pow(-1, i + j)* determinant(minor(matrix, i, j)));

		double det = 1.0 / determinant(matrix);					// adjugate and determinant
		for (int i = 0; i < inverse.size(); i++) {
			System.err.println("buradainverse");
			for (int j = 0; j <= i; j++) {
				double temp = inverse.get(i).get(j);
				inverse.get(i).set(j, inverse.get(j).get(i) * det);
				inverse.get(j).set(i, temp * det);
			}
		}
		return inverse;
	}

	private Vector<Vector<Double>> minor(Vector<Vector<Double>> matrix, int row, int column) {		
		Vector<Vector<Double>> minor = new Vector<Vector<Double>>();
		
		for (int i = 0; i < matrix.size()-1; i++) {
			System.err.println("buradaminor");
			Vector<Double> temp = new Vector<>(matrix.size()-1);
			minor.add(temp);
		}
		
		for (int i = 0; i < matrix.size(); i++)
			for (int j = 0; i != row && j < matrix.get(i).size(); j++)
				if (j != column)
					minor.get(i < row ? i : i - 1).add(j < column ? j : j - 1, matrix.get(i).get(j));
		return minor;
	}

	private Vector<Vector<Double>> multiply(Vector<Vector<Double>> matrix1, Vector<Vector<Double>> matrix2) {
		if (matrix1.get(0).size() != matrix2.size())
			throw new IllegalStateException("invalid dimensions");

		Vector<Vector<Double>> matrix = new Vector<Vector<Double>>();
		
		for (int i = 0; i < matrix1.size(); i++) {
			Vector<Double> temp = new Vector<>(matrix2.get(0).size());
			matrix.add(temp);
		}
		for (int i = 0; i < matrix1.size(); i++) {
			System.err.println("buradamultiply");
			for (int j = 0; j < matrix2.get(0).size(); j++) {
				double sum = 0;
				for (int k = 0; k < matrix1.get(i).size(); k++)
					sum += matrix1.get(i).get(k) * matrix2.get(k).get(j);
				matrix.get(i).add(j, sum);
			}
		}
		return matrix;
	}
	public Vector<Vector<Double>> lineerRegression(){
		return multiply(inverse(multiply(transpose(x), x)), multiply(transpose(x), y));
	}
	private Vector<Vector<Double>> transpose(Vector<Vector<Double>> matrix) {
		Vector<Vector<Double>> transpose = new Vector<Vector<Double>>();
		
		for (int i = 0; i < matrix.get(0).size(); i++) {
			System.err.println("buradaTranspoze");
			Vector<Double> temp = new Vector<>(matrix.size());
			transpose.add(temp);
		}
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				transpose.get(j).add(i, matrix.get(i).get(j));
			}
		}
		
		return transpose;
	}
}