import java.util.*;
import java.io.*;

public class NaiveBayes
{
	public static SparseMatrix trainData, testData;
	public static double[][] theta;						// K * V matrix 
	public static int V;							// vocabulary size 
	public static int K;							// label size 
	public static double[] classPrior;					// prior for each class 


	public static void
	init(String[] args) {
		trainData = new SparseMatrix();
		testData = new SparseMatrix();

		String trainDir = args[0] + "train.data", trainLabelDir = args[0] + "train.label";
		System.out.println("Reading training data from: " + trainDir + " and " + trainLabelDir);
		FileParser.ReadData(trainData, trainDir);
		K = FileParser.ReadLabel(trainData, trainLabelDir);
		V = FileParser.ReadVocabularySize(args[0] + "vocabulary.txt");

		String testDir = args[0] + "test.data", testLabelDir = args[0] + "test.label";
		System.out.println("Reading testing data from: " + testDir + " and " + testLabelDir);
		FileParser.ReadData(testData, testDir);
		FileParser.ReadLabel(testData, testLabelDir);

		System.out.println("Number of Topics = " + K);
		System.out.println("Number of Terms = " + V);
		theta = new double[K][V];
		classPrior = new double[K];
	}


	public static int
	getArgMax2(Map<Integer, Double> arr) {
		int s = arr.size();
		int maxI = -1;
		double maxV = -Double.MAX_VALUE;
		for (Map.Entry<Integer, Double> e: arr.entrySet()) {
			if (e.getValue() > maxV) {
				maxV = e.getValue();
				maxI = e.getKey();
			}
		}

		return maxI;
	}

	public static boolean
	greaterThan(double p1, double n1, double p2, double n2) {
		if (n1 > n2) return true;
		else if (n1 < n2) return false;
		else return (p1 > p2);
	}

	public static int
	getArgMax(String doc) {
		int maxI = -1;
		double maxRatio = 0;
		double maxUp = -Double.MAX_VALUE;

		for (int k = 1; k < K; k++) {
			double prob = 1;
			double numUp = 0, numDown = 0;
			for (String w: testData.getRow(doc)) {
				int v = Integer.parseInt(w)-1;
				prob *= (theta[k][v]/theta[0][v]);
				while (prob >= 10) {prob *= 0.1; numUp += 1;}
				while (prob < 1) {prob *= 10; numDown += 1;}
			}
			for (String w: testData.getRowComplement(doc)) {
				int v = Integer.parseInt(w)-1;
				prob *= ((1-theta[k][v])/(1-theta[0][v]));
				while (prob >= 10) {prob *= 0.1; numUp += 1;}
				while (prob < 1) {prob *= 10; numDown += 1;}
			}
			prob *= (classPrior[k]/classPrior[0]);

			while (prob >= 10) {prob *= 0.1; numUp += 1;}
			while (prob < 1) {prob *= 10; numDown += 1;}

//			System.out.println("Ratio = " + prob + " numUp = " + numUp + " numDown = " + numDown);
			if (greaterThan(prob, numUp-numDown, maxRatio, maxUp)) {
				maxRatio = prob; maxI = k; maxUp = numUp-numDown;
			}
		}
//		Scanner sc = new Scanner(System.in);
//		int gu = sc.nextInt();

		return maxI;
	}


	public static void
	train() {
		System.out.println("Training...");
		Map<String, Integer> labelSet = trainData.getLabelSet();
		// binary
		int[] numDocumentsInClass = new int[K];
		for (String doc: trainData.getDict()) {
			int k = labelSet.get(doc);
			for (String w: trainData.getRow(doc)) {
				int v = Integer.parseInt(w)-1;
				theta[k][v] += 1;
			}
			numDocumentsInClass[k] += 1;
		}
		// class prior
		for (int k = 0; k < K; k++) {
			classPrior[k] = (double)numDocumentsInClass[k]/(double)trainData.getDictSize();
		//	System.out.println("k = " + k + " prior = " + classPrior[k]);
		}
		// smoothing 
		for (int k = 0; k < K; k++) 
			for (int v = 0; v < V; v++) 
				theta[k][v] = (theta[k][v] + 1) / (double)(numDocumentsInClass[k] + V);

		/// output 
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")))) {
			for (int v = 0; v < V; v++) {
				for (int k = 0; k < K; k++) {
					writer.printf("%f\t", theta[k][v]);
				}
				writer.printf("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}


	public static void
	test() {
		System.out.println("Testing...");
		Map<String, Integer> labelSet = testData.getLabelSet();
		int cor = 0, tot = 0;
		for (String doc: testData.getDict()) {
/*
			Map<Integer, Double> probs = new HashMap<Integer, Double>();		// log of posterior probability for each topic
			for (int k = 0; k < K; k++) {
				double prob = 1;
				for (String w: testData.getRow(doc)) {
					int v = Integer.parseInt(w)-1;
					prob *= theta[k][v];
				}
				for (String w: testData.getRowComplement(doc)) {
					int v = Integer.parseInt(w)-1;
					prob *= (1-theta[k][v]);
				}
				prob *= classPrior[k];
				probs.put(k, prob);
				System.out.println("Prob = " + prob);
			}
			Scanner sc = new Scanner(System.in);
			int gu = sc.nextInt();
			int k = getArgMax2(probs);
*/
			int k = getArgMax(doc);

			// check correctness  
			if (k == labelSet.get(doc)) {
			//	System.out.println("Correct! (" + k + ")");
				cor += 1; tot += 1;
			}
			else {
			//	System.out.println("Wrong! Predicted Class = " + k + ", Ground Truth = " + labelSet.get(doc));
				tot += 1;
			}
		}
		System.out.println("Correct = " + cor + " Total = " + tot);
	}

	public static void
	main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java NaiveBayes ../../data/20news/");
			System.exit(0);
		}

		init(args);

		train();
		test();
	}
}
