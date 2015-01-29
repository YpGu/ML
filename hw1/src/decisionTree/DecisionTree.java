/**
	DecisionTree.java: the main class for a decision tree
**/

import java.util.*;

public class DecisionTree
{
	public static int NUM_FEATURES;
	public final static int NUM_CROSS_VALIDATION = 10;
	public final static boolean DISPLAY = false;

	public static void evaluate(Tree tree, Node root, ArrayList<ArrayList<DataType>> cv, int fold)
	{
		int trainCor = 0, testCor = 0;
		int trainTot = 0, testTot = 0;
		for (int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			if (i != fold)
			{
				for (int j = 0; j < cv.get(i).size(); j++)
				{
					if (cv.get(i).get(j).getLabel().equals(tree.query(cv.get(i).get(j), root)))
						trainCor++;
//					System.out.println("Real Label = " + rootData.get(i).getLabel() + " Predicted Label = " + tree.query(rootData.get(i), root));
				}
				trainTot += cv.get(i).size();
			}
			else
			{
				for (int j = 0; j < cv.get(i).size(); j++)
				{
					if (cv.get(i).get(j).getLabel().equals(tree.query(cv.get(i).get(j), root)))
						testCor++;
//					System.out.println("Real Label = " + rootData.get(i).getLabel() + " Predicted Label = " + tree.query(rootData.get(i), root));
				}
				testTot += cv.get(i).size();
			}
		}
		System.out.println("Training accuracy = " + trainCor + "/" + trainTot);
		System.out.println("Testing accuracy = " + testCor + "/" + testTot);
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

		for (int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			System.out.println("\n======= Cross Validation: Fold " + i + "/" + NUM_CROSS_VALIDATION + " =======");
			root.setData(cv, i, NUM_CROSS_VALIDATION);

			Tree tree = new Tree(root);
			tree.buildTree(tree.getRoot(), DISPLAY);
			if (DISPLAY)
				System.out.println("=============================================\nTraining ends.");

			evaluate(tree, root, cv, i);
		}

		return;
	}
}
