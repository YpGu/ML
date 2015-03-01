/**
	Solve the Ridge Regression problem using closed-form solution
**/

import java.util.*;
import Jama.*;

public class Solver
{
	public static Matrix
	sol(
		double[][] arrZ,
		double[][] arrT,
		int N,
		int M,
		double lambda
	) {
		Matrix m = new Matrix(1,1);

		Matrix Z = new Matrix(arrZ);
		Matrix ZT = Z.transpose();
		Matrix I = m.identity(M,M);
		Matrix T = new Matrix(arrT);

		Matrix w = ( ( ZT.times(Z).plus(I.times(lambda)) ).inverse() ).times(ZT).times(T);

		return w;
	}
}
