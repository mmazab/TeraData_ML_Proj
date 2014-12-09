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
	public String fileName ="";
	public String parentFolder = "";
	public boolean written = false;
	public boolean status = true;
	public boolean blocked = false;
	public Query(String text) {
		this.qText = text;
	}
	
	
	public ArrayList<Feature> CloneFeatures(){
		ArrayList<Feature> flist = new ArrayList<Feature>();
		for(Feature f: featureVector){
			flist.add(f.clone());
		}
		
		return flist;
		
	}
	

}
