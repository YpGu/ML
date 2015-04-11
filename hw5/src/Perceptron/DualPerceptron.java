/**
	DualPerceptron.java: implementation of dual perceptron algorithm
**/

import java.util.*;

public class DualPerceptron
{
	public static double[][] data;
	public static double[] label;
	public static double[] alpha;				// mistake counter vector 
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
		alpha = new double[N];

		FileParser.readData(fileDir, ",", data, label);
	}

	public static double
	innerProduct(double[] alpha, double[] label, double[][] data, double[] input, int fold) {
		double res = 0;
		for (int i = 0; i < N; i++) {
			if (i%NUM_FOLD == fold) continue;
			if (alpha[i] != 0) 
				for (int k = 0; k < K; k++) 
					res += alpha[i] * label[i] * data[i][k] * input[k];
		}

		return res;
	}

	public static void
	train(int fold) {
		boolean flag = true;
		int count = 0;
//		for (int iter = 0; iter < 1000; iter++) {
		while (true) {
			count += 1;
			for (int i = 0; i < N; i++) {
				if (i%NUM_FOLD == fold) continue;
				double yi = innerProduct(alpha, label, data, data[i], fold);
				if (yi * label[i] <= 0) {
					alpha[i] += 1;
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
			double yi = innerProduct(alpha, label, data, data[i], fold);
			if (yi * label[i] > 0) cor += 1;
			tot += 1;
		}
		System.out.println(cor + " out of " + tot + " correct! Accuracy = " + (double)cor/tot);

		return;
	}

	public static void
	main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java DualPerceptron ../../data/perceptronData.csv");
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
