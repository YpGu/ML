/**
	Implementation of Ridge Regression
**/

import java.util.*;
import java.io.*;

public class RidgeRegression
{
	public static double lambda;				// regularization coefficient 
	public static int NUM_OF_INSTANCES;
	public static int NUM_OF_FEATURES;
	public static final int NUM_OF_FOLDS = 10;
	public static String fileDir;
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
		fileDir = args[0];
		lambda = Double.parseDouble(args[1])/5;		// please adjust corresponding input 
		POWER = Integer.parseInt(args[2]);
		NUM_OF_INSTANCES = FileParser.readNumOfInstances(fileDir, ",");
		NUM_OF_FEATURES = FileParser.readNumOfFeatures(fileDir, ",");
		System.out.println("lambda = " + lambda);

		allData = new double[NUM_OF_INSTANCES][NUM_OF_FEATURES*POWER];
		allLabels = new double[NUM_OF_INSTANCES][1];
		weights = new double[NUM_OF_FEATURES*POWER+1];

		trainRMSEs = new ArrayList<Double>();
		testRMSEs = new ArrayList<Double>();

		return;
	}

	// run
	public static void
	run() {
		for (int fold = 0; fold < NUM_OF_FOLDS; fold++) {

			if (DISPLAY)
				System.out.println("-------- Fold " + fold + " --------");

/*			testData = new double[(int)(NUM_OF_INSTANCES-fold-1)/10+1][NUM_OF_FEATURES*POWER]; 
			testLabels = new double[(int)(NUM_OF_INSTANCES-fold-1)/10+1][1];
			trainData = new double[NUM_OF_INSTANCES-testData.length][NUM_OF_FEATURES*POWER];
			trainLabels = new double[NUM_OF_INSTANCES-testData.length][1];
*/
			testData = new double[NUM_OF_INSTANCES/10][NUM_OF_FEATURES*POWER];
			testLabels = new double[NUM_OF_INSTANCES/10][1];
			trainData = new double[NUM_OF_INSTANCES-testData.length][NUM_OF_FEATURES*POWER];
			trainLabels = new double[NUM_OF_INSTANCES-testData.length][1];

			FileParser.crossValidation(allData, allLabels, trainData, trainLabels, testData, testLabels, fold);

			Solver.sol(trainData, trainLabels, lambda, weights);

	//		for (int i = 0; i < weights.length; i++)
	//			System.out.println(weights[i]);

			Evaluation.evaluate(trainData, trainLabels, testData, testLabels, weights, trainRMSEs, testRMSEs, DISPLAY);

			testData = null; testLabels = null; trainData = null; trainLabels = null;
		}

		double averTrRMSE = 0, averTeRMSE = 0;
		for (int i = 0; i < trainRMSEs.size(); i++) {
			averTrRMSE += trainRMSEs.get(i);
			averTeRMSE += testRMSEs.get(i);
		}
		averTrRMSE /= trainRMSEs.size(); averTeRMSE /= trainRMSEs.size();
		System.out.println("Average Training RMSE = " + averTrRMSE);
		System.out.println("Average Testing RMSE = " + averTeRMSE);

		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./TrRMSE9", true)))) {
			writer.printf("%f\n", averTrRMSE);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./TeRMSE9", true)))) {
			writer.printf("%f\n", averTeRMSE);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// main method 
	public static void 
	main(
		String[] args
	) {
		if (args.length != 3)
		{
			System.out.println("Usage: java RidgeRegression <dataDir> <lambda> <power>");
			System.out.println("Example: java RidgeRegression ../../data/sinData_Train.csv 0.2 5");
			System.exit(0);
		}

		init(args);
		FileParser.readData(fileDir, ",", allData, allLabels, POWER);
		FileParser.center(allData);
		run();
	}
}
