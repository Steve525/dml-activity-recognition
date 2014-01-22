package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import feature_extractor.Main;
import feature_extractor.WindowToFeature;

public class MLMSimulator {

	private static final String TRAINING_DATA = "C:\\temp\\bite_counting\\windowed_data_new.arff";
	
//	private static final String TRAINING_DATA_EXPERIMENT = "C:\\temp\\bite_counting\\non-bites\\";
	
	private static final String TEST_DATA_PATH = "C:\\temp\\bite_counting\\bites\\";
	
	private static final String[] TEST_DATA = {"1.txt", "2.txt", "3.txt",
											   "4.txt", "5.txt", "6.txt",
											   "7.txt", "8.txt", "9.txt",
											   "10.txt", "11.txt", "12.txt", "13.txt"};
	
	private static final int NUM_ATTRIBUTES = 9;	// number of attributes not counting the output class
	
	Instances _trainingData;
	List<Instance> _testData;
	MultilayerPerceptron _mlp;
	
	public MLMSimulator() {
		// Training data not initialized until training method is called
		_trainingData = null;
		_testData = new ArrayList<Instance>();
		_mlp = new MultilayerPerceptron();
	}
	
	// Train the model
	public void train() {
		// Train the model...
		try {
			// Read the training data from an ARFF file
			_trainingData = new Instances(new BufferedReader(new FileReader(new File(TRAINING_DATA))));
			_trainingData.setClassIndex(NUM_ATTRIBUTES);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem with reading in the instances...");
		} 
		try {
			_mlp.buildClassifier(_trainingData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem building the classifier...");
		}
	}
	
	// Calculate test data
	public void test() throws IOException {
		for (int j = 0; j < TEST_DATA.length; j++) {
			_testData.clear();
			String testDataPath = TEST_DATA_PATH + TEST_DATA[j];
			List<String> lines = Files.readAllLines(Paths.get(testDataPath), Charset.defaultCharset());
			List<List<String>> windows = new ArrayList<List<String>>();
			for (int i = 0; i < lines.size() - Main.RANGE; i++) {
				windows.add(lines.subList(i, i + Main.RANGE));
			}
			List<String> instances = WindowToFeature.createInstances(windows);
			for (int i = 0; i < instances.size(); i++) {
				String[] values = instances.get(i).split(",");
				Instance inst = new Instance(NUM_ATTRIBUTES);
				inst.setDataset(_trainingData);
				inst.setValue(0, Double.parseDouble(values[0]));
				inst.setValue(1, Double.parseDouble(values[1]));
				inst.setValue(2, Double.parseDouble(values[2]));
				inst.setValue(3, Double.parseDouble(values[3]));
				inst.setValue(4, Double.parseDouble(values[4]));
				inst.setValue(5, Double.parseDouble(values[5]));
				inst.setValue(6, Double.parseDouble(values[6]));
				inst.setValue(7, Double.parseDouble(values[7]));
				inst.setValue(8, Double.parseDouble(values[8]));
				_testData.add(inst);
			}
			int bites = 0;
			for (Instance inst : _testData) {
				try {
					if (_mlp.classifyInstance(inst) == 0)
						bites++;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Something wrong with model classifying an instance.");
				}
			}
			System.out.println(testDataPath);
			System.out.println("Bites predicted: " + bites);
			System.out.println("============================");
		}
	}
}