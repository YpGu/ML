/**
	FileReader.java: read input files
**/

import java.util.*;
import java.io.*;

public class FileParser
{
	private static Random randGenerator = new Random(0);

	public static int readData(String fileDir, String delimiter, ArrayList<DataType> res)
	{
		int numAttrs = -1;

		// read dimension
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir)))
		{
			String currentLine = br.readLine();
			String[] tokens = currentLine.split(delimiter);
			numAttrs = tokens.length-1;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// read max/min (to do normalization) 
		ArrayList<Double> minValue = new ArrayList<Double>(10);
		ArrayList<Double> maxValue = new ArrayList<Double>(10);
		ArrayList<Boolean> minEqualsMax = new ArrayList<Boolean>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir)))
		{
			String currentLine;
			int lineID = 0;
			while ((currentLine = br.readLine()) != null)
			{
				if (currentLine.equals(""))
					continue;

				// parse line here
				String[] tokens = currentLine.split(delimiter);

				// read min/max of attributes 
				for (int i = 0; i < numAttrs; i++)
				{
					double curAttr = Double.parseDouble(tokens[i].trim());
					if (lineID != 0)
					{
						if (curAttr > maxValue.get(i))
							maxValue.set(i, curAttr);
						if (curAttr < minValue.get(i))
							minValue.set(i, curAttr);
					}
					else
					{
						minValue.add(i, curAttr);
						maxValue.add(i, curAttr);
					}
				}
				lineID++;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		for (int i = 0; i < numAttrs; i++)
		{
			if (minValue.get(i) == maxValue.get(i))
				minEqualsMax.add(i, true);
			else
				minEqualsMax.add(i, false);
		}

		// read raw data and do normalization 
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir)))
		{
			String currentLine;
			while ((currentLine = br.readLine()) != null)
			{
				if (currentLine.equals(""))
					continue;

				DataType dt = new DataType();

				// parse line here
				String[] tokens = currentLine.split(delimiter);

				// read attributes and do normalizatio 
				for (int i = 0; i < numAttrs; i++)
				{
					double curAttr = Double.parseDouble(tokens[i].trim());
					if (!minEqualsMax.get(i))
						curAttr = (curAttr - minValue.get(i)) / (maxValue.get(i) - minValue.get(i));
					dt.addAttr(curAttr);
				}

				// read label 
				String label = tokens[numAttrs];
				double y = Double.parseDouble(label);
				dt.setLabel(y);

				// add dt to ArrayList
				res.add(dt);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return numAttrs;
	}

	public static void crossValidation(ArrayList<DataType> data, ArrayList<ArrayList<DataType>> cv, int num)
	{
		for (int i = 0; i < num; i++)
		{
			cv.add(new ArrayList<DataType>());
		}

		for (int i = 0; i < data.size(); i++)
		{
			int ind = i%num;
//			int ind = randGenerator.nextInt(num);
			cv.get(ind).add(data.get(i));
		}
	}
		
}
