package edu.umich.td.fextractors;

/*
 * Author: Noy Schaal
 * Last updated: Nov 2014
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class XMLHandler extends DefaultHandler {
	
	String featFileName = "files/featureNamesNums.csv";
	ArrayList<Feature> FEATURE_SET = new ArrayList<Feature>();
	
	// Constructor: loads feature list from file
	XMLHandler() { LoadFile(); }
	
	//---------HELPER FUNCTIONS------------//
	
	// Load File function: takes in the feature list file and creates a 
	// list with initial values of 0
	void LoadFile() {
		String strLine;
		try {
			FileInputStream fstream = new FileInputStream(featFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(!strLine.isEmpty()){
					String[] features = strLine.trim().split(",");
					String category = features[0];
					String name = features[1];
					FEATURE_SET.add(new Feature(name, "0", getCategory(category)));
				}//end if
			}//end while
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	// Function to get the feature category using a string
	public static FeatureCategory getCategory(String s){
		for(FeatureCategory f : FeatureCategory.values()){
			  if (f.getFeatureCategory().equals(s)) { return f; }
			}
		return FeatureCategory.NULL;
	}
	
	
	//---------MAIN FUNCTION------------//
	  @Override
	  //Triggered when the start of tag in the XML is found.
	  public void startElement(String uri, String localName, 
	                           String qName, Attributes attributes) 
	                           throws SAXException {

			
		  int length = attributes.getLength();
		  for (int i=0; i<length; i++) {
				  String name = attributes.getQName(i);

				  
				  for(Feature f: FEATURE_SET){
					  if(f.featureName.equals(name)){
						 String value = attributes.getValue(i);
						 float valuef = Float.parseFloat(value);
						  
						 float val = Float.parseFloat(f.featureValue);
						 float result = val + valuef;
						 f.featureValue = String.valueOf(result); 
						 }
				  }
		  }
	  }
}
