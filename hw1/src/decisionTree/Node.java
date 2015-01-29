/**
	Node.java: node (class) in the tree
**/

import java.util.*;

public class Node
{
	private Node parent;
	private Node lChild, rChild;
	private int level;
	private boolean leafNode;

	private String label;				// class label if the node is a leaf node 

	private ArrayList<DataType> data;		// set of data in the node 
	private ArrayList<Integer> usedAttrs;		// set of attributes used so far 

	private int attribute;				// current criterion for split 
	private double threshold;			// current threshold for split 


	public Node()
	{
		this.parent = null;
		this.data = new ArrayList<DataType>();
		this.usedAttrs = new ArrayList<Integer>();
		this.attribute = -1;
		this.level = 0;
	}

	public Node(Node root, int level, ArrayList<Integer> attrs, ArrayList<DataType> dt)
	{
		this.parent = root;
		this.level = level;
		ArrayList<Integer> tmpAttrs = attrs;
		this.usedAttrs = tmpAttrs;
		this.data = dt;
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

	public void setRChild(Node rChild)
	{
		this.rChild = rChild;
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
}
