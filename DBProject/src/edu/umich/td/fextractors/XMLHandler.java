package edu.umich.td.fextractors;

/*
 * Author: Noy Schaal
 * Last updated: Dec 2014
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
	
	String numbersFileName = "files/featureNamesNums.csv";
	String booleansFileName = "files/featureNamesBools.csv";
	String enumsFileName = "files/featureNamesEnums.csv";
	
	ArrayList<Feature> numbers = new ArrayList<Feature>();
	ArrayList<Feature> booleans = new ArrayList<Feature>();
	ArrayList<Feature> enums = new ArrayList<Feature>();
	
	// Constructor: loads feature list from file
	XMLHandler() { LoadFileNums(); LoadFileBools(); LoadFileEnums(); }
	
	//---------HELPER FUNCTIONS------------//
	
	// Load File function: takes in the feature list file and creates a 
	// list with initial values of 0
	void LoadFileNums() {
		String strLine;
		try {
			FileInputStream fstream = new FileInputStream(numbersFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(!strLine.isEmpty()){
					String[] features = strLine.trim().split(",");
					String category = features[0];
					String name = features[1];
					numbers.add(new Feature(name, "0", getCategory(category)));
				}//end if
			}//end while
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	// Load file function: creates the boolean feature list
	void LoadFileBools() {
		String strLine;
		try {
			FileInputStream fstream = new FileInputStream(booleansFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(!strLine.isEmpty()){
					String[] features = strLine.trim().split(",");
					String category = features[0];
					String name = features[1];
					booleans.add(new Feature(name+"-AND", "1", getCategory(category)));
					booleans.add(new Feature(name+"-OR", "0", getCategory(category)));
				}//end if
			}//end while
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	// Load file function: creates the feature list that are not nums or bools
	void LoadFileEnums() {
		String strLine;
		try {
			FileInputStream fstream = new FileInputStream(enumsFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(!strLine.isEmpty()){
					String[] features = strLine.trim().split(",");
					String category = features[0];
					String name = features[1];
					switch(name){
						case "IndexType":
							enums.add(new Feature(name+"PK", "0", getCategory(category)));
							enums.add(new Feature(name+"NP", "0", getCategory(category)));
						case "PredicateKind":
							enums.add(new Feature(name+"Source", "0", getCategory(category)));
							enums.add(new Feature(name+"Join", "0", getCategory(category)));
						case "QCFStepKind":
							enums.add(new Feature(name+"LK", "0", getCategory(category)));
							enums.add(new Feature(name+"SR", "0", getCategory(category)));
							enums.add(new Feature(name+"PJ", "0", getCategory(category)));
							enums.add(new Feature(name+"MJ", "0", getCategory(category)));
							enums.add(new Feature(name+"SU", "0", getCategory(category)));
							enums.add(new Feature(name+"HF", "0", getCategory(category)));
						case "Confidence":
							enums.add(new Feature(name+"High", "0", getCategory(category)));
							enums.add(new Feature(name+"Low", "0", getCategory(category)));
							enums.add(new Feature(name+"No", "0", getCategory(category)));
						case "SortKind":
							enums.add(new Feature(name+"Rowhash", "0", getCategory(category)));
							enums.add(new Feature(name+"Field1", "0", getCategory(category)));
						case "LockLevel":
							enums.add(new Feature(name+"Row", "0", getCategory(category)));
							enums.add(new Feature(name+"Table", "0", getCategory(category)));
						case "GeogInfo":
							enums.add(new Feature(name+"Local", "0", getCategory(category)));
							enums.add(new Feature(name+"Duplicated", "0", getCategory(category)));
							enums.add(new Feature(name+"Hash Distributed", "0", getCategory(category)));
					}
				}//end if
			}//end while
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	// Function to get the feature category using a string
	private static FeatureCategory getCategory(String s){
		for(FeatureCategory f : FeatureCategory.values()){
			  if (f.getFeatureCategory().equals(s)) { return f; }
			}
		return FeatureCategory.NULL;
	}
	
	// converting strings to booleans
	private static boolean convertToBoolean(String value) {
	    boolean returnValue = false;
	    if ("1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "T".equalsIgnoreCase(value)) {
	        returnValue = true;
	    }
	    return returnValue;
	}
	
	// function to combine all three feature arrays into one
	public ArrayList<Feature> getFeatures(){
		ArrayList<Feature> FEATURE_SET = new ArrayList<Feature>();
		for(Feature f : numbers) {
			FEATURE_SET.add(f.clone());
		}
		for(Feature f : booleans){
			FEATURE_SET.add(f.clone());
		}
		for(Feature f : enums){
			FEATURE_SET.add(f.clone());
		}
		return FEATURE_SET;
		
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
				String value = attributes.getValue(i);
				System.out.println(name+", "+value);
				boolean bresult = false;
				float fresult = 0;

				// ---code to calc numerical features---  
				for(Feature fn: numbers){
					  if(fn.featureName.equals(name)){
						 float old_val = Float.parseFloat(fn.featureValue);
						 float new_val = Float.parseFloat(value);
						  
						 fresult = old_val + new_val;
						 fn.featureValue = String.valueOf(fresult); 
						 } // end if
				} // end for
				     
				
				// ---code to calc boolean features---
				for(Feature fb: booleans){
						boolean old_val = convertToBoolean(fb.featureValue);
						boolean new_val = convertToBoolean(value);
						
						if(fb.featureName.equals(name+"-AND")){
							bresult = old_val && new_val; 
							int r = bresult ? 1 : 0;
							fb.featureValue = String.valueOf(r); 
						} // end if 
						  
						if(fb.featureName.equals(name+"-OR")){
							bresult = old_val || new_val; 
							int r = bresult ? 1 : 0;
							fb.featureValue = String.valueOf(r); 
						} // end if
				} // end for 
				
				
				for(Feature fe: enums){
					  if(fe.featureName.equals(name+value)){
						  int old_val = Integer.parseInt(fe.featureValue);
						  int one = 1; 
							  
						  int result = old_val + one;
						  fe.featureValue = String.valueOf(result); 
						 } // end if
				} // end for
		  }
	  }
}
