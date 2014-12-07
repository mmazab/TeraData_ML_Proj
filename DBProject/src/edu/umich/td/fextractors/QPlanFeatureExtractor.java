package edu.umich.td.fextractors;

/*
 * Author: Noy Schaal
 * Last updated: December 2014
 */

import java.io.FileInputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class QPlanFeatureExtractor {

	// Function: takes a file name as a string and returns
	// an array list of features from the XML plan
	public ArrayList<Feature> getFeaturesFromPlan(String fileName) throws Exception{
		
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactor.newSAXParser();
	    XMLHandler handler = new XMLHandler();  
		FileInputStream fstream = new FileInputStream(fileName);
	    parser.parse(fstream,handler);
	    return handler.getFeatures();
	}
	
	// MAIN
	public static void main(String[] args) throws Exception {
		
		QPlanFeatureExtractor planex = new QPlanFeatureExtractor();
	    ArrayList<Feature> features = planex.getFeaturesFromPlan("files/plan.xml");

	    for ( Feature fe : features){
	      System.out.println(fe);
	    }
	  }
}


