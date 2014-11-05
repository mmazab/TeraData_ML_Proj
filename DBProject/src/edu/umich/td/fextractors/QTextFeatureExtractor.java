package edu.umich.td.fextractors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class QTextFeatureExtractor {
	
	String path;
	String tmpFileName = "files/tmp.feat";
	ArrayList<String> ReadFeatFromFile(){
		String strLine;
		ArrayList<String> featuresList = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(tmpFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(!strLine.isEmpty()){
					String[] features = strLine.trim().split("\t");
					for(int i=0; i<features.length; i++)
						featuresList.add(features[i]);
				}
			}
			in.close();
			return featuresList;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}
	
	
	public ArrayList<String> ExtractFeatures(String query){
		
		try{
		
		
		path = new File("").getAbsolutePath();
		WriteQueryToFile(path + "/files/tmp.sql", query, false);
		
		String command = "cd "+ path +"/files; python fextractor.py";
        Process p;
        
        p = Runtime.getRuntime().exec("bash");
        PrintWriter pw = new PrintWriter(p.getOutputStream());
        pw.print(command);
        pw.close();
        
        
        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader ms = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ln = "";

        while ((ln = error.readLine()) != null)
            System.err.println(ln);

        while ((ln = ms.readLine()) != null)
            System.out.println(ln);
        System.out.print("Extracting features ... ");
        p.waitFor();
        System.out.println("Done");
        return ReadFeatFromFile();
		}catch(Exception e){
			return null;
		}
	}
	
	public static void WriteQueryToFile(String fileName, String text, boolean append) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName), append));
			bw.write(text);
			bw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
