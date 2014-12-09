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
		int totalIO=0, totalCPU=0;
		
		for(Query q: workload){
			totalIO = totalIO + findIOindex(q);
			totalCPU = totalCPU + findCPUindex(q); 
		}

		for(Query q: workload){
			int size = q.featureVector.size();
			q.featureVector.add(new Feature("ConcurrentQueries", String.valueOf(concQueries), FeatureCategory.WORKLOAD));
			//q.featureVector.add(new Feature("TotalCPU", String.valueOf(totalCPU), FeatureCategory.WORKLOAD));
			//q.featureVector.add(new Feature("TotalIO", String.valueOf(totalIO), FeatureCategory.WORKLOAD));
			System.err.println("After WORKLOAD feature extraction: " + q.featureVector.size());
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