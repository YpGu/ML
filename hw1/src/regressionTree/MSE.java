/**
	MSE class: calculate entropy / information gain
**/

import java.util.*;

public class MSE
{
	public static double calcMSE(ArrayList<DataType> data, int feature, double threshold)
	{
		if (data.size() == 0)
		{
			System.out.println("data size = 0");
			return 0;
		}

		ArrayList<DataType> h1 = new ArrayList<DataType>(), h2 = new ArrayList<DataType>();

		int size1 = 0, size2 = 0;
		double ave1 = 0, ave2 = 0;
		for (int i = 0; i < data.size(); i++)
		{
			double val = data.get(i).getAttr().get(feature);
			double y = data.get(i).getLabel();
			if (val <= threshold)
			{
				h1.add(data.get(i));
				ave1 += y;
				size1++;
			}
			else
			{
				h2.add(data.get(i));
				ave2 += y;
				size2++;
			}
		}
		if (size1 != 0)
			ave1 /= (double)size1;
		if (size2 != 0)
			ave2 /= (double)size2;

		// calculate MSE for h1 and h2
		double mse1 = 0, mse2 = 0; 
		Map<String, Integer> eCalc1 = new HashMap<String, Integer>(), eCalc2 = new HashMap<String, Integer>();
		for (int i = 0; i < h1.size(); i++)
		{
			double y = h1.get(i).getLabel();
			mse1 += (y-ave1) * (y-ave1);
		}
		for (int i = 0; i < h2.size(); i++)
		{
			double y = h2.get(i).getLabel();
			mse2 += (y-ave2) * (y-ave2);
		}

		double mse = (mse1 + mse2) / (size1 + size2);

		return mse;
	}
}
