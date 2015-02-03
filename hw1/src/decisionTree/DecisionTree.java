/**
	DecisionTree.java: the main class for a decision tree
**/

import java.util.*;
import java.io.*;

public class DecisionTree
{
	public static int NUM_FEATURES;
	public final static int NUM_CROSS_VALIDATION = 10;
	public final static boolean DISPLAY = false;

	public static double evaluate(Tree tree, Node root, ArrayList<ArrayList<DataType>> cv, int fold)
	{
		int trainCor = 0, testCor = 0;
		int trainTot = 0, testTot = 0;
		for (int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			if (i != fold)
			{
				for (int j = 0; j < cv.get(i).size(); j++)
				{
					String groundTruthLabel = cv.get(i).get(j).getLabel();
					String predictedLabel = tree.query(cv.get(i).get(j), root);
					if (groundTruthLabel.equals(predictedLabel))
						trainCor++;
				}
				trainTot += cv.get(i).size();
			}
			else
			{
				for (int j = 0; j < cv.get(i).size(); j++)
				{
					String groundTruthLabel = cv.get(i).get(j).getLabel();
					String predictedLabel = tree.query(cv.get(i).get(j), root);
					if (groundTruthLabel.equals(predictedLabel))
						testCor++;
				}
				testTot += cv.get(i).size();
			}
		}

		String trainRes = "Training accuracy = " + trainCor + "/" + trainTot + "\n";
		String testRes = "Testing accuracy = " + testCor + "/" + testTot + "\n";
		try
		{
			File file = new File("output.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(trainRes);
			bw.write(testRes);
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("Training accuracy = " + trainCor + "/" + trainTot);
		System.out.println("Testing accuracy = " + testCor + "/" + testTot);

		double testAcc = (double)testCor/testTot;
		return testAcc;
	}

	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Wrong arguments.\nUsage: java DecisionTree <fileDir>");
			return;
		}

		String fileDir = args[0];		// to be read 

		Node root = new Node();
		ArrayList<DataType> rootData = new ArrayList<DataType>();
		NUM_FEATURES = FileParser.readData(fileDir, ",", rootData);

		ArrayList<ArrayList<DataType>> cv = new ArrayList<ArrayList<DataType>>();
		FileParser.crossValidation(rootData, cv, NUM_CROSS_VALIDATION);

		double[] etas = new double[]{0.05, 0.1, 0.15, 0.2, 0.25};

		ArrayList<Double> testAcc = new ArrayList<Double>();

		for (double eta: etas)
		{
			System.out.println("\nUsing eta: " + eta);
			for (int i = 0; i < NUM_CROSS_VALIDATION; i++)
			{
				System.out.println("\n======= Cross Validation: Fold " + i + "/" + NUM_CROSS_VALIDATION + " =======");
				root.setData(cv, i, NUM_CROSS_VALIDATION);

				Tree tree = new Tree(root);
				tree.buildTree(tree.getRoot(), DISPLAY, eta * root.getData().size());
				if (DISPLAY)
					System.out.println("=============================================\nTraining ends.");

				testAcc.add(evaluate(tree, root, cv, i));
			}
		}

		StandardDeviation.calcSD(testAcc);

		return;
	}
}
