package edu.umich.td.fextractors;

public enum FeatureCategory {
	QUERYTEXT("QueryText"), 
	QUERYPLAN("QueryPlan"), 
	WORKLOAD("WorkLoad"), 
	SYSTEMSTATUS("SystemStatus"),
	AmpPlanUsage("AmpPlanUsage"),
	AmpStepUsage("AmpStepUsage"),
	CHKWL("CHKWL"),
	CIX("CIX"),
	Configuration("Configuration"),
	CSUpd("CSUpd"),
	Database("Database"),
	DIX("DIX"),
	Field("Field"),
	Index("Index"),
	OptPlanEst("OptPlanEst"),
	Plan("Plan"),
	PlanStep("PlanStep"),
	PPIAccess("PPIAccess"),
	Predicate("Predicate"),
	Query("Query"),
	Relation("Relation"),
	Request("Request"),
	Session("Session"),
	SortKey("SortKey"),
	SourceAccess("SourceAccess"),
	Spool("Spool"),
	Statement("Statement"),
	StepDetails("StepDetails"),
	StepRecursion("StepRecursion"),
	TargetStore("TargetStore"),
	ViewRef("ViewRef"),
	WLMgmt("WLMgmt");
	
	
	private String featureCategory;
	 
	private FeatureCategory(String s) {
		featureCategory = s;
	}
 
	public String getFeatureCategory() {
		return featureCategory;
	}
}
