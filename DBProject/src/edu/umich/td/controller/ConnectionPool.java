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

	public static Connection Open_Connection(int i) {
		try {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			// Attempting to connect to Teradata
			connections[i] = DriverManager.getConnection(url, sUser, sPassword);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public ConnectionPool(int poolSize) {
		connections = new Connection[poolSize];
		
		for(int i=0; i < connections.length; i++){
			
			connections[i] = Open_Connection(i);
			
		}
		
		inUse = new boolean[poolSize];
	}

	public static boolean OpenConnection(int i) {
		try {
			if (!connections[i].isClosed()) {
				Open_Connection(i);
			}
		} catch (SQLException e) {
			return false;
		}
		return true;

	}

	public static Connection GetConnection() {

		while (true) {
			for (int i = 0; i < inUse.length; i++) {
				if (!inUse[i]) {
					try {
						if (!connections[i].isClosed()) {
							return connections[i];
						}else{
							if(OpenConnection(i))
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