/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * Controller.java
 */

package edu.umich.td.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import workload.QueriesPool;
import workload.Query;
import workload.WorkLoadGenerator;
import edu.umich.td.database.*;
import edu.umich.td.fextractors.Feature;
import edu.umich.td.fextractors.QTextFeatureExtractor;

public class Controller {

	public static void main(String[] args) {
		// 1- Load queries into the query pool from the files
		// 2- Run all the queries sequentially to detect each queries behavior
		// 3- Cluster queries into 4 categories
		// 4- Pass the different query pool to the workload creator
		// 5- Create different queries patch of different sizes
		// 6- Extract query/workload features
		// 7- Get query/workload XML execution plans
		// 8- Extract features from the execution plans
		// 9- Execute the returned list of queries from the WL creator
		// 10- Collect CPU and I/O usage

		QueriesPool qpool = new QueriesPool();
		StatsCollector.tables = StatsCollector.GetAllTablesAndCounts("TPCH");
		ArrayList<String> queriesFeaturesList = new ArrayList<String>();
		ArrayList<Feature> featuresList = new ArrayList<Feature>();

		ArrayList<Query> queries = qpool.ReadQueriesFromFile("files/queries.sql");
		qpool.ClusterQueries();

		int qTotalCount = qpool.TotalQueriesCount();

		WorkLoadGenerator workload = new WorkLoadGenerator();

		int samplesCount = 0;
		int batchSize = 0;

		qpool.ResetInclusionCount();
		Query.Max_COUNT = 6;
		samplesCount = 100;
		batchSize = 5;

		if (samplesCount * batchSize <= qTotalCount * Query.Max_COUNT) {
			workload.Generate(batchSize, samplesCount);
			qpool.ResetInclusionCount();
		} else {
			System.err
					.printf("The queries pool of size %d cannot generate %d samples with size %d using maximum repetition count of %d\n",
							qTotalCount, samplesCount, batchSize, Query.Max_COUNT);
			System.err.printf("Please increase the maximum repetition count to at least %d\n",
					(int) Math.ceil((double) ((samplesCount * batchSize) / qTotalCount)) + 1);
		}

		ArrayList<Integer> failedQueries = new ArrayList<Integer>();
		ArrayList<Long> qtimeStamps = new ArrayList<Long>();

		for (int i = 0; i < queries.size(); i++) {
			System.out.println("=========\tProcessing Query #" + i + "\t=========");
			QTextFeatureExtractor qtFeatExtractor = new QTextFeatureExtractor();
			featuresList = qtFeatExtractor.ExtractFeatures(queries.get(i));

			String query = queries.get(i).qText;
			long timeStamp = System.currentTimeMillis();
			query = query.replaceFirst("\n", "/*Q" + System.currentTimeMillis() + "*/\n");
			boolean status = TdDatabase.ExecuteQuery(query);

			// Query failed to execute properly
			if (!status) {
				failedQueries.add(i);
			} // Query is executed successfully
			else {
				// Add time stamp to the query so that we can retrieve it later
				qtimeStamps.add(timeStamp);
				String feat = "";
				for (Feature feature : featuresList) {
					System.out.print(feature.featureValue + "\t");
					feat += feature.featureValue + "\t";
				}
				queriesFeaturesList.add(feat);
			}
			System.out.println("\n");
		}

		for (int i = 0; i < failedQueries.size(); i++) {
			// System.out.println(instance);
			queries.remove(failedQueries.get(i));
		}

		// Initialize the CPU and I/O lists to collect the stats
		ArrayList<Integer> Cpu = new ArrayList<Integer>();
		ArrayList<Integer> IO = new ArrayList<Integer>();
		StringBuilder sbuildCPU = new StringBuilder();
		StringBuilder sbuildIO = new StringBuilder();

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < qtimeStamps.size(); i++) {
			int[] CpuIo = { 0, 0 };
			System.out.println("Collecting CPU and I/O usage ...");
			CpuIo = StatsCollector.CollectCpuIO("Q" + qtimeStamps.get(i));

			String featuresVector = "";
			sbuildIO.append(CpuIo[0] + "\t" + queriesFeaturesList.get(i).trim() + "\n");
			sbuildCPU.append(CpuIo[1] + "\t" + queriesFeaturesList.get(i).trim() + "\n");
			System.err.println("===>\t" + CpuIo[0] + "\t" + CpuIo[1] + "\t");

		}

		System.err.println("===> Writing I/O File ...");
		WriteToFile("files/io.txt", sbuildIO.toString(), false);
		System.err.println("===> Writing CPU File ...");
		WriteToFile("files/cpu.txt", sbuildCPU.toString(), false);
	}

	public static void WriteToFile(String fileName, String text, boolean append) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName), append));
			bw.write(text);
			bw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
