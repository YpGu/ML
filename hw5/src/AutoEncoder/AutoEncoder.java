/**
	AutoEncoder.java: implementation of 2-layer autoencoder algorithm
**/

import java.util.*;

public class AutoEncoder
{
	public static final int INS = 8;				// Number of Instances 
	public static final int M = 8, N = 3, P = 8;
	public static final int MAX_ITER = 100000;
	public static final double lr = 0.1;				// learning rate 
	public static double[][] w1, w2;				// weight matrix between layers 
	public static double[][] x, z, y;				// units for each layer 
	public static double[][] t;					// labels 

	public static void
	init() {
		Random rand = new Random();
		double initRange = 0.5;
		x = new double[INS][M+1]; z = new double[INS][N+1]; y = new double[INS][P]; t = new double[INS][P];
		w1 = new double[N][M+1]; w2 = new double[P][N+1];

		for (int i = 0; i < INS; i++) {
			for (int m = 0; m < M; m++) {
				if (m == i) x[i][m] = 1.0;
				else x[i][m] = 0.0;
			}
			x[i][M] = 1.0;
			z[i][N] = 1.0;
		}
		for (int i = 0; i < INS; i++) {
			for (int p = 0; p < P; p++) {
				if (p == i) t[i][p] = 1.0;
				else t[i][p] = 0.0;
			}
		}

		for (int n = 0; n < N; n++)
			for (int m = 0; m < M+1; m++) 
				w1[n][m] = (rand.nextDouble()) * initRange * 2.0;
//				w1[n][m] = (rand.nextDouble()-0.5) * initRange * 2.0;
		for (int p = 0; p < P; p++) 
			for (int n = 0; n < N+1; n++) 
				w2[p][n] = (rand.nextDouble()) * initRange * 2.0;
//				w2[p][n] = (rand.nextDouble()-0.5) * initRange * 2.0;
		update();
		return;
	}

	public static double
	calObj() {
		double res = 0;
		for (int i = 0; i < INS; i++) {
			for (int p = 0; p < P; p++) {
				res += (y[i][p] - t[i][p])*(y[i][p] - t[i][p]);
			}
		}
		return res;
	}

	public static double
	logis (double x) {
		if (x > 100) return 1;
		else return Math.exp(x)/(1+Math.exp(x));
	}

	public static void
	update() {
		for (int i = 0; i < INS; i++) {
			for (int n = 0; n < N; n++) {
				double ins = 0;
				for (int m = 0; m < M+1; m++)
					ins += w1[n][m] * x[i][m];
				z[i][n] = logis(ins);
			}
			for (int p = 0; p < P; p++) {
				double ins = 0;
				for (int n = 0; n < N+1; n++) 
					ins += w2[p][n] * z[i][n];
				y[i][p] = logis(ins);
			}
		}
		return;
	}

	public static void
	print(double[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				System.out.printf("%f\t", arr[i][j]);
			}
			System.out.printf("\n");
		}
		return;
	}

	public static void
	train(int iter) {
		for (int i = 0; i < INS; i++) {
			double[][] grad1 = new double[N][M+1], grad2 = new double[P][N+1];
			double[] err = new double[P];

			for (int p = 0; p < P; p++) {
				for (int n = 0; n < N+1; n++) {
					err[p] = y[i][p] * (1-y[i][p]) * (y[i][p]-t[i][p]);
					double derv = err[p] * z[i][n];
					grad2[p][n] += derv;
				}
			}
			for (int n = 0; n < N; n++) {
				for (int m = 0; m < M+1; m++) {
					double derv = 0;
					for (int p = 0; p < P; p++) 
						derv += err[p] * w2[p][n];
					derv = derv * z[i][n] * (1-z[i][n]) * x[i][m];
					grad1[n][m] += derv;
				}
			}

			for (int p = 0; p < P; p++)
				for (int n = 0; n < N+1; n++)
					w2[p][n] -= lr * grad2[p][n];
			for (int n = 0; n < N; n++)
				for (int m = 0; m < M+1; m++) 
					w1[n][m] -= lr * grad1[n][m];
		}

		update();
		if (iter%100 == 0) System.out.println("Iter = " + iter + " Obj = " + calObj());
		if (iter > MAX_ITER - 3) {
			System.out.println("------ Iter " + iter + " ------");
			print(z);
			System.out.println("----------");
//			print(w2);
//			System.out.println("----------");
			print(y);
		}
		return;
	}

	public static void
	main(String[] args) {
		init();
		for (int iter = 0; iter < MAX_ITER; iter++) {
			train(iter);
		}
	}
}

