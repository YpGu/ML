/**
	Evaluation.java: RMSE calculator
**/

import java.util.*;

public class Evaluation
{
	// calculate RMSE
	public static void 
	evaluate(
		double[][] trainData,
		double[][] trainLabels,
		double[][] testData,
		double[][] testLabels,
		double[] weights,
		ArrayList<Double> tr,
		ArrayList<Double> te,
		boolean display
	) {
		double trRMSE = 0, teRMSE = 0;

		for (int i = 0; i < trainData.length; i++) {
			double ei = trainLabels[i][0];
			for (int j = 0; j < trainData[0].length; j++) {
				ei -= weights[j] * trainData[i][j];
			}
			ei -= weights[trainData[0].length];
			trRMSE += (ei*ei);
		}
		trRMSE /= trainData.length;
		trRMSE = Math.sqrt(trRMSE);
		if (display) {
			System.out.println("Training");
			System.out.println("\tRMSE = " + trRMSE);
		}
		tr.add(trRMSE);

		for (int i = 0; i < testData.length; i++) {
			double ei = testLabels[i][0];
			for (int j = 0; j < testData[0].length; j++) {
				ei -= weights[j] * testData[i][j];
			}
			ei -= weights[testData[0].length];
			teRMSE += (ei*ei);
		}
		teRMSE /= testData.length;
		teRMSE = Math.sqrt(teRMSE);
		if (display) {
			System.out.println("Testing");
			System.out.println("\tRMSE = " + teRMSE);
		}
		te.add(teRMSE);

		return;
	}
}


