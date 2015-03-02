/**
	Solve the Ridge Regression problem using closed-form solution
**/

import java.util.*;
import Jama.*;

public class Solver
{
	public static void 
	printMat(
		Matrix w
	) {
		for (int i = 0; i < w.getRowDimension(); i++) {
			for (int j = 0; j < w.getColumnDimension(); j++) {
				System.out.println(w.get(i,j));
			}
		}
	}


	public static void
	sol_gd(
		double[][] data,
		double[][] labels,
		double lambda,
		double[] w,
		double lr
	) {
		int M = data[0].length;

/*		// assign value to w[M]
		double ave = 0;
		for (int i = 0; i < labels.length; i++)
			ave += labels[i][0];
		ave /= labels.length;
		w[M] = ave;
*/
		// train values for w[0] ~ w[M-1] 
		double oldObj = 0;
		for (int iter = 0; iter < 1000; iter++) {
			double[] grad = new double[M+1];
			for (int i = 0; i < data.length; i++) {
				double ei = labels[i][0];
				for (int j = 0; j < M; j++) {
					ei -= w[j] * data[i][j];
				}
				ei -= w[M];
				for (int j = 0; j < M; j++) {
					grad[j] -= 2 * data[i][j] * ei;
				}
				grad[M] -= 2 * ei;
			}

			for (int i = 0; i < M; i++) {
				grad[i] += 2 * lambda * w[i];
			}

			for (int i = 0; i < M+1; i++) {
				w[i] -= lr * grad[i];
			}

			double newObj = Evaluation.calcObj(data, labels, w);
//			System.out.println("Iter = " + iter + " Obj = " + newObj);
			if (Math.abs(newObj-oldObj) < 0.00001 && iter > 1) {
				break;
			}
			oldObj = newObj;
		}

		return;
	}


	/// solve using closed-form solultion
	public static void
	sol(
		double[][] arrZ,			// data matrix (N*m)
		double[][] arrT,			// labels (N*1)
		double lambda,
		double[] arrw				// ref
	) {
		Matrix Z = new Matrix(arrZ);					// N*m
		Matrix ZT = Z.transpose();					// m*N
		Matrix I = new Matrix(arrZ[0].length, arrZ[0].length);		// m*m
		for (int i = 0; i < arrZ[0].length; i++) {
			for (int j = 0; j < arrZ[0].length; j++) {
				if (i != j) {
					I.set(i,j,0);
				}
				else {
					if (lambda != 0)
						I.set(i,i,lambda);
					else
						I.set(i,i,Math.pow(10,-3));
				}
			}
		}
		Matrix T = new Matrix(arrT);					// m*1
		Matrix w = ( ( (ZT.times(Z)).plus(I) ).inverse() ).times(ZT).times(T);

		// update weights (result) 
		double[][] arrw2 = new double[w.getRowDimension()][w.getColumnDimension()];
		arrw2 = w.getArrayCopy();
		for (int i = 0; i < arrw2.length; i++) {
			arrw[i] = arrw2[i][0];
		}
		double ave = 0;
		for (int i = 0; i < arrT.length; i++)
			ave += arrT[i][0];
		ave /= arrT.length;
		arrw[arrw2.length] = ave;

		return;
	}
}
