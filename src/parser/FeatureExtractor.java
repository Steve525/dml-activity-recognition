package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *	FOR EXTRACTING APPROPRIATE INTERVALS OF ACTIVITY/NON-ACTIVITY 
 */
public class FeatureExtractor {

	public static final String READ_FROM = "C:\\temp\\bite_counting\\bites\\";
	public static final String WRITE_TO = "C:\\temp\\bite_counting\\feature-extraction\\";
	public static List<String> linesFromFile = new ArrayList<String>();
	public static final int WINDOW = 70;
	public static final double THRESHHOLD = 1.4;
	
	public static void main(String[] args) throws IOException {
		File readFolder = new File(READ_FROM);
		int g = 0;
		for (File file : readFolder.listFiles()) {
			g++;
			linesFromFile.clear();
			List<Double> zValues = extractZValues(file);
			List<Integer> interesting = new ArrayList<Integer>();
			extractInterestingInstances(zValues, interesting);
			writeOutput(interesting, g);
		}
	}

	private static void extractInterestingInstances(List<Double> zValues,
			List<Integer> interesting) throws IOException {
		Map<Double, Integer> stdToZValues = new HashMap<Double, Integer>();
		double s = 0;
		for (int start = 0; start < zValues.size(); start++) {
			if (start+WINDOW >= zValues.size()) {
				s = getStandardDeviation(zValues.subList(start, zValues.size()-1));
				if (s > THRESHHOLD)
					stdToZValues.put(s, start);
				break;
			}
			List<Double> sub = zValues.subList(start, start+WINDOW);
			s = getStandardDeviation(sub);
			if (s > THRESHHOLD) {
				stdToZValues.put(s, start);
			}
		}
		SortedSet<Double> standardDeviations = new TreeSet<Double>(stdToZValues.keySet());
		SortedSet<Double> stds1 = standardDeviations.subSet(1.5, 2.0);
		SortedSet<Double> stds2 = standardDeviations.subSet(2.0, 3.0);
		SortedSet<Double> stds3 = standardDeviations.tailSet(3.0);
		int i = 0;
		while (i < 7) {
			i++;
			if (stds1.isEmpty())
				continue;
			Double x = stds1.last();
			interesting.add(stdToZValues.get(x));
			stds1.remove(x);
			if (stds2.isEmpty())
				continue;
			x = stds2.last();
			interesting.add(stdToZValues.get(x));
			stds2.remove(x);
			if (stds3.isEmpty())
				continue;
			x = stds3.last();
			interesting.add(stdToZValues.get(x));
			stds3.remove(x);
		}
	}
	
	public static void writeOutput(List<Integer> startingPoints, int g) throws IOException {
		File fileOut = new File(WRITE_TO + "" + g + "-extracted.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
		for (Integer start : startingPoints) {
			List<String> lines = linesFromFile.subList(start, start+WINDOW);
			for (String line : lines) {
				bw.write(line + "\n");
				bw.flush();
			}
			bw.write("-20\n");
			bw.flush();
		}
		bw.close();
	}
	
	public static List<Double> extractZValues(File fileIn) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileIn));
		String line = "";
		List<Double> zValues = new ArrayList<Double>();
		while ((line = br.readLine()) != null) {
			linesFromFile.add(line);
			String[] values = line.split(", ");
			zValues.add(Double.parseDouble(values[2]));
		}
		br.close();
		return zValues;
	}
	
	public static double getStandardDeviation(List<Double> zValues) throws IOException {
		double mean = 0;
		for (double z : zValues)
			mean += z;
		mean /= zValues.size();
		double s = 0;
		for (double z : zValues)
			s += (z-mean) * (z-mean);
		s = Math.sqrt(s / (zValues.size()-1));
		return s;
	}

}
