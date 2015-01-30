/**
	DataType.java: information for each record
**/

import java.util.*;

public class DataType
{
	private ArrayList<Double> attrs;
	private double label;


	public DataType()
	{
		this.attrs = new ArrayList<Double>();
	}


	public ArrayList<Double> getAttr()
	{
		return this.attrs;
	}

	public void setAttr(ArrayList<Double> attrs)
	{
		this.attrs = attrs;
	}

	public void addAttr(double attr)
	{
		this.attrs.add(attr);
	}


	public double getLabel()
	{
		return this.label;
	}

	public void setLabel(double label)
	{
		this.label = label;
	}
}
