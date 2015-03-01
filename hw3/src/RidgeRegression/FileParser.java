/**
	FileParser.java: read input files
**/

import java.util.*;
import java.io.*;

public class FileParser
{
	private static Random randGenerator = new Random(0);

	// read number of features 
	public static int readNumOfFeatures(
		String fileDir, 
		String delimiter
	) {
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

	// read number of instances 
	public static int readNumOfInstances(
		String fileDir, 
		String delimiter
	) {
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

	// read data 
	public static void
	readData(
		String fileDir, 
		String delimiter, 
		double[][] inputs, 
		double[][] outputs, 
		int p						// up to power = p, e.g. (x, x^2, ..., x^p) 
	) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			int lineID = 0;
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.equals(""))
					continue;

				// parse line here
				String[] tokens = currentLine.split(delimiter);

				for (int i = 0; i < tokens.length-1; i++) {
					double ori = Double.parseDouble(tokens[i].trim());
					for (int j = 0; j < p; j++) {
						inputs[lineID][i*p+j] = Math.pow(ori, j+1);
					}
				}
				outputs[lineID][0] = Double.parseDouble(tokens[tokens.length-1].trim());

				lineID++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	// pro-processing: center data
	public static void
	center(
		double[][] data
	) {
		double[] aver = new double[data[0].length];
		for (int j = 0; j < data[0].length; j++) {
			for (int i = 0; i < data.length; i++) {
				aver[j] += data[i][j];
			}
			aver[j] /= data[0].length;
		}
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j] -= aver[j];
			}
		}

		return;
	}

	// output
	public static void 
	output(
		String outputDir, 
		double[] arr
	) {
		int N = arr.length;
		try (PrintWriter writer = new PrintWriter(outputDir, "UTF-8")) {
			for (int x = 0; x < N; x++)
				writer.printf("%d\t%f\n", x, arr[x]);
		}
		catch (FileNotFoundException | UnsupportedEncodingException e) 	{
			e.printStackTrace();
		}

		return;
	}

	// output
	public static void
	output(
		String outputDir, 
		ArrayList<Double> arr
	) {
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

	// split data into training and testing 
	public static void 
	crossValidation(
		double[][] allData,
		double[][] allLabels,
		double[][] trainData,
		double[][] trainLabels,
		double[][] testData,
		double[][] testLabels,
		int fold
	) {
		int trI = 0, teI = 0;
		for (int i = 0; i < allData.length; i++) {
			int ind = i%RidgeRegression.NUM_OF_FOLDS;
			if (ind != fold) {
				trainData[trI] = allData[i];
				trainLabels[trI][0] = allLabels[i][0];
				trI++;
			}
			else {
				testData[teI] = allData[i];
				testData[teI][0] = allLabels[i][0];
				teI++;
			}
		}

		return;
	}
}
