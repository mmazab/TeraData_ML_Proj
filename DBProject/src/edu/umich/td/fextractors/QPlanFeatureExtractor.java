package edu.umich.td.fextractors;

/*
 * Author: Noy Schaal
 * Last updated: December 2014
 */

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.umich.td.database.TdDatabase;


public class QPlanFeatureExtractor {

	// Function: takes a file name as a string and returns
	// an array list of features from the XML plan
	public ArrayList<Feature> getFeaturesFromPlan(Connection con, String query) throws Exception{
		
		String plan = getPlanFromQuery(con, query);
		plan = plan.substring(plan.indexOf('\n')+1);
		PrintWriter writer = new PrintWriter("plan.xml", "UTF-8");
		writer.println(plan);
		writer.close();
		
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactor.newSAXParser();
	    XMLHandler handler = new XMLHandler();  
		FileInputStream fstream = new FileInputStream("plan.xml");
	    parser.parse(fstream,handler);
	    return handler.getFeatures();
	}
	
	public String getPlanFromQuery(Connection con, String query) {
		
		String plan = "";
		String explainline = "EXPLAIN IN XML ";
		query = explainline + query;
		
		try {
			// Loading the Teradata JDBC driver

			try {
				// Creating a statement object from an active connection.
				Statement stmt = con.createStatement();
				try {
					
					// This handles multiple result sets
					boolean results = stmt.execute(query);
					
					while(results){
					
						ResultSet rs = stmt.getResultSet();
						while (rs.next()) {
							plan = plan + rs.getString(1);
						}
						rs.close();
						
						// Check for more results
						results = stmt.getMoreResults();
					
					}
					// ----------------------------------
							
					//System.out.println(plan);
				}finally {
					// Close the statement
					stmt.close();
				}
			} finally {
				
			}

			return plan;
		} catch (SQLException ex) {
			// A SQLException was generated. Catch it and display
			// the error information.
			// Note that there could be multiple error objects chained
			// together.
			System.out.println();
			System.out.println("*** SQLException caught ***");

			while (ex != null) {
				System.out.println(" Error code: " + ex.getErrorCode());
				System.out.println(" SQL State: " + ex.getSQLState());
				System.out.println(" Message: " + ex.getMessage());
			}
			return plan;
		}
	}
}

