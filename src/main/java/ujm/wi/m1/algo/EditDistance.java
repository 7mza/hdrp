package ujm.wi.m1.algo;

import org.springframework.stereotype.Component;

@Component
public class EditDistance {

	public int getDistance(String vertical, String horizontal) {
		int[][] matrix = this.initMatrix(vertical.length() + 1, horizontal.length() + 1);
		for (int i = 1; i <= vertical.length(); i++)
			for (int j = 1; j <= horizontal.length(); j++)
				if (vertical.charAt(i - 1) == horizontal.charAt(j - 1))
					matrix[i][j] = matrix[i - 1][j - 1];
				else
					matrix[i][j] = this.min3(matrix[i - 1][j - 1], matrix[i - 1][j], matrix[i][j - 1]) + 1;
		return matrix[vertical.length()][horizontal.length()];
	}

	private int min2(int a, int b) {
		int min = a;
		if (b <= min)
			min = b;
		return min;
	}

	private int min3(int a, int b, int c) {
		int min = min2(a, b);
		return this.min2(min, c);
	}

	private int[][] initMatrix(int rows, int columns) {
		int[][] matrix = new int[rows][columns];
		for (int i = 0; i < rows; i++)
			matrix[i][0] = i;
		for (int i = 0; i < columns; i++)
			matrix[0][i] = i;
		return matrix;
	}

}
