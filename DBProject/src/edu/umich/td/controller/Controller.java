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
		for(int i=0; i<queries.size(); i++){
			System.out.println("=========\tProcessing Query #" + i +"\t=========" );
			QTextFeatureExtractor qtFeatExtractor = new QTextFeatureExtractor();
			ArrayList<String> featuresList = qtFeatExtractor.ExtractFeatures(queries.get(i));
			
			TdDatabase.ExecuteQuery(queries.get(i));
			for (String feature : featuresList) {
				System.out.print(feature+"\t");
			} 
			System.out.println("\n");
			
			
			
		}

	}

}
