package edu.umich.td.controller;

import java.sql.Connection;
import java.util.ArrayList;

import edu.umich.td.database.StatsCollector;
import edu.umich.td.database.TdDatabase;
import edu.umich.td.fextractors.Feature;
import edu.umich.td.fextractors.QTextFeatureExtractor;
import edu.umich.td.workload.QueriesPool;
import edu.umich.td.workload.Query;

public class StagingPhase {

	public static void main(String[] args) {
		// 1- Load queries into the query pool from the files
		// 2- Run all the queries sequentially to detect each queries behavior
		
		QueriesPool qpool = new QueriesPool();
		ArrayList<Query> queries = qpool.ReadQueriesFromDirectory("/home/mahmoud/Documents/DB/Data/TrainingData");
		
		ArrayList<Integer> failedQueries = new ArrayList<Integer>();
		ArrayList<Long> qtimeStamps = new ArrayList<Long>();
		Connection con = TdDatabase.OpenConnection();
		
		TdDatabase.ExecuteQuery(con, "database TPCH;");
		
		
		for (int i = 0; i < queries.size(); i++) {
			//Write queries to file after processing 100 queries
			if(i % 100 == 0){
				Connection statsCon =  StatsCollector.OpenConnection();
				for (int j = 0; j < qtimeStamps.size(); j++) {
					int[] CpuIo = { 0, 0 };
					System.out.println("Collecting CPU and I/O usage ...");
					CpuIo = StatsCollector.CollectCpuIO(statsCon, "Q" + qtimeStamps.get(j));
					queries.get(j).cpuCnt = CpuIo[0];
					queries.get(j).ioCnt = CpuIo[1];
					System.err.println("===>\t" + CpuIo[0] + "\t" + CpuIo[1] + "\t");
				}
				StatsCollector.CloseConnection(statsCon);
				qpool.WriteFilesToDirectory("/home/mahmoud/Documents/DB/Data/labeledQueries");
			}
			
			
			System.out.println("========= Query #" + i + " on the stage\t=========");
			System.out.println(queries.get(i).parentFolder+ "/" +queries.get(i).fileName);
			String query = queries.get(i).qText;
			long timeStamp = System.currentTimeMillis();
			query = query.replaceFirst("\n", "/*Q" + System.currentTimeMillis() + "*/\n");
			boolean status = TdDatabase.ExecuteQuery(con, query);

			// Query failed to execute properly
			if (!status) {
				failedQueries.add(i);
			} // Query is executed successfully
			else {
				// Add time stamp to the query so that we can retrieve it later
				qtimeStamps.add(timeStamp);
			}
			System.out.println("\n");
		}
		for (int i = 0; i < failedQueries.size(); i++) {
			queries.remove(failedQueries.get(i));
		}
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Connection statsCon =  StatsCollector.OpenConnection();
		
		for (int i = 0; i < qtimeStamps.size(); i++) {
			int[] CpuIo = { 0, 0 };
			System.out.println("Collecting CPU and I/O usage ...");
			CpuIo = StatsCollector.CollectCpuIO(statsCon, "Q" + qtimeStamps.get(i));
			queries.get(i).cpuCnt = CpuIo[0];
			queries.get(i).ioCnt = CpuIo[1];
			System.err.println("===>\t" + CpuIo[0] + "\t" + CpuIo[1] + "\t");
		}
		
		
		StatsCollector.CloseConnection(statsCon);
		TdDatabase.CloseConnection(con);
		
		
		qpool.WriteFilesToDirectory("/home/mahmoud/Documents/DB/Data/labeledQueries");
	}

}
