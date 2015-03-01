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
		double[] weights
//		double[] res
	) {
		double trRMSE = 0, teRMSE = 0;

		System.out.println("Training");
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
		System.out.println("\tRMSE = " + trRMSE);

		System.out.println("Testing");
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
		System.out.println("\tRMSE = " + teRMSE);

//		res[0] = trRMSE; res[1] = teRMSE;
		return;
	}
}


