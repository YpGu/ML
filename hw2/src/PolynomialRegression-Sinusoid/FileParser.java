/**
	FileParser.java: read input files
**/

import java.util.*;
import java.io.*;

public class FileParser
{
	private static Random randGenerator = new Random(0);

	// read number of features 
	public static int 
	readNumOfFeatures(
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
	public static int 
	readNumOfInstances(
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
				inputs[lineID][(tokens.length-1)*p] = 1;
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
		double[][] trainData,
		double[][] testData
	) {
		int M = trainData[0].length;
		double[] aver = new double[M];
		for (int j = 0; j < M; j++) {
			for (int i = 0; i < trainData.length; i++) {
				aver[j] += trainData[i][j];
			}
			aver[j] /= trainData.length;
		}

		for (int j = 0; j < M; j++) {
			for (int i = 0; i < trainData.length; i++) {
				trainData[i][j] -= aver[j];
			}
		}
		for (int j = 0; j < M; j++) {
			for (int i = 0; i < testData.length; i++) {
				testData[i][j] -= aver[j];
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
}
