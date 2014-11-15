package edu.umich.td.fextractors;

public class Feature {
	
	public String featureName, featureValue;
	
	Feature(String name, String value){
		this.featureName = name;
		this.featureValue = value;
	}
	
	  @Override
	  public String toString() {
		  return "(" + featureName + ", " + featureValue + ")";
	  }
}

