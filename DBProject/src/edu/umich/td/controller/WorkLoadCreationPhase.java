package edu.umich.td.controller;


import java.util.ArrayList;


import edu.umich.td.workload.QueriesPool;
import edu.umich.td.workload.Query;
import edu.umich.td.workload.WorkLoadGenerator;

public class WorkLoadCreationPhase {

	public static void main(String[] args) {

		// 1- Load labeled queries into the query pool from the files
		// 2- Cluster queries into 4 categories
		// 3- Pass the different query pool to the workload creator
		// 4- Create different queries patch of different sizes

		QueriesPool qpool = new QueriesPool();
		qpool.ReadLabeledQueriesFromDirectory("/home/mahmoud/Documents/DB/Data/labeledQueries/Staged/StagedTraining");
		qpool.ClusterQueries();
		int qTotalCount = qpool.TotalQueriesCount();

		WorkLoadGenerator workload = new WorkLoadGenerator();

		int samplesCount = 0;
		int batchSize = 0;

		for (int j = 0; j < 5; j++) {
			qpool.ResetInclusionCount();
			Query.Max_COUNT = 7 - j;
			samplesCount = 240 - j * 40;
			batchSize = 5 * j;
			if (samplesCount * batchSize <= qTotalCount * Query.Max_COUNT) {
				workload.Generate(batchSize, samplesCount);
			} else {
				System.err
						.printf("The queries pool of size %d cannot generate %d samples with size %d using maximum repetition count of %d\n",
								qTotalCount, samplesCount, batchSize, Query.Max_COUNT);
				System.err.printf("Please increase the maximum repetition count to at least %d\n",
						(int) Math.ceil((double) ((samplesCount * batchSize) / qTotalCount)) + 1);
			}
		}
		
		workload.WriteWorkloadsToDisk("/home/mahmoud/Documents/DB/Data/Workloads/TrainingWorkloads");


	}

}
