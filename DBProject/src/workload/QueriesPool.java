package workload;

/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * QueriesPool.java
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QueriesPool {
	static ArrayList<Query> QueriesList;
	static ArrayList<Query> hCPU_hIOList;
	static ArrayList<Query> hCPU_lIOList;
	static ArrayList<Query> lCPU_hIOList;
	static ArrayList<Query> lCPU_lIOList;
	static int Q_TOTAL_COUNT;
	double CPU_AVG = 0;
	double IO_AVG = 0;

	public QueriesPool() {
		QueriesList = new ArrayList<Query>();
		hCPU_hIOList = new ArrayList<Query>();
		hCPU_lIOList = new ArrayList<Query>();
		lCPU_hIOList = new ArrayList<Query>();
		lCPU_lIOList = new ArrayList<Query>();
	}

	// This function loads the queries from file(s) and create a list of queries
	public ArrayList<Query> ReadQueriesFromFile(String fileName) {
		String strLine;
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuilder sbuild = new StringBuilder();
			while ((strLine = br.readLine()) != null) {
				if (!strLine.isEmpty()) {
					if (strLine.contains(";")) {
						sbuild.append(strLine);
						QueriesList.add(new Query(sbuild.toString()));
						sbuild = new StringBuilder();
					} else
						sbuild.append(strLine + "\n");
				}
			}
			in.close();
			return QueriesList;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		System.err.println("******************** END of file ****************** ");
		return null;
	}

	/*
	 * Cluster the queries into 4 classes: 1- High CPU, High I/O 2- High CPU,
	 * low I/O 3- low CPU, High I/O 4- low CPU, low I/O
	 */
	public void ClusterQueries() {
		ArrayList<Integer> cpuConsmp = ReadStatsFiles("files/cpu.txt");
		ArrayList<Integer> ioConsmp = ReadStatsFiles("files/io.txt");
		long cpuSum = 0, ioSum = 0;

		if (ioConsmp.size() == cpuConsmp.size()) {
			for (int i = 0; i < ioConsmp.size(); i++) {
				cpuSum += cpuConsmp.get(i);
				ioSum += ioConsmp.get(i);
			}
		}
		IO_AVG = ioSum / (ioConsmp.size());
		CPU_AVG = cpuSum / (cpuConsmp.size());

		if (ioConsmp.size() == cpuConsmp.size() && ioConsmp.size() <= QueriesList.size()) {
			for (int i = 0; i < ioConsmp.size(); i++) {
				QueriesList.get(i).cpuCnt = cpuConsmp.get(i);
				QueriesList.get(i).ioCnt = ioConsmp.get(i);
				// 1- High CPU, High I/O
				if (cpuConsmp.get(i) > CPU_AVG && ioConsmp.get(i) > IO_AVG) {
					hCPU_hIOList.add(QueriesList.get(i));
				}// 2- High CPU, low I/O
				else if (cpuConsmp.get(i) > CPU_AVG && ioConsmp.get(i) <= IO_AVG) {
					hCPU_lIOList.add(QueriesList.get(i));
				}// 3- low CPU, High I/O
				else if (cpuConsmp.get(i) <= CPU_AVG && ioConsmp.get(i) > IO_AVG) {
					lCPU_hIOList.add(QueriesList.get(i));
				}// 4- low CPU, low I/O
				else if (cpuConsmp.get(i) <= CPU_AVG && ioConsmp.get(i) <= IO_AVG) {
					lCPU_lIOList.add(QueriesList.get(i));
				}
			}
		}
	}

	public ArrayList<Integer> ReadStatsFiles(String fileName) {
		String strLine;
		// This is the consumption value of the resource either CPU or I/O
		ArrayList<Integer> resConsumption = new ArrayList<Integer>();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if (!strLine.isEmpty()) {
					resConsumption.add(Integer.parseInt(strLine.split("\t")[0]));
				}
			}
			in.close();
			return resConsumption;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		System.err.println("******************** END of file ****************** ");
		return null;

	}

	public void ResetInclusionCount() {
		for (Query q : hCPU_hIOList) {
			q.inclusionCnt = 0;
		}
		for (Query q : hCPU_lIOList) {
			q.inclusionCnt = 0;
		}
		for (Query q : lCPU_hIOList) {
			q.inclusionCnt = 0;
		}
		for (Query q : lCPU_lIOList) {
			q.inclusionCnt = 0;
		}
	}

	public int TotalQueriesCount() {
		Q_TOTAL_COUNT = hCPU_hIOList.size() + hCPU_lIOList.size() + lCPU_hIOList.size() + lCPU_lIOList.size();
		return Q_TOTAL_COUNT;
	}

}
