/**
	FileReader.java: read input files
**/

import java.util.*;
import java.io.*;

public class FileParser
{
	public static int readNumOfFeatures(String fileDir, String delimiter) {
		int numAttrs = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine = br.readLine();
			String[] tokens = currentLine.split(delimiter);
			numAttrs = tokens.length-1;
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return numAttrs;
	}

	public static int readNumOfInstances(String fileDir, String delimiter) {
		int numInstances = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.equals(""))
					continue;
				numInstances += 1;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return numInstances;
	}

	public static void readData(String fileDir, String delimiter, double[][] inputs, int[] outputs, int M) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			int lineID = 0;
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.equals(""))
					continue;

				// parse line here
				String[] tokens = currentLine.split(delimiter);
				for (int i = 0; i < M; i++) {
					double curAttr = Double.parseDouble(tokens[i].trim());
					inputs[lineID][i] = curAttr;
				}
				outputs[lineID] = Integer.parseInt(tokens[M].trim())-1;			// to make the index start from 0 
//				if (outputs[lineID] > 2) System.out.println(outputs[lineID]);
				lineID++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	// Output
	public static void output(String outputDir, double[] arr) {
		int N = arr.length;
		try (PrintWriter writer = new PrintWriter(outputDir, "UTF-8")) {
			for (int x = 0; x < N; x++)
				writer.printf("%d\t%f\n", x, arr[x]);
		}
		catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return;
	}

	public static void output(String outputDir, ArrayList<Double> arr) {
		int N = arr.size();
		try (PrintWriter writer = new PrintWriter(outputDir, "UTF-8")) {
			for (int x = 0; x < N; x++)
				writer.printf("%d\t%f\n", x, arr.get(x));
		}
		catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return;
	}


/*	public static void crossValidation(ArrayList<DataType> data, ArrayList<ArrayList<DataType>> cv, int num)
	{
		for (int i = 0; i < num; i++)
		{
			cv.add(new ArrayList<DataType>());
		}

		for (int i = 0; i < data.size(); i++)
		{
			int ind = i%num;
			cv.get(ind).add(data.get(i));
		}
	}
*/		
}
