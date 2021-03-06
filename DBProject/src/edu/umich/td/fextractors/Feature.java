package edu.umich.td.fextractors;


public class Feature {

	public FeatureCategory category;
	public String featureName, featureValue;
	
	Feature(String name, String value, FeatureCategory type){
		this.featureName = name;
		this.featureValue = value;
		this.category = type;
		
	}
	
    public Feature clone(){
        Feature f = new Feature(this.featureName, this.featureValue, this.category);
        return f;
    }
	
	  @Override
	  public String toString() {
		  return "(" + featureName + ", " + featureValue + ")";
	  }
}
