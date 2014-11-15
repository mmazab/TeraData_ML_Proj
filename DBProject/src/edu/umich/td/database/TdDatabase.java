package edu.umich.td.database;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*Author: Noy Schaal, Mahmoud Azab
 * Last Updated: November 2014
 * TdDatabase.java
 */

public class TdDatabase {

	// Credentials
	public static String sUser = "mazab";
	public static String sPassword = "eecs584";
	public static String url = "jdbc:teradata://ec2-54-167-248-214.compute-1.amazonaws.com";
	
	
	

	public Connection OpenConnection() {
		try {
			return DriverManager.getConnection(url, sUser, sPassword);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public void CloseConnection(Connection con) {
		// Close the connection
		System.out.println(" Closing connection to Teradata...");
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean ExecuteQuery(String query) {
		try {
			// Loading the Teradata JDBC driver
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			// Attempting to connect to Teradata
			Connection con = DriverManager.getConnection(url, sUser, sPassword);
			try {
				// Creating a statement object from an active connection.
				Statement stmt = con.createStatement();
				try {
					// Execute query
					System.out.println("Executing query...");
					stmt.executeQuery(query);
					System.out.println("Waiting...");
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// Close the statement
					stmt.close();
				}
			} finally {
				// Close the connection
				con.close();
			}
			return true;
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
				ex.printStackTrace();
				System.out.println();
				ex = ex.getNextException();
			}
			return false;
		}
	}

	

}