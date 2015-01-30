/**
	Tree.java: we build decision tree here and take record as inputs 
**/

import java.util.*;

public class Tree
{
	private Node tRoot;


	public Tree(Node root)
	{
		this.tRoot = root;
	}


	public Node getRoot()
	{
		return this.tRoot;
	}

	public void setRoot(Node tRoot)
	{
		this.tRoot = tRoot;
	}

	public void buildTree(Node root, boolean display, double eta)
	{
		if (display)
		{
			System.out.println("=============================================");
			System.out.println("Level " + root.getLevel() + "\tSize = " + root.getData().size() + "\tUsed " + root.getUsedAttr().size() + " attribute(s)");

			if (root.getUsedAttr().size() != 0)
				System.out.println("Those attributes are: " + root.getUsedAttr());
		}

		// check if stop criterion is met 
		if (root.getData().size() == 0)
		{
			root.setLeaf(true);
			root.setLabel(0);
			return;
		}

		if (root.getUsedAttr().size() == RegressionTree.NUM_FEATURES)
		{
			root.setLeaf(true);
			majorityVote(root);
			if (display)
				System.out.println("We arrive at a leaf node because we used up all attributes!\nLabel at this leaf is " + root.getLabel() + ".");
			return;
		}

		// early stop strategy
		if (root.getData().size() <= eta)
		{
			root.setLeaf(true);
			majorityVote(root);
			if (display)
				System.out.println("We arrive at a leaf node because early stop criterion is met!");
			return;
		}

		// root is not leaf node 
		double minMSE = Double.MAX_VALUE;
		int bestAttr = -1;
		double bestThreshold = 0;

		for (int i = 0; i < RegressionTree.NUM_FEATURES; i++)
		{
			if (root.getUsedAttr().contains(i))
				continue;

			if (display)
				System.out.println("  Checking Attribute " + i);

			// select threshold for feature #i 
			for (int j = 0; j < root.getData().size(); j++)
			{
				double threshold = root.getData().get(j).getAttr().get(i);
				double tmpMSE = MSE.calcMSE(root.getData(), i, threshold);
				if (tmpMSE < minMSE)
				{
					minMSE = tmpMSE;
					bestAttr = i;
					bestThreshold = threshold;
				}
			}

			if (display)
				System.out.println("    Min MSE so far = " + minMSE);
		}

		if (display)
		{
			System.out.printf("bestAttr = %d, ", bestAttr);
			System.out.printf("bestThreshold = %f, ", bestThreshold);
			System.out.println("minMSE = " + minMSE);
		}

		// set root node 
		if (true)
		{
			root.setLeaf(false);
			root.setAttr(bestAttr);
			root.setThreshold(bestThreshold);

/**	I don't know why it doesn't work: 
			Node lc = new Node();
			lc.setParent(root);
			lc.setData(filter(root, bestAttr, bestThreshold, 0));
			lc.setUsedAttrs(root.getUsedAttr());
			lc.addUsedAttr(bestAttr);
			lc.setLevel(root.getLevel() + 1);
			root.setLChild(lc);

			Node rc = new Node();
			rc.setParent(root);
			rc.setData(filter(root, bestAttr, bestThreshold, 1));
			rc.setUsedAttrs(root.getUsedAttr());
			rc.addUsedAttr(bestAttr);
			rc.setLevel(root.getLevel() + 1);
			root.setLChild(rc);
*/

			ArrayList<Integer> tmpAL = new ArrayList<Integer>();
			for (int i = 0; i < root.getUsedAttr().size(); i++)
			{
				int attr = root.getUsedAttr().get(i);
				tmpAL.add(attr);
			}
			tmpAL.add(bestAttr);

			Node lc = new Node(
				root, 
				root.getLevel() + 1,
				tmpAL,
				filter(root, bestAttr, bestThreshold, 0)
			);
			root.setLChild(lc);

			Node rc = new Node(
				root, 
				root.getLevel() + 1, 
				tmpAL,
				filter(root, bestAttr, bestThreshold, 1)
			);
			root.setRChild(rc);

			if (display)
				System.out.println("Children size: " + root.getLChild().getData().size() + " " + root.getRChild().getData().size());

			buildTree(root.getLChild(), display, eta);
			buildTree(root.getRChild(), display, eta);
		}
//		else
//		{
//			root.setLeaf(true);
//			majorityVote(root);
//			if (display)
//				System.out.println("We arrive at a leaf node because IG = 0!\nLabel at this leaf is " + root.getLabel() + ".");
//			return;
//		}

		return;
	}

	public ArrayList<DataType> filter(Node curNode, int feature, double val, int option)
	{
		ArrayList<DataType> res = new ArrayList<DataType>();
		for (int i = 0; i < curNode.getData().size(); i++)
		{
			DataType datum = curNode.getData().get(i);
			if (datum.getAttr().get(feature) <= val && option == 0)
				res.add(datum);
			if (datum.getAttr().get(feature) > val && option == 1)
				res.add(datum);
		}

		return res;
	}

	public double query(DataType dt, Node root)
	{
		if (root.isLeaf())
			return root.getLabel();

		String res = "";
		Node curNode = root;

		Node nextNode = new Node();
		int attr = curNode.getAttr();
		double val = dt.getAttr().get(attr);
		double threshold = curNode.getThreshold();
		if (val <= threshold)
			nextNode = curNode.getLChild();
		else
			nextNode = curNode.getRChild();

		return query(dt, nextNode);
	}

	public void majorityVote(Node root)
	{
		double ave = 0;
		for (int j = 0; j < root.getData().size(); j++)
		{
			double y = root.getData().get(j).getLabel();
			ave += y;
		}
		if (root.getData().size() != 0)
			ave /= (double)root.getData().size(); 

		root.setLabel(ave);
	}

}
