/**
	Evaluation.java: various evaluation measures.
**/

import java.util.*;

public class Evaluation
{
	// sum of squared errors 
	public static double
	sumOfSquaredErrors(double[][] data, double[][] centers, int[] predict) {
		double res = 0;
		for (int n = 0; n < data.length; n++) {
			int k = predict[n];
			for (int m = 0; m < centers[k].length; m++) {
				res += (centers[k][m] - data[n][m]) * (centers[k][m] - data[n][m]);
			}
		}

		return res;
	}

	// NMI (higher the better) 
	public static double
	normalizedMutualInformation(
		double[][] data, int[] predict, int[] label,
		int K1,						// actual number of classes 
		int K2						// K in KMeans 
	) {
		double[] py = new double[K1];			// list of P(Y)'s 
		double[] pc = new double[K2];			// list of P(C)'s 
		double[][] pyc = new double[K1][K2];
		double hy = 0, hc = 0, hyc = 0;
		for (int n = 0; n < data.length; n++) {
			int k1 = label[n];
			py[k1] += 1.0;
			int k2 = predict[n];
			pc[k2] += 1.0;
			pyc[k1][k2] += 1.0;
		}
		for (int k = 0; k < K1; k++) {
			py[k] /= (double)(data.length);
			hy -= py[k] * Math.log(py[k] + Double.MIN_VALUE);
		}
		for (int k = 0; k < K2; k++) {
			if (pc[k] != 0) 
				for (int k1 = 0; k1 < K1; k1++)
					pyc[k1][k] /= pc[k];

			pc[k] /= (double)(data.length);
			hc -= pc[k] * Math.log(pc[k] + Double.MIN_VALUE);
		}
		for (int k1 = 0; k1 < K1; k1++) {
			for (int k2 = 0; k2 < K2; k2++) {
				hyc -= pc[k2] * pyc[k1][k2] * Math.log(pyc[k1][k2] + Double.MIN_VALUE);
			}
		}

		double res = 2 * (hy - hyc) / (hy + hc);
		return res;
	}


}
