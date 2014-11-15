package edu.umich.td.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatsCollector {
	
	public static String sUser = "dbc";
	public static String sPassword = "eecs58414";
	public static String url = "jdbc:teradata://ec2-54-167-248-214.compute-1.amazonaws.com";
	
	public static int[] CollectCpuIO(String query) {
		int[] CpuIO = { 0, 0 };
		String getData = "SELECT TotalIOCount, AMPCPUTime FROM DBC.QRYlogv WHERE QueryText LIKE '%" 
				+ query + "%' and UserName = 'MAZAB';";
		try {
			// Loading the Teradata JDBC driver
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Connection con = DriverManager.getConnection(url, sUser, sPassword);
			

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
				// Close the connection
				System.out.println(" Closing connection to Teradata...");
				con.close();
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

}
