package edu.umich.td.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StatsCollector {

	public static String sUser = "dbc";
	public static String sPassword = "eecs58414";
	public static String url = "jdbc:teradata://ec2-54-224-213-223.compute-1.amazonaws.com";
	
	public static Map<String, Integer> tables;
	
	
	public static Connection OpenConnection() {
		try {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			// Attempting to connect to Teradata
			Connection con = DriverManager.getConnection(url, sUser, sPassword);
			return con;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public static void CloseConnection(Connection con) {
		// Close the connection
		System.out.println(" Closing connection to Teradata...");
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int[] CollectCpuIO(Connection con, String query) {
		int[] CpuIO = { 0, 0 };
		String getData = "SELECT AMPCPUTime, TotalIOCount FROM DBC.QRYlogv WHERE QueryText LIKE '%"
				+ query + "%' and UserName = 'MAZAB';";
		try {
			// Loading the Teradata JDBC driver
			try {
				Statement stmt = con.createStatement();
				System.out.println(" Statement object created. \n");

				try {
					ResultSet rs = stmt.executeQuery(getData);

					int rowCount = 0;
					while (rs.next()) {
						rowCount++;
						System.out.println("IO: " + rs.getInt(1) + " CPU: "
								+ rs.getInt(2));
						CpuIO[0] = rs.getInt(1);
						CpuIO[1] = rs.getInt(2);
					}
					System.out.println("\n " + rowCount + " row(s) returned.");
				} finally {
					// Close the statement
					stmt.close();
					System.out.println("\n Statement object closed. \n");
				}
			} finally {
				
				
			}
			return CpuIO;
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
			return null;
		}

	}

	public static Map<String, Integer> GetAllTablesAndCounts(String dbName) {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		try {
			// Loading the Teradata JDBC driver
			Class.forName("com.teradata.jdbc.TeraDriver");
			Connection con = DriverManager.getConnection(url, sUser, sPassword);
			String getData = "HELP database " + dbName + ";";
			try {
				Statement stmt = con.createStatement();
				try {
					ResultSet rs = stmt.executeQuery(getData);
					int rowCount = 0;
					while (rs.next()) {
						rowCount++;
						if(!rs.getString(1).contains("_")){
							int count = GetTableRowCount(con, dbName, rs.getString(1));
							countMap.put(rs.getString(1), count);
						}
					}
				} finally {
					// Close the statement
					stmt.close();
				}
			} finally {
				// Close the connection
				con.close();
			}
			return countMap;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	private static int GetTableRowCount(Connection con, String dbName, String tableName) {
		String getData = "Select COUNT(*) from  " + dbName +"."+tableName + ";";
		try {
			Statement stmt = con.createStatement();
			try {
				ResultSet rs = stmt.executeQuery(getData);
				while (rs.next()) {
					return rs.getInt(1);
				}
			}
			finally {
				// Close the statement
				stmt.close();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return 0;
	}

}
