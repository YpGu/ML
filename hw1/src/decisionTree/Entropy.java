/**
	Entropy class: calculate entropy
**/

import java.util.*;

public class Entropy
{
	public static double calcEntropy(ArrayList<DataType> data, int feature, double threshold)
	{
		if (data.size() == 0)
		{
			System.out.println("data size = 0");
			return 0;
		}

		ArrayList<DataType> h1 = new ArrayList<DataType>(), h2 = new ArrayList<DataType>();

		int count = 0;
		for (int i = 0; i < data.size(); i++)
		{
			double val = data.get(i).getAttr().get(feature);
			if (val <= threshold)
			{
				h1.add(data.get(i));
				count++;
			}
			else
			{
				h2.add(data.get(i));
			}
		}

		// calculate entropy for h1 and h2 
		Map<String, Integer> eCalc1 = new HashMap<String, Integer>(), eCalc2 = new HashMap<String, Integer>();
		for (int i = 0; i < h1.size(); i++)
		{
			String label = h1.get(i).getLabel();
			if (eCalc1.containsKey(label))
				eCalc1.put(label, eCalc1.get(label)+1);
			else
				eCalc1.put(label, 1);
		}
		for (int i = 0; i < h2.size(); i++)
		{
			String label = h2.get(i).getLabel();
			if (eCalc2.containsKey(label))
				eCalc2.put(label, eCalc2.get(label)+1);
			else
				eCalc2.put(label, 1);
		}
		double e1 = 0, e2 = 0;
//		System.out.println(eCalc1.keySet());
		for (String str: eCalc1.keySet())
		{
			int strCount = eCalc1.get(str);
			if (strCount != 0)
			{
				double port = (double)strCount/count;
				e1 += port * Math.log(port) / Math.log(2);
			}
		}
		for (String str: eCalc2.keySet())
		{
			int strCount = eCalc2.get(str);
			if (strCount != 0)
			{
				double port = (double)strCount/(data.size()-count);
				e2 += port * Math.log(port) / Math.log(2);
			}
		}


		double prob = (double)count/data.size();
		if (prob == 0)
			return e2;
		else if (prob == 1)
			return e1;

		double infoGain = prob * e1 + (1-prob) * e2;

//		if (infoGain == 0)
//		{
//			System.out.println("prob = " + prob + " e1 = " + e1 + " e2 = " + e2);
//		}

		return infoGain;
	}
}
