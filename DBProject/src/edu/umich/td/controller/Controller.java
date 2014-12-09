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
		// 5- Read different query batches
		// 6- Extract query/workload features
		// 7- Get query/workload execution plans
		// 8- Extract features from the execution plans
		// 9- Execute the returned list of queries from the WL creator
		// 10- Collect CPU and I/O usage

		StatsCollector.tables = StatsCollector.GetAllTablesAndCounts("TPCH");

		int Temp = 0;
		int limiter = 0;
		int batch_size = 1;
		WorkLoadGenerator workloads = new WorkLoadGenerator();
		workloads.ReadWorkLoadFromDirectory("/home/mahmoud/Documents/DB/Data/Workloads/TrainingWorkloads", batch_size);

		Connection con = TdDatabase.OpenConnection();

		int K = 0;
		for (ArrayList<Query> queries : WorkLoadGenerator.wlQueries) {

			for (Query q : queries) {
				System.out.println(K++ + ".");
				QTextFeatureExtractor qtFeatExtractor = new QTextFeatureExtractor();
				QPlanFeatureExtractor qtPlanFeatExtractor = new QPlanFeatureExtractor();
				q.featureVector.clear();
				q.featureVector.addAll(qtFeatExtractor.ExtractFeatures(q));
				try {
					q.featureVector.addAll(qtPlanFeatExtractor.getFeaturesFromPlan(con, q.qText));
				} catch (Exception e) {
					System.err.println(q.qText);
					System.err.println(e.getMessage());
				}
			}
		}
		int Counter = 0;
		for (ArrayList<Query> queries : WorkLoadGenerator.wlQueries) {

			if (queries.size() == batch_size) {
				for (Query q : queries) {
					System.out.println("=========\tProcessing Query #" + Counter++ + "\t=========>  " + q.parentFolder
							+ "/" + q.fileName + "\t features: " + q.featureVector.size());

					String query = q.qText;

					long timeStamp = System.currentTimeMillis();
					query = query.replaceFirst("\n", "/*Q" + timeStamp + "*/\n");
					boolean status = TdDatabase.ExecuteQuery(con, query);

					// Query failed to execute properly
					if (!status) {
						q.status = false;
					} else {
						// Add time stamp to the query so that we can
						// retrieve
						// it
						// later
						DataInstance data = new DataInstance();
						data.features = q.CloneFeatures();
						timeStampsToQueryFeatures.put(timeStamp, data);

					}

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

		WriteDataIntoFile(batch_size, "test");


		TdDatabase.CloseConnection(con);

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

			if (category == FeatureCategory.QUERYPLAN) {

				if (feature.category == FeatureCategory.WORKLOAD || feature.category == FeatureCategory.SYSTEMSTATUS
						|| feature.category == FeatureCategory.QUERYTEXT
						|| feature.category == FeatureCategory.QUERYPLAN)
					sbuild.append(feature.featureValue + ",");

			} else {

				if (feature.category != category)
					sbuild.append(feature.featureValue + ",");
			}
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

		WriteToFile("files/Train/IO_" + samplesSize + "_ALL." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Train/CPU_" + samplesSize + "_ALL." + type, ioSbuild_ALL.toString(), true);

		WriteToFile("files/Train/IO_" + samplesSize + "_TEXt." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Train/CPU_" + samplesSize + "_TEXT." + type, ioSbuild_ALL.toString(), true);

		WriteToFile("files/Train/IO_" + samplesSize + "_PLAN." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Train/CPU_" + samplesSize + "_PLAN." + type, ioSbuild_ALL.toString(), true);

		WriteToFile("files/Train/IO_" + samplesSize + "_WL." + type, cpuSbuild_ALL.toString(), true);
		WriteToFile("files/Train/CPU_" + samplesSize + "_WL." + type, ioSbuild_ALL.toString(), true);

		StatsCollector.CloseConnection(statsCon);
		timeStampsToQueryFeatures.clear();

	}

}
