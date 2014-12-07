package edu.umich.td.fextractors;

public enum EnumFeature {
	// IndexType
	PK("Primary Key"),
	NP("Nonpartitioned Primary"),
	// PredicateKind
	Source("Source"),
	Join("Join"),
	// QCFStepKind
	LK("LK"),
	SR("SR"),
	PJ("PJ"),
	MJ("MJ"),
	SU("SU"),
	HF("HF"),
	// Confidence
	High("High"),
	Low("Low"),
	No("No"),
	// SortKind
	Roehash("Rowhash"),
	Field1("Field1"),
	//LockLevel
	Row("Row"),
	Table("Table"),
	// GeogInfo
	Local("Local"),
	Duplicated("Duplicated");
	
	
	private String enumCategory;
	 
	private EnumFeature(String s) {
		enumCategory = s;
	}
 
	public String getFeatureCategory() {
		return enumCategory;
	}
}
