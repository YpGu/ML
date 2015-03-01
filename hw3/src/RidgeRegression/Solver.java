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
	sol(
		double[][] arrZ,			// data matrix (N*m)
		double[][] arrT,			// labels (N*1)
		double lambda,
		double[] arrw				// ref
	) {
		Matrix Z = new Matrix(arrZ);					// N*m
		Matrix ZT = Z.transpose();					// m*N
		Matrix I = new Matrix(arrZ[0].length, arrZ[0].length);		// m*m
		for (int i = 0; i < arrZ[0].length; i++)
			I.set(i,i,1);
		Matrix T = new Matrix(arrT);					// m*1
printMat(T);
		Matrix w = ( ( ZT.times(Z).plus(I.times(lambda)) ).inverse() ).times(ZT).times(T);

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
