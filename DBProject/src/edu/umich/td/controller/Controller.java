/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * Controller.java
 */

package edu.umich.td.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import edu.umich.td.database.*;
import edu.umich.td.fextractors.QTextFeatureExtractor;

public class Controller {

	public static void main(String[] args) {
		// 1- Load queries into the query pool from the files
		// 2- Pass these queries to the workload creator
		// 3- Extract query/workload features
		// 4- Get query/workload XML execution plans
		// 5- Extract features from the execution plans
		// 6- Execute the returned list of queries from the WL creator
		// 7- Collect CPU and I/O usage
		QueriesPool qpool = new QueriesPool();
		ArrayList<String> queriesFeaturesList = new ArrayList<String>();
		ArrayList<String> featuresList = new ArrayList<String>();
		ArrayList<String> queries = qpool
				.ReadQueriesFromFile("files/queries.sql");
		ArrayList<Integer> failedQueries = new ArrayList<Integer>();
		ArrayList<Long> qtimeStamps = new ArrayList<Long>();
		for (int i = 0; i < queries.size(); i++) {
			System.out.println("=========\tProcessing Query #" + i
					+ "\t=========");
			QTextFeatureExtractor qtFeatExtractor = new QTextFeatureExtractor();
			featuresList = qtFeatExtractor
					.ExtractFeatures(queries.get(i));

			

			String query = queries.get(i);
			long timeStamp = System.currentTimeMillis();
			query = query.replaceFirst("\n", "/*Q" + System.currentTimeMillis()
					+ "*/\n");
			boolean status = TdDatabase.ExecuteQuery(query);

			if (!status) {
				failedQueries.add(i);
				//continue;
			} else {
				qtimeStamps.add(timeStamp);
				String feat = "";
				for (String feature : featuresList){
					System.out.print(feature + "\t");
					feat += feature + "\t";
				}
				queriesFeaturesList.add(feat);
			}
			System.out.println("\n");
		}

		for (int i =0; i < failedQueries.size(); i++) {
			//System.out.println(instance);
			queries.remove(failedQueries.get(i));
			
		}
		
		ArrayList<Integer> Cpu = new ArrayList<Integer>();
		ArrayList<Integer> IO = new ArrayList<Integer>();
		StringBuilder sbuildCPU = new StringBuilder();
		StringBuilder sbuildIO = new StringBuilder();
		
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i =0; i < qtimeStamps.size(); i++){
			int[] CpuIo = { 0, 0 };
			System.out.println("Collecting CPU and I/O usage ...");
			CpuIo = StatsCollector.CollectCpuIO("Q" + qtimeStamps.get(i) );
			
			//IO.add(CpuIo[0]);
			String featuresVector = "";

			sbuildIO.append( CpuIo[0] +"\t"+ queriesFeaturesList.get(i).trim() + "\n");
			sbuildCPU.append( CpuIo[1] +"\t"+ queriesFeaturesList.get(i).trim() +"\n");
			//Cpu.add(CpuIo[0]);
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
