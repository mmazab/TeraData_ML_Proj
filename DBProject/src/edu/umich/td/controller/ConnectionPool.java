package edu.umich.td.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

	static Connection[] connections;
	static boolean[] inUse;

	
	// Credentials
	public static String sUser = "mazab";
	public static String sPassword = "eecs584";
	
	public static String url = "jdbc:teradata://ec2-54-164-159-30.compute-1.amazonaws.com";
	
	
	

	public static boolean Open_Connection(int i) {
		try {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			// Attempting to connect to Teradata
			connections[i] = DriverManager.getConnection(url, sUser, sPassword);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	public ConnectionPool(int poolSize) {
		connections = new Connection[poolSize];	
	}
	
	
	public static boolean OpenConnection(int i){
		try {
			if(!connections[i].isClosed()){
				return Open_Connection(i);
			}
		} catch (SQLException e) {
			
		}
		return false;
		
	}
	
	public static Connection GetConnection() {

		while (true) {
			for (int i = 0; i < inUse.length; i++) {
				if (!inUse[i]) {
					try {
						if(!connections[i].isClosed()){
							
							return connections[i];
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
				e.getMessage();
				return null;
			}
			
		}
	}

}
