/**
	GaussianMixtureModel.java: Implementation of K-means algorithm
	Problem: Data points tend to fall into one cluster 
**/

import java.util.*;
import java.io.*;

public class GaussianMixtureModel
{
	public static double[][] data;					// data (N * M) 
	public static int[] label;					// actual labels (N * 1)
	public static int[] predict;					// predicted labels (N * 1) 
	public static double[][] mu;					// center for Gaussians (K * M) 
	public static double[] weight;					// weight for each cluster (K * 1) 

	public static int M;						// feature dimension
	public static int N;						// num of data instances
	public static int K;						// num of clusters 
	public static final int MAX_ITER = 5;

	public static void
	init(String[] args) {
		Random rand = new Random();
		String fileDir = args[0];
		K = Integer.parseInt(args[1]);
		N = FileParser.readNumOfInstances(fileDir, ",");
		M = FileParser.readNumOfFeatures(fileDir, ",");
		System.out.println("\nNumber of Clusters = " + K);

		data = new double[N][M];
		label = new int[N];
		predict = new int[N];
		mu = new double[K][M];
		weight = new double[K];

		FileParser.readData(fileDir, ",", data, label, M);

		for (int n = 0; n < N; n++) 
			predict[n] = rand.nextInt(K);
//			predict[n] = label[n];				// alright 
		for (int k = 0; k < K; k++) 
			weight[k] = 1/(double)K;

		double[][] gamma = new double[N][K];
		for (int n = 0; n < N; n++) 
			for (int k = 0; k < K; k++)
				gamma[n][k] = rand.nextDouble();	// 
		update(data, mu, weight, gamma);

		return;
	}

	// calculate l2 distance 
	public static double
	calcMixture(double[] arr1, double[] arr2) {
		double ip = 0;						// inner product
		for (int i = 0; i < arr1.length; i++) {
			ip += arr1[i] * arr2[i];
		}
		double res = Math.pow(2*Math.PI, -K/2.0) * Math.exp(-0.5*ip);
		return res;
	}

	// assign the nearest cluster to data 
	public static int
	argMax(double[] arr) {
		int res = 0; double max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				res = i;
			}
		}
		return res;
	}

	public static void
	estimate(double[][] data, double[][] mu, double[] weight, double[][] gamma) {
		for (int n = 0; n < data.length; n++) {
			double sumOfGamma = 0;
			for (int k = 0; k < K; k++) {
				gamma[n][k] = weight[k] * calcMixture(data[n], mu[k]);
				sumOfGamma += gamma[n][k];
			}

			for (int k = 0; k < K; k++) {
				gamma[n][k] /= sumOfGamma;
//				System.out.printf("%f ", gamma[n][k]);
			}
//			Scanner sc = new Scanner(System.in);
//			int gu = sc.nextInt();
		}

		return;
	}

	public static void
	update(double[][] data, double[][] mu, double[] weight, double[][] gamma) {
		// update centers 
		double[] muNorm = new double[M];
		for (int k = 0; k < K; k++) 
			for (int m = 0; m < M; m++)
				mu[k][m] = 0; 
		for (int n = 0; n < data.length; n++) {
			for (int m = 0; m < M; m++) {
				for (int k = 0; k < K; k++) {
					mu[k][m] += gamma[n][k] * data[n][m];
					muNorm[m] += gamma[n][k] * data[n][m];
				}
			}
		}
		for (int k = 0; k < K; k++) 						// normalize 
			for (int m = 0; m < M; m++) 
				mu[k][m] /= muNorm[m];

		// update weight 
		for (int k = 0; k < K; k++) 
			weight[k] = 0;
		for (int k = 0; k < K; k++) 
			for (int n = 0; n < N; n++) 
				weight[k] += gamma[n][k];
		double sumWeight = 0;
		for (int k = 0; k < K; k++) 
			sumWeight += weight[k];
		for (int k = 0; k < K; k++)
			weight[k] /= sumWeight;

		return;
	}


	// training - assigning clusters to data points 
	public static void 
	train() {
		for (int iter = 0; iter < MAX_ITER; iter++) {
			if (iter%100 == 0) System.out.println("Iter = " + iter);
			double[][] gamma = new double[N][K];
			for (int n = 0; n < N; n++) {					// E step 
				estimate(data, mu, weight, gamma);
				update(data, mu, weight, gamma);
			}
			for (int n = 0; n < N; n++) {
				predict[n] = argMax(gamma[n]);
			}

			double e = Evaluation.sumOfSquaredErrors(data, mu, predict);
			System.out.println("err = " + e);

			for (int k = 0; k < K; k++) System.out.printf("%f\t", weight[k]);
			System.out.printf("\n");

			Scanner sc = new Scanner(System.in);
//			int gu = sc.nextInt();
		}

		return;
	}

	public static void 
	main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java GaussianMixtureModel ../../data/spambase.csv <cluster_size>");
			System.exit(0);
		}

		init(args);

		train();
	}
}
