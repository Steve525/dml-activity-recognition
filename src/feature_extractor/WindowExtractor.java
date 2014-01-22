package feature_extractor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowExtractor {

	public WindowExtractor() {
	}
	
	/**
	 * 
	 * @return A list of window samples (sets of data instances that will later be reduced to one
	 * 			data instance for the ARFF file)
	 * @throws IOException
	 */
	public static List<List<String>> extractFeatures(String FILE_EXTRACT_LIST,
													 String DATA_FOLDER) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(FILE_EXTRACT_LIST), Charset.defaultCharset());
		Map<File, List<String>> filesToOpen = new HashMap<File, List<String>>();
		File currentFile = null;
		for (String line : lines) {
			if (line.equals("#")) {
				continue;
			}
			else if (line.matches(".*txt")) {
				currentFile = new File(line);
				filesToOpen.put(currentFile, new ArrayList<String>());
			}
			else
				filesToOpen.get(currentFile).add(line);
		}
		return extract(filesToOpen, DATA_FOLDER);
	}
	
	/** Uses a map of Files ---> (line ranges for extracting) to extract raw data sample ranges
	 * 
	 * @param filesToExtractFrom	
	 * @throws IOException
	 */
	private static List<List<String>> extract(Map<File, List<String>> filesToExtractFrom,
											  String DATA_FOLDER) throws IOException {
		List<List<String>> windows = new ArrayList<List<String>>();
		for (File f : filesToExtractFrom.keySet()) {
			String fullPath = DATA_FOLDER + f.getName();
			List<String> linesInFile = Files.readAllLines(Paths.get(fullPath), Charset.defaultCharset());
			List<String> ranges = filesToExtractFrom.get(f);
			for (String s : ranges) {
				String[] g = s.split("-");
				int start = Integer.parseInt(g[0]);
				int end = Integer.parseInt(g[1]);
				if (!Main.RANGE_IS_DEFAULT) {
					int shrink = (int)(((end - start) - Main.RANGE) / 2);
					if ((end - start) >= Main.RANGE) {
						end -= shrink;
						start += shrink;
					}
					else {
						end = (((end - start) / 2) + start) + (Main.RANGE / 2);
						start = end - Main.RANGE;
					}
					if (start < 0) 
						start = 0;
					if (end >= linesInFile.size())
						end = linesInFile.size()-1;
				}
//				start--;
//				end--;
				try {
					windows.add(linesInFile.subList(start, end));
				} catch (IndexOutOfBoundsException e) {
					System.out.println(start+", " +end+", "+fullPath);
					e.printStackTrace();
				}
			}
		}
		return windows;
	}
}
