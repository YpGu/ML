/**
	DecisionTree.java: the main class for a decision tree
**/

import java.util.*;

public class DecisionTree
{
	public static int NUM_FEATURES;

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

		root.setData(rootData);

		Tree tree = new Tree(root);
		tree.buildTree(tree.getRoot());
		System.out.println("=============================================");
		System.out.println("Training ends.");

		int cor = 0;
		for (int i = 0; i < 150; i++)
		{
			if (rootData.get(i).getLabel().equals(tree.query(rootData.get(i), root)))
				cor++;
//			System.out.println("Real Label = " + rootData.get(i).getLabel() + " Predicted Label = " + tree.query(rootData.get(i), root));
		}
		System.out.println(cor);

		return;
	}
}
