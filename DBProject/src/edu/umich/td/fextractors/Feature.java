package edu.umich.td.fextractors;


public class Feature {

	public FeatureCategory category;
	public String featureName, featureValue;
	
	Feature(String name, String value, FeatureCategory type){
		this.featureName = name;
		this.featureValue = value;
		this.category = type;
	}
	
	  @Override
	  public String toString() {
		  return category.getFeatureCategory() + ": (" + featureName + ", " + featureValue + ")";
	  }
}
