package feature_extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ArffWriter {

	public ArffWriter() {
	}
	
	public static void createArff(List<String> instances,
								  String ARFF_WRITE_TO) 	throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ARFF_WRITE_TO)));
		writeHeader(bw);
		for (String instance : instances) {
			bw.write(instance + "\n");
			bw.flush();
		}
	}
	
	private static void writeHeader(BufferedWriter bw) {
		String[][] header = {{"@RELATION bites\n\n"},
				{"@ATTRIBUTE xStandardDev NUMERIC\n"},
				{"@ATTRIBUTE yStandardDev NUMERIC\n"},
				{"@ATTRIBUTE zStandardDev NUMERIC\n"},
				
				{"@ATTRIBUTE xMean NUMERIC\n"},
				{"@ATTRIBUTE yMean NUMERIC\n"},
				{"@ATTRIBUTE zMean NUMERIC\n"},
				
				{"@ATTRIBUTE xJerkMean NUMERIC\n"},
				{"@ATTRIBUTE yJerkMean NUMERIC\n"},
				{"@ATTRIBUTE zJerkMean NUMERIC\n"},
				
				{"@ATTRIBUTE class {Bite, Non-Bite}\n\n"},
				{"@DATA\n"}};
		for (int i = 0; i < header.length; i++) {
			try {
				bw.write(header[i][0]);
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
