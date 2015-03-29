import java.util.*;
import java.io.*;

public class NaiveBayes
{
	public static SparseMatrix trainData, testData;
	public static Map<Integer, Map<String, Double>> theta;			// K * V matrix
	public static int V;							// vocabulary size 
	public static int K;							// label size 
	public static double[] classPrior;					// prior for each class 
	public static Set<String> voc;						// vocabulary 
	public static Set<String> vocAll;					// vocabulary (all) 


	public static void
	init(String[] args) {
		trainData = new SparseMatrix();
		testData = new SparseMatrix();
		theta = new HashMap<Integer, Map<String, Double>>();
		voc = new HashSet<String>();

		V = Integer.parseInt(args[1]);
		String thresholdWordList = args[0] + "wordList/wordList" + args[1];
		if (V == 0) {
			thresholdWordList = args[0] + "fullVoc";
			V = FileParser.ReadVocabularySize(thresholdWordList);
		}
		voc = FileParser.ReadVocabulary(thresholdWordList);
		vocAll = FileParser.ReadVocabulary(args[0] + "fullVoc");

		String trainDir = args[0] + "train.data", trainLabelDir = args[0] + "train.label";
		System.out.println("Reading training data from: " + trainDir + " and " + trainLabelDir);
		FileParser.ReadData(trainData, trainDir, voc);
		K = FileParser.ReadLabel(trainData, trainLabelDir);

		String testDir = args[0] + "test.data", testLabelDir = args[0] + "test.label";
		System.out.println("Reading testing data from: " + testDir + " and " + testLabelDir);
		FileParser.ReadData(testData, testDir, voc);
		FileParser.ReadLabel(testData, testLabelDir);

		System.out.println("Number of Topics = " + K);
		System.out.println("Size of Vocabulary = " + V);
		System.out.println("Size of Vocabulary (all) = " + vocAll.size());

		for (int k = 0; k < K; k++) {
			Map<String, Double> m = new HashMap<String, Double>();
			theta.put(k, m);
		}
		classPrior = new double[K];
	}

	public static boolean
	greaterThan(double p1, double n1, double p2, double n2) {
		if (n1 > n2) return true;
		else if (n1 < n2) return false;
		else return (p1 > p2);
	}

	public static int
	getArgMax(SparseMatrix data, String doc, int option) {
		int maxI = 0;
		double maxRatio = 1;
		double maxUp = 0;

		for (int k = 1; k < K; k++) {
			double prob = 1;
			double numUp = 0, numDown = 0;

			for (String w: testData.getRow(doc)) {
				if (option == 1) {								// binary
					prob *= (theta.get(k).get(w)/theta.get(0).get(w));
					prob *= ((1-theta.get(0).get(w))/(1-theta.get(k).get(w)));
				}
				else if (option == 2) {								// multi event 
					prob *= Math.pow((theta.get(k).get(w)/theta.get(0).get(w)), data.getElement(doc, w));
				}
				while (prob >= 10) {prob *= 0.1; numUp += 1;}
				while (prob < 1) {prob *= 10; numDown += 1;}
			}

			for (String w: voc) {
//			for (String w: vocAll) {
				if (option == 1)
					prob *= ((1-theta.get(k).get(w))/(1-theta.get(0).get(w)));
				while (prob >= 10) {prob *= 0.1; numUp += 1;}
				while (prob < 1) {prob *= 10; numDown += 1;}
			}

			prob *= (classPrior[k]/classPrior[0]);

			while (prob >= 10) {prob *= 0.1; numUp += 1;}
			while (prob < 1) {prob *= 10; numDown += 1;}

			if (greaterThan(prob, numUp-numDown, maxRatio, maxUp)) {
				maxRatio = prob; maxI = k; maxUp = numUp-numDown;
			}
		}

		return maxI;
	}


	// option = 1: binary; 2: multinomial 
	public static void
	train(int option) {
		System.out.println("Training...");
		Map<String, Integer> labelSet = trainData.getLabelSet();
		// freqency
		int[] numInClass = new int[K];
		for (String doc: trainData.getDict()) {
			int k = labelSet.get(doc);
			for (String w: trainData.getRow(doc)) {
				try {
					double freq = theta.get(k).get(w);
					if (option == 1) 
						theta.get(k).put(w, freq + 1);					// binary
					else if (option == 2)
						theta.get(k).put(w, freq + trainData.getElement(doc, w));	// multi event
				}
				catch (java.lang.NullPointerException e) {
					theta.get(k).put(w, 1.0);
				}
				if (option == 2) 
					numInClass[k] += trainData.getElement(doc, w);				// multi event 
			}
			if (option == 1)
				numInClass[k] += 1;								// binary 
		}
		// class prior
		for (int k = 0; k < K; k++) {
			classPrior[k] = (double)numInClass[k]/(double)trainData.getDictSize();
		}
		// smoothing 
		double cc = 1;											// smoothing factor
		for (int k = 0; k < K; k++) {
			for (String w: voc) {
//			for (String w: vocAll) {
				try {
					double f = (theta.get(k).get(w) + cc) / (double)(numInClass[k] + V * cc);
					theta.get(k).put(w, f);
				}
				catch (java.lang.NullPointerException e) {
					double f = cc / (double)(numInClass[k] + V * cc);
					theta.get(k).put(w, f);
				}
			}
		}

		/// output 
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")))) {
			for (String w: voc) {
				for (int k = 0; k < K; k++) {
					writer.printf("%f\t", theta.get(k).get(w));
				}
				writer.printf("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}


	// option = 1: binary; 2: multinomial 
	public static void
	test(int option) {
		System.out.println("Testing...");
		Map<String, Integer> labelSet = testData.getLabelSet();
		int cor = 0, tot = 0;
		int docCount = 0;
		for (String doc: testData.getDict()) {
//			if (docCount%70 == 0) System.out.println("\t" + docCount);
			int k = getArgMax(testData, doc, option);
			if (k == labelSet.get(doc)) {
			//	System.out.println("Correct! (" + k + ")");
				cor += 1; tot += 1;
			}
			else {
			//	System.out.println("Wrong! Predicted Class = " + k + ", Ground Truth = " + labelSet.get(doc));
				tot += 1;
			}
			docCount += 1;
		}
		System.out.println("Correct = " + cor + " Total = " + tot + " Accuracy = " + (cor/(double)tot));
	}

	public static void
	main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java NaiveBayes <fileDir> <threshold (0 for no threshold)>");
			System.out.println("Example: java NaiveBayes ../../data/20news/ 500");
			System.exit(0);
		}

		init(args);

		train(1);
		test(1);
	}
}
