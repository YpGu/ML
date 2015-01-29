/**
	DataType.java: information for each record
**/

import java.util.*;

public class DataType
{
	private ArrayList<Double> attrs;
	private String label;


	public DataType()
	{
		this.attrs = new ArrayList<Double>();
		this.label = null;
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


	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
}
