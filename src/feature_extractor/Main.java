package feature_extractor;

import java.io.IOException;
import java.util.List;

import simulator.MLMSimulator;

public class Main {
	
	private static final String NON_BITE_FILE_EXTRACT_LIST 
							= "C:\\temp\\bite_counting\\manually_extract_features_non-bites_2.txt";
	private static final String BITE_FILE_EXTRACT_LIST 
							= "C:\\temp\\bite_counting\\manually_extracted_features2.txt";
	private static final String NON_BITE_DATA_FOLDER_PATH
							= "C:\\temp\\bite_counting\\non-bites\\";
	private static final String BITE_DATA_FOLDER_PATH
							= "C:\\temp\\bite_counting\\bites\\";
	private static final String ARFF_WRITE_TO = "C:\\temp\\bite_counting\\windowed_data_new.arff";
	
	public static final boolean RANGE_IS_DEFAULT = false;
	public static final int RANGE = 20;

	public static void main(String[] args) {
		List<String> instances;
		try {
			instances = WindowToFeature.createInstances(
								WindowExtractor.extractFeatures(NON_BITE_FILE_EXTRACT_LIST,
					 											NON_BITE_DATA_FOLDER_PATH)
					 									);
			instances.addAll(
					WindowToFeature.createInstances(
								WindowExtractor.extractFeatures(BITE_FILE_EXTRACT_LIST,
					 											BITE_DATA_FOLDER_PATH)
					 								)
					 		);
			ArffWriter.createArff(instances, ARFF_WRITE_TO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MLMSimulator simulator = new MLMSimulator();
		simulator.train();
		try {
			simulator.test();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem testing! Error thrown up to main.");
		}
	}

}
