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

				// read attributes 
				for (int i = 0; i < numAttrs; i++)
				{
					double curAttr = Double.parseDouble(tokens[i].trim());
					dt.addAttr(curAttr);
				}

				// read label 
				String label = tokens[numAttrs];
				dt.setLabel(label);

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
