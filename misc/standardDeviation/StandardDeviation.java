import java.util.*;
import java.lang.*;

public class StandardDeviation
{
	public static void calcSD(ArrayList<Double> arr)
	{
		double ss = 0;		// sum of squares
		double ave = 0;
		for (int i = 0; i < arr.size(); i++)
		{
			ave += arr.get(i);
		}
		ave /= arr.size();
		System.out.println("Average = " + ave);

		for (int i = 0; i < arr.size(); i++)
		{
			ss += (arr.get(i) - ave) * (arr.get(i) - ave);
		}
		ss /= arr.size();

		System.out.println("Standard Deviation is " + Math.sqrt(ss));
	}

	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("Wrong Arguments.");
			return;
		}

		ArrayList<Double> arr = new ArrayList<Double>();
		for (int i = 0; i < args.length; i++)
		{
			try
			{
				arr.add(Double.parseDouble(args[i]));
			}
			catch (NumberFormatException e)
			{
				String[] parts = args[i].split("/");
				double val = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
				arr.add(val);
			}
		}
		calcSD(arr);

		return;
	}
}
