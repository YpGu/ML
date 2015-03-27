import java.util.*;

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
	getArgMax(Map<Integer, Double> arr) {
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
		// smoothing 
		for (int k = 0; k < K; k++) 
			for (int v = 0; v < V; v++) 
				theta[k][v] = (theta[k][v] + 1) / (double)(numDocumentsInClass[k] + V);

		return;
	}


	public static void
	test() {
		System.out.println("Testing...");
		Map<String, Integer> labelSet = testData.getLabelSet();
		for (String doc: testData.getDict()) {
			Map<Integer, Double> probs = new HashMap<Integer, Double>();		// log of posterior probability for each topic
			for (int k = 0; k < K; k++) {
				double prob = 0;
				for (String w: testData.getRow(doc)) {
					int v = Integer.parseInt(w)-1;
					prob += Math.log(theta[k][v] + Double.MIN_VALUE);
				}
				for (String w: testData.getRowComplement(doc)) {
					int v = Integer.parseInt(w);
					prob += Math.log(1 - theta[k][v] + Double.MIN_VALUE);
				}
				prob += Math.log(classPrior[k]);
				probs.put(k, prob);
			}
			for (Map.Entry<Integer, Double> e: probs.entrySet()) {
				System.out.println("prob = " + e.getValue());			// TODO: infinity
			}
			Scanner sc = new Scanner(System.in);
			int gu = sc.nextInt();

			int k = getArgMax(probs);
			// check correctness  
			if (k == labelSet.get(doc)) {
				System.out.println("Correct!");
			}
			else {
				System.out.println("Wrong! Predicted Class = " + k + ", Ground Truth = " + labelSet.get(doc));
			}
		}
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
