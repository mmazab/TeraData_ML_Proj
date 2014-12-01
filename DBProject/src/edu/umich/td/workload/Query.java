package edu.umich.td.workload;

import java.util.ArrayList;

import edu.umich.td.fextractors.Feature;

public class Query {
	public static int Max_COUNT = 6;
	public ArrayList<Feature> featureVector =new ArrayList<Feature>();
	public String qText = "";
	public boolean included = false;
	public int inclusionCnt = 0;
	public long ioCnt = 0;
	public long cpuCnt = 0;
	
	public Query(String text) {
		this.qText = text;
	}
	

}
