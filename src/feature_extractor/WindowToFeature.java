package feature_extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WindowToFeature {
	
	private static final int NUMBER_OF_AXIS = 3;

	public WindowToFeature() {}
	
	/**
	 * 
	 * @param windowSamples		This is the list of windows
	 * @return Actual instances used in the final ARFF
	 * @throws IOException
	 */
	public static List<String> createInstances(List<List<String>> windowSamples) throws IOException {
		List<String> finalInstances = new ArrayList<String>();
		// For each window...
		for(List<String> window : windowSamples) {
			double[][] windowMatrix = new double[window.size()][5];
			String label = "";
			int i = 0;
			// For each sample in the window...
			for (String sample : window) {
				String[] data = sample.split(", ");
				windowMatrix[i][0] = Double.parseDouble(data[0]);
				windowMatrix[i][1] = Double.parseDouble(data[1]);
				windowMatrix[i][2] = Double.parseDouble(data[2]);
				windowMatrix[i][3] = Double.parseDouble(data[3]);
				windowMatrix[i][4] = Double.parseDouble(data[4]);
				label = data[5];
				i++;
			}
			// Only getting the mean and standard deviation for each window
			double[] mean = getMeanForWindow(windowMatrix);
			double[] stdDev = getStandardDeviationForWindow(windowMatrix, mean);
			double[] jerkMean = getJerkForceMean(windowMatrix);
			finalInstances.add(stdDev[0] + "," + stdDev[1] + "," + stdDev[2] + "," +
							   mean[0] + "," + mean[1] + "," + mean[2] + "," +
							   jerkMean[0] + "," + jerkMean[1] + "," + jerkMean[2] + "," +
							   label);
		}
		return finalInstances;
	}
	
	/**
	 * 
	 * @param window
	 * @return	an array of 3 doubles corresponding to x, y and z mean respectively.
	 */
	public static double[] getMeanForWindow(double[][] window) {
		double[] mean = new double[3];
		for (int i = 0; i < window.length; i++) {
			double[] row = window[i];
			for (int j = 0; j < NUMBER_OF_AXIS; j++) {
				mean[j] += row[j];
			}
		}
		mean[0] /= window.length;
		mean[1] /= window.length;
		mean[2] /= window.length;
		return mean;
	}
	
	/**
	 * 
	 * @param window
	 * @param mean		The mean for the x, y and z axis respectively
	 * @return			An array of size 3 containing the standard deviation for the 3 axis
	 */
	public static double[] getStandardDeviationForWindow(double[][] window, double[] mean) {
		double[] stdDev = new double[3];
		for (int i = 0; i < window.length; i++) {
			double[] row = window[i];
			for (int j = 0; j < NUMBER_OF_AXIS; j++) {
				stdDev[j] += Math.pow(row[j] - mean[j], 2);
			}
		}
		stdDev[0] = Math.sqrt(stdDev[0] / window.length);
		stdDev[1] = Math.sqrt(stdDev[1] / window.length);
		stdDev[2] = Math.sqrt(stdDev[2] / window.length);
		return stdDev;
	}
	
	public static double[] getJerkForceMean(double[][] window) {
		double[] jerk = new double[3];
		for (int i = 1; i < window.length; i++) {
			for (int j = 0; j < NUMBER_OF_AXIS; j++) {
				jerk[j] += (window[i][j] - window[i-1][j]) / window[i][3];
			}
		}
		jerk[0] /= window.length;
		jerk[1] /= window.length;
		jerk[2] /= window.length;
		return jerk;
	}
	
}
