/**
	KMeans.java: Implementation of K-means algorithm
	Problem: Data points tend to fall into one cluster 
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
	public static int K1;						// actual number of clusters 
	public static final int MAX_ITER = 500;

	public static void
	initCenters(double[][] ctr) {
		Random rand = new Random();
		for (int k = 0; k < K; k++) {
			int r = rand.nextInt(N);
			for (int m = 0; m < M; m++) 
				ctr[k][m] = data[r][m];
		}

/*
		System.out.println("centers:");
		for (int k = 0; k < K; k++) {
			System.out.printf("%d ", k);
			for (int m = 0; m < M; m++) 
				System.out.printf("%f\t", ctr[k][m]);
			System.out.printf("\n");
		}
*/
		return;
	}

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
		center = new double[K][M];

		FileParser.readData(fileDir, ",", data, label, M);
		Normalizer.norm(data);

		K1 = 0;
		for (int n = 0; n < N; n++) {
			if (label[n] > K1) 
				K1 = label[n];
		}
		K1++;
		System.out.println("K1 = " + K1);

		initCenters(center);

		return;
	}

	// calculate l2 distance 
	public static double
	calcDistance(double[] arr1, double[] arr2) {
		double res = 0;
		for (int i = 0; i < arr1.length; i++) {
			res += (arr1[i] - arr2[i]) * (arr1[i] - arr2[i]);
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
//			System.out.printf("k = %d, dist = %f\n", k, dist);
		}
//		System.out.printf("%d selected\n", res);

		Scanner sc = new Scanner(System.in);
//		int gu = sc.nextInt();

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
		// print
		if (false) {
			for (int k = 0; k < K; k++) {
				System.out.printf("-- cluster %d %d ", k, numElements[k]);
				for (int m = 0; m < M; m++) 
					System.out.printf("%f\t", center[k][m]);
				System.out.printf("\n");
			}
		}
		return;
	}

	// training - assigning clusters to data points 
	public static void 
	train() {
		double oldObj = -1;
		for (int iter = 0; iter < MAX_ITER; iter++) {
			if (iter%100 == 0) System.out.println("Iter = " + iter);
			for (int n = 0; n < N; n++) {
				predict[n] = argMax(data[n], center);
//				System.out.printf("%d ", predict[n]);
			}
//			System.out.printf("\n");
			updateCenters(data, predict, center);

			double e = Evaluation.sumOfSquaredErrors(data, center, predict);
			double f = Evaluation.normalizedMutualInformation(data, predict, label, K1, K);
			System.out.println("err = " + e + " nmi = " + f);
			if (e == oldObj && iter > 2) break;
			oldObj = e;

			Scanner sc = new Scanner(System.in);
//			int gu = sc.nextInt();
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
