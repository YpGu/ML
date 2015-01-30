/**
	RegressionTree.java: the main class for a decision tree
**/

import java.util.*;
import java.io.*;

public class RegressionTree
{
	public static int NUM_FEATURES;
	public final static int NUM_CROSS_VALIDATION = 10;
	public final static boolean DISPLAY = false;

	public static double evaluate(Tree tree, Node root, ArrayList<ArrayList<DataType>> cv, int fold)
	{
		double trainMSE = 0, testMSE = 0;
		int trainTot = 0, testTot = 0;
		for (int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			if (i != fold)
			{
				for (int j = 0; j < cv.get(i).size(); j++)
				{
					trainMSE += Math.pow(cv.get(i).get(j).getLabel() - tree.query(cv.get(i).get(j), root), 2);
//					System.out.println("Real Label = " + rootData.get(i).getLabel()");
//					System.out.println("Predicted Label = " + tree.query(rootData.get(i), root));
				}
				trainTot += cv.get(i).size();
			}
			else
			{
				for (int j = 0; j < cv.get(i).size(); j++)
				{
					testMSE += Math.pow(cv.get(i).get(j).getLabel() - tree.query(cv.get(i).get(j), root), 2);
//					System.out.println("Real Label = " + rootData.get(i).getLabel()");
//					System.out.println("Predicted Label = " + tree.query(rootData.get(i), root));
				}
				testTot += cv.get(i).size();
			}
		}

		if (trainTot != 0)
			trainMSE /= trainTot;
		if (testTot != 0)
			testMSE /= testTot;

		String trainRes = "Training MSE = " + trainMSE;
		String testRes = "Testing MSE = " + testMSE;
		try
		{
			File file = new File("output.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(trainRes + "\n");
			bw.write(testRes + "\n");
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println(trainRes);
		System.out.println(testRes);

		return testMSE;
	}

	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Wrong arguments.\nUsage: java RegressionTree <fileDir>");
			return;
		}

		String fileDir = args[0];		// to be read 

		Node root = new Node();
		ArrayList<DataType> rootData = new ArrayList<DataType>();
		NUM_FEATURES = FileParser.readData(fileDir, ",", rootData);

		ArrayList<ArrayList<DataType>> cv = new ArrayList<ArrayList<DataType>>();
		FileParser.crossValidation(rootData, cv, NUM_CROSS_VALIDATION);

		double[] etas = new double[]{0.05, 0.1, 0.15, 0.2};

		for (double eta: etas)
		{
			ArrayList<Double> testMSEs = new ArrayList<Double>();

			System.out.println("\nUsing eta " + eta);
			for (int i = 0; i < NUM_CROSS_VALIDATION; i++)
			{
				System.out.println("\n======= Cross Validation: Fold " + i + "/" + NUM_CROSS_VALIDATION + " =======");
				root.setData(cv, i, NUM_CROSS_VALIDATION);

				Tree tree = new Tree(root);
				tree.buildTree(tree.getRoot(), DISPLAY, eta * root.getData().size());
				if (DISPLAY)
					System.out.println("=============================================\nTraining ends.");

				testMSEs.add(evaluate(tree, root, cv, i));
			}

			StandardDeviation.calcSD(testMSEs);
		}

		return;
	}
}
