/**
	Implementation of Ridge Regression
**/

import java.util.*;
import java.io.*;

public class PolynomialRegression
{
	public static int NUM_OF_INSTANCES_TRAIN;
	public static int NUM_OF_INSTANCES_TEST;
	public static int NUM_OF_FEATURES;
	public static String trainDir, testDir;
	public static int POWER;
	public static final boolean DISPLAY = false;

	public static ArrayList<Double> trainRMSEs;
	public static ArrayList<Double> testRMSEs;

	public static double[][] allData;
	public static double[][] allLabels;
	public static double[][] trainData;
	public static double[][] trainLabels;			// m*1 vector
	public static double[][] testData;
	public static double[][] testLabels;

	public static double[] weights;


	public static void init(String[] args)
	{
		trainDir = args[0];
		testDir = args[1];
		POWER = Integer.parseInt(args[2]);
		NUM_OF_INSTANCES_TRAIN = FileParser.readNumOfInstances(trainDir, ",");
		NUM_OF_INSTANCES_TEST = FileParser.readNumOfInstances(testDir, ",");
		NUM_OF_FEATURES = FileParser.readNumOfFeatures(trainDir, ",");

		trainData = new double[NUM_OF_INSTANCES_TRAIN][NUM_OF_FEATURES*POWER+1];
		trainLabels = new double[NUM_OF_INSTANCES_TRAIN][1];
		testData = new double[NUM_OF_INSTANCES_TEST][NUM_OF_FEATURES*POWER+1];
		testLabels = new double[NUM_OF_INSTANCES_TEST][1];
		weights = new double[NUM_OF_FEATURES*POWER+1];

		trainRMSEs = new ArrayList<Double>();
		testRMSEs = new ArrayList<Double>();

		return;
	}

	// run
	public static void
	run() {
		FileParser.center(trainData, testData);

		Solver.sol(trainData, trainLabels, weights);

	//	for (int i = 0; i < weights.length; i++)
	//		System.out.println(weights[i]);

		Evaluation.evaluate(trainData, trainLabels, testData, testLabels, weights, trainRMSEs, testRMSEs, DISPLAY);

		double averTrRMSE = 0, averTeRMSE = 0;
		for (int i = 0; i < trainRMSEs.size(); i++) {
			averTrRMSE += trainRMSEs.get(i);
			averTeRMSE += testRMSEs.get(i);
		}
		averTrRMSE /= trainRMSEs.size(); averTeRMSE /= trainRMSEs.size();
		System.out.println("Training RMSE = " + averTrRMSE);
		System.out.println("Testing RMSE = " + averTeRMSE);

	}

	// main method 
	public static void 
	main(
		String[] args
	) {
		if (args.length != 3)
		{
			System.out.println("Usage: java PolynomialRegression <trainDir> <testDir> <power>");
			System.out.println("Example: java PolynomialRegression ../../data/sinData_Train.csv ../../data/sinData_Validation.csv 15");
			System.exit(0);
		}

		init(args);

		FileParser.readData(trainDir, ",", trainData, trainLabels, POWER);
		FileParser.readData(testDir, ",", testData, testLabels, POWER);

		run();
	}
}
