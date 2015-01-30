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
		System.out.println("\nAverage = " + ave);

		for (int i = 0; i < arr.size(); i++)
		{
			ss += (arr.get(i) - ave) * (arr.get(i) - ave);
		}
		ss /= arr.size();

		System.out.println("Standard Deviation is " + Math.sqrt(ss));
	}
}
