/**
	Node.java: node (class) in the tree
**/

import java.util.*;

public class Node
{
	private Node parent;
	public Node[] children;
	private Node lChild, rChild;
	private int level;
	private boolean leafNode;

	private String label;			// class label if the node is a leaf node 

	private ArrayList<DataType> data;	// set of data in the node 
	private ArrayList<Integer> usedAttrs;	// set of attributes used so far 

	private int attribute;			// current criterion for split 
	private double threshold;		// current threshold for split 


	public Node()
	{
		this.parent = null;
//		this.children = new Node[2];
		this.data = new ArrayList<DataType>();
		this.usedAttrs = new ArrayList<Integer>();
		this.attribute = -1;
		this.level = 0;
	}

//	public Node(Node root, int level, ArrayList<Integer> attrs, int attr, ArrayList<DataType> dt)
	public Node(Node root, int level, ArrayList<Integer> attrs, ArrayList<DataType> dt)
	{
		this.parent = root;
		this.level = level;
		ArrayList<Integer> tmpAttrs = attrs;
//		tmpAttrs.add(attr);
		this.usedAttrs = tmpAttrs;
		this.data = dt;
	}

	public Node[] getChildren()
	{
		return this.children;
	}

	public void setChildren(Node[] children)
	{
		this.children = children;
	}

	public Node getLChild()
	{
		return this.lChild;
	}
	public Node getRChild()
	{
		return this.rChild;
	}
	public void setLChild(Node lChild)
	{
		this.lChild = lChild;
	}
	public void setLChild(Node root, int level, ArrayList<Integer> attrs, int attr, ArrayList<DataType> dt)
	{
//		this.lChild = new Node();
		this.lChild.parent = root;
		this.lChild.level = level;
		ArrayList<Integer> tmpAttrs = attrs;
		tmpAttrs.add(attr);
		this.lChild.usedAttrs = tmpAttrs;
		this.lChild.data = dt;
	}
	public void setRChild(Node rChild)
	{
		this.rChild = rChild;
	}
	public void setRChild(Node root, int level, ArrayList<Integer> attrs, int attr, ArrayList<DataType> dt)
	{
//		this.rChild = new Node();
		this.rChild.parent = root;
		this.rChild.level = level;
		ArrayList<Integer> tmpAttrs = attrs;
		tmpAttrs.add(attr);
		this.rChild.usedAttrs = tmpAttrs;
//		this.rChild.usedAttrs.add(attr);
		this.rChild.data = dt;
	}


	public Node getParent()
	{
		return this.parent;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}


	public int getLevel()
	{
		return this.level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}


	public boolean isLeaf()
	{
		return leafNode;
	}

	public void setLeaf(boolean isLeaf)
	{
		this.leafNode = isLeaf;
	}


	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}


	public ArrayList<DataType> getData()
	{
		return this.data;
	}

	public void setData(ArrayList<DataType> data)
	{
		this.data = data;
	}

	public void addData(DataType datum)
	{
		this.data.add(datum);
	}


	public int getAttr()
	{
		return this.attribute;
	}

	public void setAttr(int attr)
	{
		this.attribute = attr;
	}


	public double getThreshold()
	{
		return this.threshold;
	}

	public void setThreshold(double threshold)
	{
		this.threshold = threshold;
	}


	public ArrayList<Integer> getUsedAttr()
	{
		return this.usedAttrs;
	}

	public void setUsedAttrs(ArrayList<Integer> attrs)
	{
		this.usedAttrs = attrs;
	}

	public void addUsedAttr(int attr)
	{
		this.usedAttrs.add(attr);
	}

/*
	public void split()
	{
		// stop criterion if necessary

		if (this.data.size() == 0)
		{
			this.setLeaf(true);
			this.setLabel("");
//			this.setLabel(this.majorityVote());

			return;
		}

		if (this.remainingAttrs.size() == 0)
		{
			this.setLeaf(true);
			this.setLabel(this.majorityVote());

			return;
		}

		// thete's still data left and attributes left 
		double minEntropy = 1;
		double bestThreshold = 0;
		int bestAttr = -1;

		for (int attr: remainingAttrs)
		{
			for (int j = 0; j < this.getData.size(); j++)
			{
				double tmpThreshold = this.getData.get(j).getAttr(attr);
				double tmpEntropy = Entropy.calcEntropy(this.data, attr, tmpThreshold);
				if (tmpEntropy < minEntropy)
				{
					minEntropy = tmpEntropy;
					bestThreshold = tmpThreshold;
					bestAttr = attr;
				}
			}
		}

		if (bestAttr != -1)
		{
			this.setAttr(bestAttr);
			this.setThreshold(bestThreshold);
			this.children[0] = new Node();
			this.children.setData(filter(this.getData(), this.getAttr(), this.getThreshold(), 0));
			this.children[1] = new Node();
			this.children.setData(filter(this.getData(), this.getAttr(), this.getThreshold(), 1));

			children[0].split();
			children[1].split();
		}

		return;
	}
*/

/*
	public static ArrayList<DataType> filter(ArrayList<DataType> parentData, int attr, double val, int option)
	{
		ArrayList<DataType> res = new ArrayList<DataType>();

		for (int i = 0; i < parentData.size(); i++)
		{
			DataType datum = parentData.get(i);
			if (datum.getAttr().get(attr) < val && option == 0)
				res.add(datum);
			else if (datum.getAttr().get(attr) >= val && option == 1)
				res.add(datum);
		}

		return res;
	}



	public String majorityVote()
	{
		int preAttr = root.getParent().getFeature();
		double preThreshold = root.getParent().getThreshold();
		Map<String, Integer> MV = new HashMap<String, Integer>();
		for (int j = 0; j < root.getData().size(); j++)
		{
			double curVal = root.getData().get(j).getAttr().get(preAttr);
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
*/
}
