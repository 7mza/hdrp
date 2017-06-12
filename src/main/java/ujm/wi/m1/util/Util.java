package ujm.wi.m1.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Util {
	
	public double[] normalize(int[] array) {
		double[] norm = new double[array.length];
		double max = this.getMax(array);
		for (int i = 0; i < array.length; i++)
			norm[i] = (double) array[i] / max;
		return norm;
	}

	private double getMax(int[] array) {
		int max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return (double) max;
	}

	public int[] getHistogram(String str) {
		int[] hist = new int[8];
		int[] array = this.stringtoArray(str);
		int cpt = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < array.length; j++) {
				if (array[j] == i)
					cpt++;
			}
			hist[i] = cpt;
			cpt = 0;
		}
		return hist;
	}

	private int sumString(String str) {
		int sum = 0;
		for (int i = 0; i < str.length(); i++)
			sum = sum + Character.getNumericValue(str.charAt(i));
		return sum;
	}

	public int[] sliceFreeman(String str, int nbr) {
		List<Integer> sums = new ArrayList<>();
		int step = 0, index = 0, len = str.length() / nbr;
		while (index < nbr) {
			String sub = str.substring(step, step + len);
			sums.add(sumString(sub));
			index++;
			step = step + len;
		}
		if (str.length() % nbr > 0) {
			String sub = str.substring(step, str.length());
			sums.add(sumString(sub));
		}
		if (sums.size() > nbr) {
			int last = sums.get(index);
			int tmp = sums.get(index - 1) + last;
			sums.remove(index);
			sums.remove(index - 1);
			sums.add(tmp);
		}
		int[] tmp = new int[sums.size()];
		index = 0;
		for (Integer i : sums) {
			tmp[index] = i;
			index++;
		}
		return tmp;
	}

	private int[] stringtoArray(String str) {
		int[] array = new int[str.length()];
		for (int i = 0; i < str.length(); i++)
			array[i] = Character.getNumericValue(str.charAt(i));
		return array;
	}

	public String reduceMatrix(String str, int rate, boolean summation) {
		int[] array = stringtoArray(str);
		int[][] matrix = arrayToMatrix(array, 200);
		String reduced = "";
		int row = 0, col = 0, cpt = 0;
		while (row <= matrix.length - rate) {
			while (col <= matrix[0].length - rate) {
				for (int i = 0; i < rate; i++)
					for (int j = 0; j < rate; j++)
						cpt = cpt + matrix[row + i][col + j];
				if (!summation)
					if (cpt < rate * rate / 2)
						cpt = 0;
					else
						cpt = 1;
				reduced = reduced + cpt + ",";
				cpt = 0;
				col += rate;
			}
			cpt = 0;
			col = 0;
			row += rate;
		}
		return reduced.substring(0, reduced.length() - 1);
	}

	public String arraytoString(int[] array) {
		String tmp = "";
		for (int i = 0; i < array.length; i++)
			tmp = tmp + array[i];
		return tmp;
	}

	public int[][] arrayToMatrix(int[] array, int length) {
		int[][] matrix = new int[length][length];
		int i = 0, j = 0;
		for (int k = 0; k < array.length; k++) {
			matrix[i][j] = array[k];
			if (j < length - 1)
				j++;
			else {
				i++;
				j = 0;
			}
		}
		return matrix;
	}

	public int findPopular(int[] a) {
		if (a == null || a.length == 0)
			return 0;
		Arrays.sort(a);
		int previous = a[0];
		int popular = a[0];
		int count = 1;
		int maxCount = 1;
		for (int i = 1; i < a.length; i++) {
			if (a[i] == previous)
				count++;
			else {
				if (count > maxCount) {
					popular = a[i - 1];
					maxCount = count;
				}
				previous = a[i];
				count = 1;
			}
		}
		return count > maxCount ? a[a.length - 1] : popular;
	}

}