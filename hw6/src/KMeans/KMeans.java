/**
	KMeans.java: Implementation of K-means algorithm.
**/

import java.util.*;
import java.io.*;

public class KMeans
{
	public static double[][] data;					// data (N * M) 
	public static int[] label;					// actual labels (N * 1)
	public static int[] predict;					// predicted labels (N * 1) 
	public static double[][] center;				// center for clusters (K * M) 
	public static int M;						// feature dimension
	public static int N;						// num of data instances
	public static int K;						// num of clusters 
	public static final int MAX_ITER = 100000;
	public static final int MAX_FOLD = 10;
	public static final boolean WRITE = false;

	public static final double lr = 0.000000005;			// spambase, breast cancer
//	public static final double lr = 0.000000001;			// diabete
//	public static final double lr = 0.002;				// test
	public static final double epsilon = 0.001;			// spambase, brease cancer
//	public static final double epsilon = 0.1;			// diabete
	public static double lambda;


	public static void
	init(String[] args) {
		String fileDir = args[0];
		K = Integer.parseInt(args[1]);
		N = FileParser.readNumOfInstances(fileDir, ",");
		M = FileParser.readNumOfFeatures(fileDir, ",");
		System.out.println("\nNumber of Clusters = " + K);

		data = new double[N][M];
		label = new int[N];
		predict = new int[N];
		center = new double[K][M];

		FileParser.readData(fileDir, ",", data, label, M);

		Random rand = new Random(0);
		for (int n = 0; n < N; n++) 
			predict[n] = rand.nextInt(K);
		updateCenters(data, predict, center);

		return;
	}

	// calculate l2 distance 
	public static double
	calcDistance(double[] arr1, double[] arr2) {
		double res = 0;
		for (int i = 0; i < arr1.length; i++) {
			res += arr1[i] * arr2[i];
		}
		return res;
	}

	// assign the nearest cluster to data 
	public static int
	argMax(double[] data, double[][] center) {
		int res = -1;
		double minDistance = Double.MAX_VALUE;
		for (int k = 0; k < K; k++) {
			double dist = calcDistance(data, center[k]);
			if (dist < minDistance) {
				minDistance = dist;
				res = k;
			}
		}

		return res;
	}

	// update centers 
	public static void
	updateCenters(double[][] data, int[] predict, double[][] center) {
		for (int k = 0; k < K; k++) 
			for (int m = 0; m < M; m++) 
				center[k][m] = 0;
		int[] numElements = new int[K];
		// sum 
		for (int n = 0; n < N; n++) {
			int k = predict[n];
			for (int m = 0; m < M; m++)
				center[k][m] += data[n][m];
			numElements[k] += 1;
		}
		// aver
		for (int k = 0; k < K; k++) {
			if (numElements[k] == 0) numElements[k] = 1;
			for (int m = 0; m < M; m++) {
				center[k][m] /= (double)numElements[k];
			}
		}
		return;
	}

	// training - assigning clusters to data points 
	public static void 
	train() {
		for (int iter = 0; iter < MAX_ITER; iter++) {
			if (iter%100 == 0) System.out.println("Iter = " + iter);
			for (int n = 0; n < N; n++) 
				predict[n] = argMax(data[n], center);
			updateCenters(data, predict, center);
		}

		return;
	}

	public static void 
	main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java KMeans ../../data/spambase.csv <cluster_size>");
			System.exit(0);
		}

		init(args);

		train();
	}
}
