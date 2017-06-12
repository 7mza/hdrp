package ujm.wi.m1.algo;

import org.springframework.stereotype.Component;

/*
 * Amine
 */
@Component
public class Freeman {

	public String getFreemanCode(int[][] matrix) {
		/*
		 * int[][] matrixx = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		 * { 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
		 * 0 }, { 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1,
		 * 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0 }, { 0, 0, 0, 0, 1, 1,
		 * 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0,
		 * 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0 }, { 0, 0,
		 * 0, 0, 0, 0, 0, 1, 1, 0, 0 }, { 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 }, {
		 * 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		 * } };
		 */
		String code = "";
		int[] courant = new int[2];
		int direction;
		int[] initPixel = this.getFirstPixel(matrix);
		int[] nouveau = new int[2];
		courant = initPixel;
		direction = 2;
		do {
			direction = (direction + 6) % 8;
			do {
				if (courant[0] == 0 && (direction == 7 || direction == 0 || direction == 1)) {
					direction = (direction + 1) % 8;
				} else if (courant[0] == 199 && (direction == 5 || direction == 4 || direction == 3)) {
					direction = (direction + 1) % 8;
				} else if (courant[1] == 0 && (direction == 7 || direction == 6 || direction == 5)) {
					direction = (direction + 1) % 8;
				} else if (courant[1] == 199 && (direction == 1 || direction == 2 || direction == 3)) {
					direction = (direction + 1) % 8;
				} else {
					nouveau = prochainPixel(courant, direction);
					direction = (direction + 1) % 8;
				}
			} while (!allumer(nouveau, matrix));
			direction = (direction + 7) % 8;
			code = code + direction;
			courant = nouveau;
		} while ((courant[0] != initPixel[0]) || (courant[1] != initPixel[1]));
		return code;
	}

	private int[] getFirstPixel(int[][] matrix) {
		int[] first = new int[2];
		int rows = matrix.length;
		int columns = matrix[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (matrix[i][j] == 1) {
					first[0] = i;
					first[1] = j;
					return first;
				}
			}
		}
		return null;
	}

	private int[] prochainPixel(int[] courant, int direction) {
		int[] tab = new int[2];
		switch (direction) {

		case 0:
			tab[0] = courant[0] - 1;
			tab[1] = courant[1];
			return tab;
		case 1:
			tab[0] = courant[0] - 1;
			tab[1] = courant[1] + 1;
			return tab;
		case 2:
			tab[0] = courant[0];
			tab[1] = courant[1] + 1;
			return tab;
		case 3:
			tab[0] = courant[0] + 1;
			tab[1] = courant[1] + 1;
			return tab;
		case 4:
			tab[0] = courant[0] + 1;
			tab[1] = courant[1];
			return tab;
		case 5:
			tab[0] = courant[0] + 1;
			tab[1] = courant[1] - 1;
			return tab;
		case 6:
			tab[0] = courant[0];
			tab[1] = courant[1] - 1;
			return tab;
		case 7:
			tab[0] = courant[0] - 1;
			tab[1] = courant[1] - 1;
			return tab;

		default:
			System.out.println("La direction doit être un entier entre 0 et 7");
		}
		return null;
	}

	private boolean allumer(int[] nouveau, int[][] matrix) {
		int i = nouveau[0];
		int j = nouveau[1];
		if (matrix[i][j] == 1)
			return true;
		return false;
	}

}