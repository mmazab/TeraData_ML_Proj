package edu.umich.td.database;

/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * QueriesPool.java
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QueriesPool {
	ArrayList<String> QueriesList;
	
	public QueriesPool(){
		QueriesList = new ArrayList<String>();
	}
	public ArrayList<String> ReadQueriesFromFile(String fileName){
		String strLine;
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuilder sbuild = new StringBuilder();

			while ((strLine = br.readLine()) != null) {
				if(!strLine.isEmpty()){
					if(strLine.contains(";")){
						sbuild.append(strLine);
						QueriesList.add(sbuild.toString());
						sbuild = new StringBuilder();
					}	
					else
						sbuild.append(strLine+"\n");
				}
			}
			in.close();
			return QueriesList;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		System.err.println("******************** END of file ****************** ");
		return null;
	}
	
	public ArrayList<ArrayList<String>> ClusteredQueries(){
		
		return null;
	}
	

}
