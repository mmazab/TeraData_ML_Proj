package edu.umich.td.workload;

import java.util.ArrayList;
import java.util.Random;

/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * WorkLoadGenerator.java
 */

public class WorkLoadGenerator {

	//Number of queries within each sample
	int wlCNT = 0;
	//Number of samples to be generated
	int samplesCnt = 0;
	
	//The generated Workload Queries
	ArrayList<ArrayList<Query>> wlQueries;

	// Random variable to select which pool to grab from and how many queries to be grabbed
	static Random whichPool = new Random();
	// Random variable to select queries from High-CPU High-IO queries pool
	static Random hcpu_hio_RndGen = new Random();
	// Random variable to select queries from High-CPU Low-IO queries pool
	static Random hcpu_lio_RndGen = new Random();
	// Random variable to select queries from Low-CPU High-IO queries pool
	static Random lcpu_hio_RndGen = new Random();
	// Random variable to select queries from Low-CPU Low-IO queries pool
	static Random lcpu_lio_RndGen = new Random();

	public WorkLoadGenerator() {
		wlQueries = new ArrayList<ArrayList<Query>>();
	}

	public void Generate(int batchSize, int samplesCnt) {
		// Create parallel workload of a certain number of queries
		wlCNT = batchSize;
		//The target number of samples that the WorkloadGenerator will generate
		this.samplesCnt = samplesCnt;
		//Special case when the batch size equals 1, we generate all the queries to run sequentially
		if (wlCNT == 1)
			GenerateAll();
		else {
			for (int i = 0; i < samplesCnt; i++) {
				int grabbed = 0;
				ArrayList<Query> qList = new ArrayList<Query>(wlCNT);
				while (grabbed != wlCNT) {
					int selectpool = whichPool.nextInt(4);
					int grabCNT = whichPool.nextInt(wlCNT);
					int qInstance = 0;
					// Grab the queries from the High-CPU High-IO queries pool
					if (selectpool == 0) {
						//Grab certain number of queries from this pool
						//the total grabbed queries don't exceed the batch size
						for (int j = 0; j < grabCNT && grabbed < wlCNT; j++) {
							int poolSize = QueriesPool.hCPU_hIOList.size();
							if (poolSize > 0) {
								qInstance = hcpu_hio_RndGen.nextInt(poolSize);
								//if the query was not included too much, we will grab it. else skip this query
								if (QueriesPool.hCPU_hIOList.get(qInstance).inclusionCnt < Query.Max_COUNT) {
									qList.add(QueriesPool.hCPU_hIOList.get(qInstance));
									//increment the inclusion count of the grabbed query
									QueriesPool.hCPU_hIOList.get(qInstance).inclusionCnt++;
									grabbed++;
								}
							}
						}
					} // Grab the queries from the High-CPU Low-IO queries pool
					else if (selectpool == 1) {
						//Grab certain number of queries from this pool
						//the total grabbed queries don't exceed the batch size
						for (int j = 0; j < grabCNT && grabbed < wlCNT; j++) {
							int poolSize = QueriesPool.hCPU_lIOList.size();
							if (poolSize > 0) {
								qInstance = hcpu_lio_RndGen.nextInt(poolSize);
								//if the query was not included too much, we will grab it. else skip this query
								if (QueriesPool.hCPU_lIOList.get(qInstance).inclusionCnt < Query.Max_COUNT) {
									qList.add(QueriesPool.hCPU_lIOList.get(qInstance));
									//increment the inclusion count of the grabbed query
									QueriesPool.hCPU_lIOList.get(qInstance).inclusionCnt++;
									grabbed++;
								}
							}
						}
					} // Grab the queries from the Low-CPU High-IO queries pool
					else if (selectpool == 2) {
						//Grab certain number of queries from this pool
						//the total grabbed queries don't exceed the batch size
						for (int j = 0; j < grabCNT && grabbed < wlCNT; j++) {
							int poolSize = QueriesPool.lCPU_hIOList.size();
							if (poolSize > 0) {
								qInstance = lcpu_hio_RndGen.nextInt(poolSize);
								//if the query was not included too much, we will grab it. else skip this query
								if (QueriesPool.lCPU_hIOList.get(qInstance).inclusionCnt < Query.Max_COUNT) {
									qList.add(QueriesPool.lCPU_hIOList.get(qInstance));
									//increment the inclusion count of the grabbed query
									QueriesPool.lCPU_hIOList.get(qInstance).inclusionCnt++;
									grabbed++;
								}
							}
						}

					} // Grab the queries from the Low-CPU Low-IO queries pool
					else if (selectpool == 3) {
						//Grab certain number of queries from this pool
						//the total grabbed queries don't exceed the batch size
						for (int j = 0; j < grabCNT && grabbed < wlCNT; j++) {
							int poolSize = QueriesPool.lCPU_lIOList.size();
							if (poolSize > 0) {
								qInstance = lcpu_lio_RndGen.nextInt(poolSize);
								//if the query was not included too much, we will grab it. else skip this query 
								if (QueriesPool.lCPU_lIOList.get(qInstance).inclusionCnt < Query.Max_COUNT) {
									qList.add(QueriesPool.lCPU_lIOList.get(qInstance));
									//increment the inclusion count of the grabbed query
									QueriesPool.lCPU_lIOList.get(qInstance).inclusionCnt++;
									grabbed++;
								}
							}
						}
					}
				}
				wlQueries.add(qList);
				System.out.println("Sample number " + (i+1) + " of size " + wlCNT + " has been created." );
			}
		}
	}

	
	//This function generates the full list of queries
	void GenerateAll() {
		for (Query q : QueriesPool.hCPU_hIOList) {
			ArrayList<Query> singleQueryList = new ArrayList<Query>(1);
			singleQueryList.add(q);
			wlQueries.add(singleQueryList);
		}
		for (Query q : QueriesPool.hCPU_lIOList) {
			ArrayList<Query> singleQueryList = new ArrayList<Query>(1);
			singleQueryList.add(q);
			wlQueries.add(singleQueryList);
		}
		for (Query q : QueriesPool.lCPU_hIOList) {
			ArrayList<Query> singleQueryList = new ArrayList<Query>(1);
			singleQueryList.add(q);
			wlQueries.add(singleQueryList);
		}
		for (Query q : QueriesPool.lCPU_lIOList) {
			ArrayList<Query> singleQueryList = new ArrayList<Query>(1);
			singleQueryList.add(q);
			wlQueries.add(singleQueryList);
		}

	}

}
