/**
	DualPerceptronRBF.java: implementation of dual perceptron algorithm (with RBF kernel) 
**/

import java.util.*;

public class DualPerceptronRBF
{
	public static double[][] data;
	public static double[] label;
	public static double[] alpha;				// mistake counter vector 
	public static final int NUM_FOLD = 10;
	public static final int MAX_ITER = 1000;
	public static final double eta = 0.001;
	public static double sigma;
	public static int N, K;					// #data, #features 

	public static void
	init(String[] args) {
		String fileDir = args[0];
		sigma = Double.parseDouble(args[1]);
		N = FileParser.readNumOfInstances(fileDir, ",");
		K = FileParser.readNumOfFeatures(fileDir, ",");
		System.out.println("\nUsing sigma " + sigma);

		data = new double[N][K+1];
		label = new double[N];
		alpha = new double[N];

		FileParser.readData(fileDir, ",", data, label);
	}

	public static double
	RBFKernel(double[] arr1, double[] arr2, double sigma) {
		double res = 0;
		for (int k = 0; k < arr1.length-1; k++)
			res += (arr1[k]-arr2[k]) * (arr1[k]-arr2[k]);
		res = Math.exp(-res/2/sigma/sigma);

		return res;
	}

	public static double
	predict(double[] alpha, double[] label, double[][] data, double[] input, double sigma, int fold) {
		double res = 0;
		for (int i = 0; i < N; i++) {
			if (i%NUM_FOLD == fold) continue;
			if (alpha[i] != 0) 
				res += alpha[i] * label[i] * RBFKernel(data[i], input, sigma);
		}

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
				double yi = predict(alpha, label, data, data[i], sigma, fold);
				if (yi * label[i] <= 0) {
					alpha[i] += 1;
					flag = false;
				}
			}
			if (flag) break;
			if (count == MAX_ITER) break;
			flag = true;
		}
		System.out.println("Convergence Reached after " + count + " Iteration(s)!");

		return;
	}

	public static double
	test(int fold) {
		int cor = 0, tot = 0;
		for (int i = 0; i < N; i++) {
			if (i%NUM_FOLD != fold) continue;
			double yi = predict(alpha, label, data, data[i], sigma, fold);
			if (yi * label[i] > 0) cor += 1;
			tot += 1;
		}
		double acc = (double)cor/tot;
		System.out.println(cor + " out of " + tot + " correct! Accuracy = " + acc);

		return acc;
	}

	public static void
	main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java DualPerceptronRBF ../../data/perceptronData.csv <sigma>");
			System.exit(0);
		}

		double averAcc = 0;
		init(args);
		for (int fold = 0; fold < NUM_FOLD; fold++) {
			System.out.println("\n------ Fold " + fold + " ------");
			train(fold);
			averAcc += test(fold);
		}
		System.out.println("Average Accuracy = " + averAcc/NUM_FOLD);

		return;
	}
}
