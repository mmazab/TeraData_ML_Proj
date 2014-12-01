package edu.umich.td.fextractors;

public enum FeatureCategory {
	QUERYTEXT("QueryText"), 
	QUERYPLAN("QueryPlan"), 
	WORKLOAD("WorkLoad"),
	SYSTEMSTATUS("SystemStatus"),
	// the following categories are from the XML plan
	AmpPlanUsage("AmpPlanUsage"),
	AmpStepUsage("AmpStepUsage"),
	CIX("CIX"),
	Configuration("Configuration"),
	CSUpd("CSUpd"),
	Database("Database"),
	DIX("DIX"),
	Field("Field"),
	Index("Index"),
	OptPlanEst("OptPlanEst"),
	OptStepEst("OptStepEst"),
	Plan("Plan"),
	PlanStep("PlanStep"),
	PPIAccess("PPIAccess"),
	Predicate("Predicate"),
	Relation("Relation"),
	Request("Request"),
	SortKey("SortKey"),
	SourceAccess("SourceAccess"),
	Spool("Spool"),
	Statement("Statement"),
	StepDetails("StepDetails"),
	StepRecursion("StepRecursion"),
	TargetStore("TargetStore"),
	WLMgmt("WLMgmt"),
	NULL("");
	
	
	private String featureCategory;
	 
	private FeatureCategory(String s) {
		featureCategory = s;
	}
 
	public String getFeatureCategory() {
		return featureCategory;
	}
}
