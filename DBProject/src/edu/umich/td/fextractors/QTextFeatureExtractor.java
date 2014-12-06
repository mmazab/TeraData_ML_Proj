package edu.umich.td.fextractors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.umich.td.database.StatsCollector;
import edu.umich.td.workload.Query;

public class QTextFeatureExtractor {
	String path;
	String tmpFileName = "files/tmp.feat";

	// read query text features vector from file
	ArrayList<Feature> ReadFeatFromFile() {
		String strLine;
		ArrayList<Feature> featuresList = new ArrayList<Feature>();
		try {
			FileInputStream fstream = new FileInputStream(tmpFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if (!strLine.isEmpty()) {
					String[] features = strLine.trim().split("\t");
					for (int i = 0; i < features.length; i++){
						Feature feature = new Feature("f_"+i, features[i], FeatureCategory.QUERYTEXT);
						featuresList.add(feature);
					}
				}//end if
			}//end while
			in.close();
			return featuresList;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	} //end ReadFeatFromFile

	// Run query text feature extractor script
	public ArrayList<Feature> ExtractFeatures(Query query) {
		try {
			path = new File("").getAbsolutePath();
			WriteQueryToFile(path + "/files/tmp.sql", query.qText, false);
			String command = "cd " + path + "/files; python fextractor.py";
			Process p;
			p = Runtime.getRuntime().exec("bash");
			PrintWriter pw = new PrintWriter(p.getOutputStream());
			pw.print(command);
			pw.close();
			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedReader ms = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String ln = "";

			while ((ln = error.readLine()) != null)
				System.err.println(ln);

			while ((ln = ms.readLine()) != null)
				System.out.println(ln);
			System.out.print("Extracting features ... ");
			p.waitFor();

			System.out.println("Done");
			ArrayList<Feature> features = ReadFeatFromFile();

			ArrayList<Feature> tableCountsFeatures = ExtractTextFeatures(query, "");
			//features.addAll(tableCountsFeatures);

			return features;
		} catch (Exception e) {
			return null;
		}
	}

	// These text features
	public static ArrayList<Feature> ExtractTextFeatures(Query query, String dbName) {
		if (dbName.isEmpty())
			dbName = "TPCH";
		ArrayList<Feature> feature = new ArrayList<Feature>();
		Iterator<Entry<String, Integer>> tableItr = StatsCollector.tables.entrySet().iterator();
		while (tableItr.hasNext()) {
			Map.Entry<String, Integer> tableCount = (Map.Entry<String, Integer>) tableItr.next();
			String tableName = tableCount.getKey();
			Integer rowCount = tableCount.getValue();
			if (query.qText.contains(tableName))
				feature.add(new Feature("t_"+tableName ,"1", FeatureCategory.QUERYTEXT));
			else
				feature.add(new Feature("t_"+tableName ,"0", FeatureCategory.QUERYTEXT));
			feature.add(new Feature(tableName+"_cnt", rowCount.toString(), FeatureCategory.QUERYTEXT));
		}
		return feature;
	}

	// Write queries to a file to be sent to the python script
	public static void WriteQueryToFile(String fileName, String text, boolean append) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName), append));
			bw.write(text);
			bw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
