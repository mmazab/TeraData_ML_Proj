package edu.umich.td.fextractors;

import java.util.ArrayList;
import edu.umich.td.workload.*;

public class WKLDFeatureExtractor {

	// Function: takes in a list of workloads, 
	// adds workload features to each query feature vector
	public void getWKLDFeatures(ArrayList<ArrayList<Query>> list){
		for(ArrayList<Query> workload : list){
			extractFeatures(workload);
		}
	}
	
	
	// Function: gets features for each workload
	public void extractFeatures(ArrayList<Query> workload){
		int concQueries = workload.size(); 
		int sameTables=0, commonCond=0, totalIO=0, totalCPU=0;
		ArrayList<String[]> listOfTexts = new ArrayList<String[]>();
		
		for(Query q: workload){
			totalIO = totalIO + findIOindex(q);
			totalCPU = totalCPU + findCPUindex(q); 
			String text[] = q.qText.split("select|from|where|group by|order by");
			listOfTexts.add(text);
		}
		
		ArrayList<String> tables = new ArrayList<String>();
		ArrayList<String> wheres = new ArrayList<String>();
		
		for(String[] t: listOfTexts){
			tables.add(t[0]);
			wheres.add(t[1]);
		}
		
		for(int i = 2; i<tables.size(); i++){
			if(tables.get(i).equals(tables.get(i-1))){ sameTables++; }
		}
		
		for(int i = 2; i<wheres.size(); i++){
			if(wheres.get(i).equals(wheres.get(i-1))){ commonCond++; }
		}

		for(Query q: workload){
			q.featureVector.add(new Feature("ConcurrentQueries", String.valueOf(concQueries), FeatureCategory.WORKLOAD));
			q.featureVector.add(new Feature("TotalCPU", String.valueOf(totalCPU), FeatureCategory.WORKLOAD));
			q.featureVector.add(new Feature("TotalIO", String.valueOf(totalIO), FeatureCategory.WORKLOAD));
			q.featureVector.add(new Feature("SameTables", String.valueOf(sameTables), FeatureCategory.WORKLOAD));
			q.featureVector.add(new Feature("SameCond", String.valueOf(commonCond), FeatureCategory.WORKLOAD));
		}
		
	}
	
	
	
	// ---- HELPER FUNCTIONS ----
	public int findIOindex(Query q){
		int i = 0;
		for(Feature f: q.featureVector){
			if(f.featureName.equals("IOCount")){
				return i;
			}else{
				i++;
			}
		}
		return 0;
	}
	
	public int findCPUindex(Query q){
		int i = 0;
		for(Feature f: q.featureVector){
			if(f.featureName.equals("CPUTime")){
				return i;
			}else{
				i++;
			}
		}
		return 0;
	}

}
