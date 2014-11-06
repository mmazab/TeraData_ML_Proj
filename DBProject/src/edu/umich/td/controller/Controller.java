
/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * WorkLoadCreator.java
 */

package edu.umich.td.controller;
import java.util.ArrayList;

import edu.umich.td.database.*;
import edu.umich.td.fextractors.QTextFeatureExtractor;
public class Controller {

	public static void main(String[] args) {
		//1- Load queries into the query pool from the files
		//2- Pass these queries to the workload creator
		//3- Extract query/workload features
		//4- Get query/workload XML execution plans
		//5- Extract features from the execution plans
		//6- Execute the returned list of queries from the WL creator
		//7- Collect CPU and I/O usage
		QueriesPool qpool = new QueriesPool();
		ArrayList<String> queries = qpool.ReadQueriesFromFile("files/queries.sql");
		ArrayList<Integer> failedQueries = new ArrayList<Integer>();
		for(int i=0; i<queries.size(); i++){
			System.out.println("=========\tProcessing Query #" + i +"\t=========" );
			QTextFeatureExtractor qtFeatExtractor = new QTextFeatureExtractor();
			ArrayList<String> featuresList = qtFeatExtractor.ExtractFeatures(queries.get(i));
			
			int[] CpuIo = {0,0};
			
			String query = queries.get(i);
			System.out.println( query );
			
			query = query.replaceFirst("\n", "/*Q" + System.currentTimeMillis()+"*/\n" );
			System.err.println( query );
			boolean status = TdDatabase.ExecuteQuery(queries.get(i));
			
			if(!status) 
				failedQueries.add(i+1);
			else {
				System.out.println("Collecting CPU and I/O usage ...");
				//CpuIo =  StatsCollector.CollectCpuIO(queries.get(i));
				System.err.println("===>\t" + CpuIo[0] + "\t" + CpuIo[1]);
			}
			
			for (String feature : featuresList) {
				System.out.print(feature+"\t");
			} 
			System.out.println("\n");
		}
		
		for (Integer instance : failedQueries) {
			System.out.println(instance);
		}

	}

}
