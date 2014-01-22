package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *	FOR CONVERTING BITE FILES TO CSV 
 */
public class GraphingMain {
	
	public static String FILE_OUT = "C:\\temp\\bite_counting\\csv\\";

	public static void main(String[] args) throws IOException {
		String pathBites = "C:\\temp\\bite_counting\\bites";
		String pathNonBites = "C:\\temp\\bite_counting\\non-bites";
		File bitesFolder = new File(pathBites);
		File nonBitesFolder = new File(pathNonBites);
		
		BufferedReader br = null;
		// For each file...
		int g = 1;
		for(File file : bitesFolder.listFiles()) {
			List<Double> xAxis = new ArrayList<Double>();
			List<Double> yAxis = new ArrayList<Double>();
			List<Double> zAxis = new ArrayList<Double>();
			List<Double> deltaTime = new ArrayList<Double>();
			List<Double> elapsedTime = new ArrayList<Double>();
			List<String> labels = new ArrayList<String>();
			List<Double> xJerk = new ArrayList<Double>();
			List<Double> yJerk = new ArrayList<Double>();
			List<Double> zJerk = new ArrayList<Double>();
			br = new BufferedReader(new FileReader(file));
			String line;
			int i = 0;
			// For each line in the file...
			while ((line = br.readLine()) != null) {
				String[] data = line.split(", ");
				String label = data[5];
				if (label.equals("Bite")) {
					double xAccel = Double.parseDouble(data[0]);
					xAxis.add(xAccel);
					double yAccel = Double.parseDouble(data[1]);
					yAxis.add(yAccel);
					double zAccel = Double.parseDouble(data[2]); 
					zAxis.add(zAccel);
					
					double changeInTime = Double.parseDouble(data[3]); 
					deltaTime.add(changeInTime);
					elapsedTime.add(Double.parseDouble(data[4]));
					
					labels.add(data[5]);
					
					double xJ, yJ, zJ;
					if (i == 0) {
						xJ = xAccel / changeInTime;
						yJ = yAccel / changeInTime;
						zJ = zAccel / changeInTime;
					}
					else {
						xJ = (xAccel - xAxis.get(i - 1)) / changeInTime;
						yJ = (yAccel - yAxis.get(i - 1)) / changeInTime;
						zJ = (zAccel - zAxis.get(i - 1)) / changeInTime;
					}
					xJerk.add(xJ);
					yJerk.add(yJ);
					zJerk.add(zJ);
					i++;
				}
			}
			// Only getting the standard deviation for each file (i.e. each person)
			double xMean = 0;
			for (Double x : xAxis)
				xMean += x;
			xMean = xMean / xAxis.size();
			double xVariance = 0;
			for (Double x : xAxis)
				xVariance += Math.pow(x - xMean, 2);
			xVariance = xVariance / xAxis.size();
			double xStandardDev = Math.sqrt(xVariance);
			
			double yMean = 0;
			for (Double y : yAxis)
				yMean += y;
			yMean = yMean / yAxis.size();
			double yVariance = 0;
			for (Double y : yAxis)
				yVariance += Math.pow(y - yMean, 2);
			yVariance = yVariance / yAxis.size();
			double yStandardDev = Math.sqrt(yVariance);
			
			double zMean = 0;
			for (Double z : zAxis)
				zMean += z;
			zMean = zMean / zAxis.size();
			double zVariance = 0;
			for (Double z : zAxis)
				zVariance += Math.pow(z - zMean, 2);
			zVariance = zVariance / zAxis.size();
			double zStandardDev = Math.sqrt(zVariance);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_OUT+"_bite_"+g+".csv", true));
			g++;
			bw.write("xAxis,yAxis,zAxis,xStandardDev,yStandardDec,zStandardDev,xJerk,yJerk,zJerk,xMean,yMean,zMean,elapsedTime,label\n");
			bw.flush();
			for (int k = 0; k < xAxis.size(); k++) {
				bw.write(xAxis.get(k) + "," + yAxis.get(k) + "," + zAxis.get(k) + "," + 
						 xStandardDev + "," + yStandardDev + "," + zStandardDev + "," +
						 xJerk.get(k) + "," + yJerk.get(k) + "," + zJerk.get(k) + "," +
						 xMean + "," + yMean + "," + zMean + "," +
						 elapsedTime.get(k) + "," + labels.get(k) + "\n");
				bw.flush();
			}
			bw.close();
		}
		
		// Non-bites
		g = 1;
		for (File file : nonBitesFolder.listFiles()) {
			List<Double> xAxis = new ArrayList<Double>();
			List<Double> yAxis = new ArrayList<Double>();
			List<Double> zAxis = new ArrayList<Double>();
			List<Double> deltaTime = new ArrayList<Double>();
			List<Double> elapsedTime = new ArrayList<Double>();
			List<String> labels = new ArrayList<String>();
			List<Double> xJerk = new ArrayList<Double>();
			List<Double> yJerk = new ArrayList<Double>();
			List<Double> zJerk = new ArrayList<Double>();
			br = new BufferedReader(new FileReader(file));
			String line;
			int i = 0;
			// For each line in the file...
			while ((line = br.readLine()) != null) {
				String[] data = line.split(", ");
				String label = data[5];
				if (label.equals("Non-Bite")) {
					double xAccel = Double.parseDouble(data[0]);
					xAxis.add(xAccel);
					double yAccel = Double.parseDouble(data[1]);
					yAxis.add(yAccel);
					double zAccel = Double.parseDouble(data[2]); 
					zAxis.add(zAccel);
					
					double changeInTime = Double.parseDouble(data[3]); 
					deltaTime.add(changeInTime);
					elapsedTime.add(Double.parseDouble(data[4]));
					
					labels.add(data[5]);
					
					double xJ, yJ, zJ;
					if (i == 0) {
						xJ = xAccel / changeInTime;
						yJ = yAccel / changeInTime;
						zJ = zAccel / changeInTime;
					}
					else {
						xJ = (xAccel - xAxis.get(i - 1)) / changeInTime;
						yJ = (yAccel - yAxis.get(i - 1)) / changeInTime;
						zJ = (zAccel - zAxis.get(i - 1)) / changeInTime;
					}
					xJerk.add(xJ);
					yJerk.add(yJ);
					zJerk.add(zJ);
					i++;
				}
			}
			// Only getting the standard deviation for each file (i.e. each person)
			double xMean = 0;
			for (Double x : xAxis)
				xMean += x;
			xMean = xMean / xAxis.size();
			double xVariance = 0;
			for (Double x : xAxis)
				xVariance += Math.pow(x - xMean, 2);
			xVariance = xVariance / xAxis.size();
			double xStandardDev = Math.sqrt(xVariance);
			
			double yMean = 0;
			for (Double y : yAxis)
				yMean += y;
			yMean = yMean / yAxis.size();
			double yVariance = 0;
			for (Double y : yAxis)
				yVariance += Math.pow(y - yMean, 2);
			yVariance = yVariance / yAxis.size();
			double yStandardDev = Math.sqrt(yVariance);
			
			double zMean = 0;
			for (Double z : zAxis)
				zMean += z;
			zMean = zMean / zAxis.size();
			double zVariance = 0;
			for (Double z : zAxis)
				zVariance += Math.pow(z - zMean, 2);
			zVariance = zVariance / zAxis.size();
			double zStandardDev = Math.sqrt(zVariance);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_OUT+"_nonbite_"+g+".csv", true));
			g++;
			for (int k = 0; k < xAxis.size(); k++) {
				bw.write(xAxis.get(k) + "," + yAxis.get(k) + "," + zAxis.get(k) + "," + 
						 xStandardDev + "," + yStandardDev + "," + zStandardDev + "," +
						 xJerk.get(k) + "," + yJerk.get(k) + "," + zJerk.get(k) + "," +
						 xMean + "," + yMean + "," + zMean + "," +
						 elapsedTime.get(k) + "," + labels.get(k) + "\n");
				bw.flush();
			}
			bw.close();
		}
	}

}
