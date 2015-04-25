/**
	Normalizer.java: normalize raw data
**/

import java.util.*;

public class Normalizer
{
	public static void
	norm(double[][] data) {
		int n1 = data.length, n2 = data[0].length;
		double[] minCol = new double[n2];
		double[] maxCol = new double[n2];
		for (int i = 0; i < n2; i++) {
			minCol[i] = Double.MAX_VALUE;
			maxCol[i] = -Double.MAX_VALUE;
		}

		// aver 
		double[] aver = new double[n2];
		double[] std = new double[n2];
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (data[i][j] < minCol[j]) 
					minCol[j] = data[i][j];
				if (data[i][j] > maxCol[j]) 
					maxCol[j] = data[i][j];
				aver[j] += data[i][j];
			}
		}
		for (int j = 0; j < n2; j++) 
			aver[j] /= (double)n1;

		// std 
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				std[j] += (data[i][j] - aver[j]) * (data[i][j] - aver[j]);
			}
		}
		for (int j = 0; j < n2; j++) 
			std[j] = Math.sqrt(std[j] / (n1-1));

		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				// minmax normalization 
//				double diff = maxCol[j] - minCol[j];
//				if (diff != 0) 
//					data[i][j] = (data[i][j] - minCol[j]) / diff;
				// z-score normalization
				if (std[j] != 0)
					data[i][j] = (data[i][j] - aver[j]) / std[j];
			}
		}

		return;
	}
}
