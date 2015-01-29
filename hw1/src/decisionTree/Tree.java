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

	public void buildTree(Node root)
	{
		System.out.println("=============================================");
		System.out.println("Level " + root.getLevel() + "\tSize = " + root.getData().size() + "\tUsed " + root.getUsedAttr().size() + " attribute(s)");
		if (root.getUsedAttr().size() != 0)
			System.out.println("Those attributes are: " + root.getUsedAttr());

		// check if stop criterion is met 
		if (root.getData().size() == 0)
		{
			root.setLeaf(true);
			return;
		}

		if (root.getUsedAttr().size() == DecisionTree.NUM_FEATURES)
		{
			root.setLeaf(true);
			majorityVote(root);
			System.out.println("We arrive at a leaf node because we used up all attributes!");
			System.out.println("Label at this leaf is " + root.getLabel() + ".");
			return;
		}

		// root is not leaf node 
		double maxInfoGain = -100;
		int bestAttr = -1;
		double bestThreshold = 0;

		for (int i = 0; i < DecisionTree.NUM_FEATURES; i++)
		{
			if (root.getUsedAttr().contains(i))
				continue;

			System.out.println("  Checking Attribute " + i);

			// select threshold for feature #i 
			for (int j = 0; j < root.getData().size(); j++)
			{
				double threshold = root.getData().get(j).getAttr().get(i);
				double tmpInfoGain = Entropy.calcEntropy(root.getData(), i, threshold);
				if (tmpInfoGain > maxInfoGain)
				{
					maxInfoGain = tmpInfoGain;
					bestAttr = i;
					bestThreshold = threshold;
				}
			}

			System.out.println("    Best info gain so far = " + maxInfoGain);
		}

		System.out.printf("bestAttr = %d, ", bestAttr);
		System.out.printf("bestThreshold = %f, ", bestThreshold);
		System.out.println("maxInfoGain = " + maxInfoGain);

		// set root node 
		if (maxInfoGain != 0)
		{
			root.setLeaf(false);
			root.setAttr(bestAttr);
			root.setThreshold(bestThreshold);

/**			I don't know why it doesn't work: 
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

			System.out.println("Children size: " + root.getLChild().getData().size() + " " + root.getRChild().getData().size());

			buildTree(root.getLChild());
			buildTree(root.getRChild());
		}
		else
		{
			root.setLeaf(true);
			majorityVote(root);
			System.out.println("We arrive at a leaf node because IG = 0!");
			System.out.println("Label at this leaf is " + root.getLabel() + ".");
			return;
		}

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

	public String query(DataType dt, Node root)
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
		Map<String, Integer> MV = new HashMap<String, Integer>();
		for (int j = 0; j < root.getData().size(); j++)
		{
			String label = root.getData().get(j).getLabel();
			if (MV.containsKey(label))
				MV.put(label, MV.get(label)+1);
			else
				MV.put(label, 1);
		}
		// majority vote
		int maxCount = -1;
		for (String str: MV.keySet())
		{
			if (MV.get(str) > maxCount)
			{
				maxCount = MV.get(str);
				root.setLabel(str);
			}
		}
	}

}
