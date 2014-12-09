/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * Controller.java
 */

package edu.umich.td.controller;

import java.awt.image.SampleModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.umich.td.database.*;
import edu.umich.td.fextractors.Feature;
import edu.umich.td.fextractors.FeatureCategory;
import edu.umich.td.fextractors.QPlanFeatureExtractor;
import edu.umich.td.fextractors.QTextFeatureExtractor;
import edu.umich.td.fextractors.WKLDFeatureExtractor;
import edu.umich.td.workload.DataInstance;
import edu.umich.td.workload.QueriesPool;
import edu.umich.td.workload.Query;
import edu.umich.td.workload.WorkLoadGenerator;

public class Controller {

	public static HashMap<Long, DataInstance> timeStampsToQueryFeatures = new HashMap<Long, DataInstance>();

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

		StatsCollector.tables = StatsCollector.GetAllTablesAndCounts("TPCH");

		int Temp = 0;
		int limiter = 0;
		WorkLoadGenerator workloads = new WorkLoadGenerator();
		workloads.ReadWorkLoadsFromDirectory("/home/mahmoud/Documents/DB/Data/Workloads/TestWorkloads");

		int slice = 0;
		while (workloads.wlQueries.size() > 0) {
			Connection con = TdDatabase.OpenConnection();

			ArrayList<Query> queries;
			for (int K = 0; K < 150 && K < workloads.wlQueries.size(); K++) {
				queries = workloads.wlQueries.get(K);

				if (queries.size() == 1) {
					System.out.println(K + ".");
					for (Query q : queries) {
						QTextFeatureExtractor qtFeatExtractor = new QTextFeatureExtractor();
						QPlanFeatureExtractor qtPlanFeatExtractor = new QPlanFeatureExtractor();
						q.featureVector.clear();
						q.featureVector.addAll(qtFeatExtractor.ExtractFeatures(q));
						int size = q.featureVector.size();
						// System.err.println("After text feature extraction: "+
						// q.featureVector.size());
						try {

							q.featureVector.addAll(qtPlanFeatExtractor.getFeaturesFromPlan(con, q.qText));
							// System.err.println("After PLAN feature extraction: "
							// + (q.featureVector.size()-size));
						} catch (Exception e) {
							System.err.println(q.qText);
							System.err.println(e.getMessage());
						}
					}
				}

			}

			for (int K = 0; K < 150 && K < workloads.wlQueries.size(); K++) {
				queries = workloads.wlQueries.get(K);
				ArrayList<Integer> failedQueries = new ArrayList<Integer>();

				if (queries.size() == 1 && limiter > Temp) {
					int i = 0;
					for (Query q : queries) {
						System.out.println("=========\tProcessing Query #" + K + "\t=========>  " + q.parentFolder
								+ "/" + q.fileName + "\t features: " + q.featureVector.size());

						String query = queries.get(i).qText;

						long timeStamp = System.currentTimeMillis();
						query = query.replaceFirst("\n", "/*Q" + System.currentTimeMillis() + "*/\n");
						boolean status = TdDatabase.ExecuteQuery(con, query);

						// Query failed to execute properly
						if (!status) {
							failedQueries.add(i);
						} else {
							// Add time stamp to the query so that we can
							// retrieve
							// it
							// later
							DataInstance data = new DataInstance();
							data.features = q.CloneFeatures();
							timeStampsToQueryFeatures.put(timeStamp, data);
							
						}
						i++;
					}
				} else
					limiter++;
			}
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.err.println("Writing Back");
			if (limiter > Temp)
				WriteDataIntoFile(1, "test");
			int l = 0;
			while (workloads.wlQueries.size() > 0 && l < 150 && l < workloads.wlQueries.size()) {
				workloads.wlQueries.remove(l);
				l++;
			}
			TdDatabase.CloseConnection(con);
		}

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

	static public String GetPrintableInstance(long C, ArrayList<Feature> f, int samplesSize, FeatureCategory category) {

		StringBuilder sbuild = new StringBuilder();
		sbuild.append(samplesSize + ",");
		for (Feature feature : f) {
			if (feature.category != category)
				sbuild.append(feature.featureValue + ",");
		}

		sbuild.append(C);
		return sbuild.toString();

	}

	public static void WriteDataIntoFile(int samplesSize, String type) {
		StringBuilder cpuSbuild_ALL = new StringBuilder();
		StringBuilder ioSbuild_ALL = new StringBuilder();

		StringBuilder cpuSbuild_TEXT = new StringBuilder();
		StringBuilder ioSbuild_TEXT = new StringBuilder();

		StringBuilder cpuSbuild_PLAN = new StringBuilder();
		StringBuilder ioSbuild_PLAN = new StringBuilder();

		StringBuilder cpuSbuild_WL = new StringBuilder();
		StringBuilder ioSbuild_WL = new StringBuilder();

		Connection statsCon = StatsCollector.OpenConnection();

		Iterator it = timeStampsToQueryFeatures.entrySet().iterator();
		while (it.hasNext()) {
			int[] CpuIo = { 0, 0 };
			Map.Entry pairs = (Map.Entry) it.next();
			CpuIo = StatsCollector.CollectCpuIO(statsCon, "Q" + pairs.getKey());
			DataInstance instance = (DataInstance) pairs.getValue();
			instance.CPU = CpuIo[0];
			instance.IO = CpuIo[1];

			cpuSbuild_ALL.append(GetPrintableInstance(instance.CPU, instance.features, samplesSize,
					FeatureCategory.NULL) + "\n");
			ioSbuild_ALL.append(GetPrintableInstance(instance.IO, instance.features, samplesSize, FeatureCategory.NULL)
					+ "\n");

			cpuSbuild_TEXT.append(GetPrintableInstance(instance.CPU, instance.features, samplesSize,
					FeatureCategory.QUERYTEXT) + "\n");
			ioSbuild_TEXT.append(GetPrintableInstance(instance.IO, instance.features, samplesSize,
					FeatureCategory.QUERYTEXT) + "\n");

			cpuSbuild_PLAN.append(GetPrintableInstance(instance.CPU, instance.features, samplesSize,
					FeatureCategory.QUERYPLAN) + "\n");
			ioSbuild_PLAN.append(GetPrintableInstance(instance.IO, instance.features, samplesSize,
					FeatureCategory.QUERYPLAN) + "\n");

			cpuSbuild_WL.append(GetPrintableInstance(instance.CPU, instance.features, samplesSize,
					FeatureCategory.WORKLOAD) + "\n");
			ioSbuild_WL.append(GetPrintableInstance(instance.IO, instance.features, samplesSize,
					FeatureCategory.WORKLOAD) + "\n");

			it.remove();
		}

		WriteToFile("files/Test/IO_" + samplesSize + "_ALL." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Test/CPU_" + samplesSize + "_ALL." + type, ioSbuild_ALL.toString(), true);

		WriteToFile("files/Test/IO_" + samplesSize + "_TEXt." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Test/CPU_" + samplesSize + "_TEXT." + type, ioSbuild_ALL.toString(), true);

		WriteToFile("files/Test/IO_" + samplesSize + "_PLAN." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Test/CPU_" + samplesSize + "_PLAN." + type, ioSbuild_ALL.toString(), true);

		WriteToFile("files/Test/IO_" + samplesSize + "_WL." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Test/CPU_" + samplesSize + "_WL." + type, ioSbuild_ALL.toString(), true);

		StatsCollector.CloseConnection(statsCon);
		timeStampsToQueryFeatures.clear();

	}

}
