import java.util.*;
import java.io.*;

public class LogisticRegression
{
	public static double[] w;
	public static double[][] data;					// data 
	public static double[] label;					// labels
	public static int K;						// feature dimension
	public static int N;						// num of data instances
	public static final int MAX_ITER = 100000;
	public static final int MAX_FOLD = 10;
	public static final boolean WRITE = false;

//	public static final double lr = 0.000000005;			// spambase, breast cancer
	public static final double lr = 0.000000001;			// diabete
//	public static final double epsilon = 0.001;			// spambase, brease cancer
	public static final double epsilon = 0.0001;			// diabete


	public static void
	init(String[] args) {
		String fileDir = args[0];
		N = FileParser.readNumOfInstances(fileDir, ",");
		K = FileParser.readNumOfFeatures(fileDir, ",");

		data = new double[N][K+1];
		label = new double[N];
		w = new double[K+1];
		FileParser.readData(fileDir, ",", data, label);


	}

	// logistic function
	public static double
	logis(double x) {
		if (x > 100) 
			return 1;
		else
			return Math.exp(x) / (1 + Math.exp(x));
	}

	// provide the result of probability of class 1
	public static double[]
	predict() {
		double[] res = new double[N];
		for (int i = 0; i < N; i++) {
			double v = 0;
			for (int k = 0; k < K+1; k++) 
				v += w[k] * data[i][k];
			res[i] = logis(v);
		}

		return res;
	}

	// calculate the objective function (log-likelihood)
	public static double
	calcObj(int fold) {
		double res = 0;
		double[] sigma = new double[N];
		sigma = predict();
		for (int i = 0; i < N; i++) {
			if (i%MAX_FOLD != fold) {
				if (label[i] == 1) 
					res += Math.log(sigma[i] + Double.MIN_VALUE);
				else if (label[i] == 0) 
					res += Math.log(1 - sigma[i] + Double.MIN_VALUE);
			}
		}

		return res;
	}

	public static void
	train(int fold) {
		System.out.println("==============================");
		System.out.println("Training fold " + fold);
		double oldObj = 0, newObj;
		for (int iter = 0; iter < MAX_ITER; iter++) {
			double[] grad = new double[K+1];
			// calculate gradient
			double[] sigma = new double[N];
			sigma = predict();
			for (int i = 0; i < N; i++) {
				if (i%MAX_FOLD != fold) {
					if (label[i] == 1) {
						for (int k = 0; k < K+1; k++) 
							grad[k] += (1-sigma[i]) * data[i][k];
					}
					else if (label[i] == 0) {
						for (int k = 0; k < K+1; k++) 
							grad[k] -= sigma[i] * data[i][k];
					}
				}
			}
			// update
			for (int k = 0; k < K+1; k++) {
				w[k] += lr * grad[k];
			}
			// check objective
			newObj = calcObj(fold);
			if (WRITE) {
				try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./convergenceCurve", true)))) {
					writer.printf("%f\n", newObj);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (iter%10000 == 0) 
				System.out.println("\tIter " + iter + " Objective function = " + newObj);
			if (newObj - oldObj < epsilon && iter != 0) {
				break;
			}
			oldObj = newObj;
		}

		return;
	}

	public static void
	test(int fold) {
		double[] sigma = new double[N];
		sigma = predict();
		int cor = 0, tot = 0;
		for (int i = 0; i < N; i++) {
			if (i%MAX_FOLD == fold) {			// testing
//			if (i%MAX_FOLD != fold) {			// training
				if ((label[i]-0.5) * (sigma[i]-0.5) > 0) 
					cor += 1;
				tot += 1;
			}
		}
		System.out.println("Correct = " + cor + " Total = " + tot + " Accuracy = " + (double)cor/tot);

		return;
	}

	public static void 
	main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java LogisticRegression ../../data/spambase.csv");
			System.exit(0);
		}

		init(args);

		for (int fold = 0; fold < MAX_FOLD; fold++) {
//			if (fold != 0) break;
			train(fold);
			test(fold);
			
		}
	}
}
