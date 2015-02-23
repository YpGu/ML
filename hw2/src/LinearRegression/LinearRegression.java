import java.util.*;

public class LinearRegression
{
	public static double[][] data;						// x values (data)
	public static double[] obs;						// y value
	public static double[] w;						// weights 

	public static double lr;						// learning rate 
	public static double tolerance;						// tolerance 

	public static int NUM_FEATURES;						// Number of Features 
	public static int NUM_INSTANCES;					// Number of Instances 
	public final static int MAX_ITER = 1000;				// Maximum Number of Iterations 
	public final static int NUM_FOLD = 10;					// n-fold 
	public final static String DELIMITER = ",";				// Delimiter for input files 
	public static Random rand;
	public static Scanner sc;

	public LinearRegression()
	{
	}


	public static void init(String fileDir)
	{
		rand = new Random(0);
		sc = new Scanner(System.in);

		if (fileDir.contains("housing"))
		{
			lr = 0.0004;
			tolerance = 0.005;
		}
		else if (fileDir.contains("concrete"))
		{
			lr = 0.0007;
			tolerance = 0.0001;
		}
		else if (fileDir.contains("yacht"))
		{
			lr = 0.001;
			tolerance = 0.001;
		}

		NUM_FEATURES = FileParser.readNumOfFeatures(fileDir, ",");
		NUM_INSTANCES = FileParser.readNumOfInstances(fileDir, ",");

		data = new double[NUM_INSTANCES][NUM_FEATURES+1];
		obs = new double[NUM_INSTANCES];
		w = new double[NUM_FEATURES+1];
		for (int i = 0; i < NUM_FEATURES+1; i++)
			w[i] = 0;

		return;
	}

	public static void normalize(int fold)
	{
		// z-score normalization 
		double[] aver = new double[NUM_FEATURES];
		for (int i = 0; i < NUM_FEATURES; i++)
			aver[i] = 0;
		double[] std = new double[NUM_FEATURES];
		for (int i = 0; i < NUM_FEATURES; i++)
			std[i] = 0;
		for (int i = 0; i < NUM_FEATURES; i++)
		{
			int deno = 0;
			for (int j = 0; j < NUM_INSTANCES; j++)
			{
				if (j%NUM_FOLD != fold)
				{
					aver[i] += data[j][i];
					deno += 1;
				}
			}

			aver[i] /= deno;
		}

		for (int i = 0; i < NUM_FEATURES; i++)
		{
			int deno = 0;
			for (int j = 0; j < NUM_INSTANCES; j++)
			{
				if (j%NUM_FOLD != fold)
				{
					std[i] += (data[j][i] - aver[i]) * (data[j][i] - aver[i]);
					deno += 1;
				}
			}

			std[i] /= (deno-1);
			std[i] = Math.sqrt(std[i]);
		}
		for (int i = 0; i < NUM_FEATURES; i++)
		{
			if (std[i] != 0)
			{
				for (int j = 0; j < NUM_INSTANCES; j++)
				{
//					if (j%NUM_FOLD != fold)
						data[j][i] = (data[j][i] - aver[i]) / std[i];
//					else if (j%NUM_FOLD == fold)
//						data[j][i] = (data[j][i] - aver[i]) / std[i];
				}
			}
			else
			{
				for (int j = 0; j < NUM_INSTANCES; j++)
				{
//					if (j%NUM_FOLD != fold)
						data[j][i] = 0;
//					else if (j%NUM_FOLD != fold)
//						data[j][i] = 0;
				}
			}
		}

		return;
	}

	public static double calcTrainingRMSE(int fold)
	{
		double res = 0;
		int deno = 0;
		for (int j = 0; j < NUM_INSTANCES; j++)
		{
			if (j%NUM_FOLD != fold)
			{
				double ej = obs[j];
				for (int i = 0; i < NUM_FEATURES+1; i++)
					ej -= w[i] * data[j][i];
				res += ej * ej;
				deno += 1;
			}
		}
		res /= deno;
		res = Math.sqrt(res);

		return res;
	}

	public static double calcTestingRMSE(int fold)
	{
		double res = 0;
		int deno = 0;
		for (int j = 0; j < NUM_INSTANCES; j++)
		{
			if (j%NUM_FOLD == fold)
			{
				double ej = obs[j];
				for (int i = 0; i < NUM_FEATURES+1; i++)
					ej -= w[i] * data[j][i];
				res += ej * ej;
				deno += 1;
			}
		}
		res /= deno;
		res = Math.sqrt(res);

		return res;
	}


	public static void train(int fold)
	{
		double[] grad_w = new double[NUM_FEATURES+1];

		double oldObj = -1, newObj = 0;

		for (int iter = 0; iter < MAX_ITER; iter++)
		{
//			System.out.println("Iter " + iter);

			// empty gradients
			for (int i = 0; i < NUM_FEATURES+1; i++)
				grad_w[i] = 0;

			// calculate derivatives 
			for (int j = 0; j < NUM_INSTANCES; j++)
			{
				if (j%NUM_FOLD != fold)
				{
					double ej = obs[j];
					for (int i = 0; i < NUM_FEATURES+1; i++)
						ej -= w[i] * data[j][i];

					for (int i = 0; i < NUM_FEATURES+1; i++)
						grad_w[i] -= ej * data[j][i];
				}
			}

			// update
			for (int i = 0; i < NUM_FEATURES+1; i++)
				w[i] -= lr * grad_w[i];

//			System.out.println(w[NUM_FEATURES]);
//			int doge = sc.nextInt();


			// compare objective functions 
			newObj = calcTrainingRMSE(fold);
			double rate = Math.abs(newObj - oldObj);
//			System.out.println("  New Obj = " + newObj);
			if (rate <= tolerance && iter > 0)
				break;
			oldObj = newObj;
		}

		return;
	}


	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage: java LinearRegression <fileDir> <fold>");
			System.out.println("Example: java LinearRegression ../../data/housing.csv 0");
			System.exit(0);
		}

		String fileDir = args[0];
		int fold = Integer.parseInt(args[1]);
		init(fileDir);

		FileParser.readData(fileDir, ",", data, obs);

//		System.out.println("Raw Data:");
//		for (int i = 0; i < 10; i++)
//			System.out.printf("%f\t", data[i][6]);
		normalize(fold);
//		System.out.println("\nNormalized Data:");
//		for (int i = 0; i < 10; i++)
//			System.out.printf("%f\t", data[i][6]);
//		System.out.println(" ");

		train(fold);


		System.out.println("-----------------------------------");
		double res = calcTestingRMSE(fold);
		System.out.println("Testing RMSE = " + res);
		res = calcTrainingRMSE(fold);
		System.out.println("Training RMSE = " + res);

		FileParser.output("./res/weight." + fold, w);

		FileParser.output("./res/y", obs);

/*
		for (int i = 0; i < NUM_INSTANCES; i++)
		{
			for (int j = 0; j < NUM_FEATURES; j++)
			{
				System.out.printf("%f", data[i][j]);
				System.out.printf("\t");
			}
			System.out.println(" ");
		}
*/

	}
}
