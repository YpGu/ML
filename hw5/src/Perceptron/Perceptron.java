/**
	Perceptron.java: implementation of perceptron algorithm
**/

import java.util.*;

public class Perceptron
{
	public static double[][] data;
	public static double[] label;
	public static double[] w;
	public static final int NUM_FOLD = 10;
	public static final double eta = 0.001;
	public static int N, K;					// #data, #features 

	public static void
	init(String[] args) {
		String fileDir = args[0];
		N = FileParser.readNumOfInstances(fileDir, ",");
		K = FileParser.readNumOfFeatures(fileDir, ",");

		data = new double[N][K+1];
		label = new double[N];
		w = new double[K+1];
		FileParser.readData(fileDir, ",", data, label);
	}

	public static double
	innerProduct(double[] arr1, double[] arr2) {
		int K = arr1.length;
		if (K != arr2.length) {
			System.out.println("Inner Product: dimension does not agree!");
			return -1;
		}

		double res = 0;
		for (int k = 0; k < K; k++) 
			res += arr1[k] * arr2[k];
		return res;
	}

	public static void
	train(int fold) {
		boolean flag = true;
		int count = 0;
		while (true) {
			count += 1;
			for (int i = 0; i < N; i++) {
				if (i%NUM_FOLD == fold) continue;
				double yi = innerProduct(w, data[i]);
				if (yi * label[i] <= 0) {
					for (int k = 0; k < K+1; k++) 
						w[k] += eta * label[i] * data[i][k];
					flag = false;
				}
			}
			if (flag) break;
			flag = true;
		}
		System.out.println("Convergence Reached after " + count + " Iteration(s)!");

		return;
	}

	public static void
	test(int fold) {
		int cor = 0, tot = 0;
		for (int i = 0; i < N; i++) {
			if (i%NUM_FOLD != fold) continue;
			double yi = innerProduct(w, data[i]);
			if (yi * label[i] > 0) cor += 1;
			tot += 1;
		}
		System.out.println(cor + " out of " + tot + " correct! Accuracy = " + (double)cor/tot);

		return;
	}

	public static void
	main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java Perceptron ../../data/perceptronData.csv");
			System.exit(0);
		}

		init(args);
		for (int fold = 0; fold < NUM_FOLD; fold++) {
			System.out.println("\n------ Fold " + fold + " ------");
			train(fold);
			test(fold);
		}

		return;
	}
}
