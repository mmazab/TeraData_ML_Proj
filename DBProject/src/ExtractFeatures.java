/*Author: Noy Schaal
 * Last Updated: November 2014
 * ExtractFeatures.java
 */

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class ExtractFeatures {

	 // Credentials
    public static String sUser = "dbc";
    public static String sPassword = "eecs58414";
    
    // Query Info
    final static String FILE_NAME = "files/test.sql"; 
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	
	/**
	 * Returns the string of the query from the SQL file.
	 */
	public static String LoadQuery(String aFileName) throws IOException
	{
		Path path = Paths.get(aFileName);
		String query = "";
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				query = query + String.valueOf(scanner.nextLine())+ " ";
			}
		}
		return query;
	}
	
	/**
	 * Main
	 */
    public static void main(String args[])throws ClassNotFoundException
    {
        // Creation of URL to be passed to the JDBC driver
        String url = "jdbc:teradata://ec2-54-167-248-214.compute-1.amazonaws.com";
        
        String query = "";       
		try { query = LoadQuery(FILE_NAME); }
		catch (IOException e) { e.printStackTrace(); }
		
        String getData = "SELECT TotalIOCount, AMPCPUTime, MaxAMPCPUTime, MinAmpCPUTime "
				+ "FROM DBC.QRYlogv "
				+ "WHERE QueryText= \'" + query + "\';";		
        
       try {
            // Loading the Teradata JDBC driver
            Class.forName("com.teradata.jdbc.TeraDriver");
            System.out.println(" JDBC driver loaded. \n");

            // Attempting to connect to Teradata
            Connection con = DriverManager.getConnection(url, sUser, sPassword);
            System.out.println(" Connection to Teradata established. \n");

            try {
                // Creating a statement object from an active connection.
                Statement stmt = con.createStatement();
                System.out.println(" Statement object created. \n");

                try {
                    // Execute query
                    System.out.println("Executing query...");
                    stmt.executeQuery(query);
                    
                    System.out.println("Waiting...");
                    Thread.sleep(5000);
                    
                    System.out.println("Getting features...");
                    ResultSet rs = stmt.executeQuery(getData);
                    
                    int rowCount = 0;
                    while(rs.next())
                    {
                        rowCount ++;
                        System.out.println("IO: " + rs.getInt(1) + " CPU: " + rs.getInt(2));
                    }
                    System.out.println("\n " + rowCount + " row(s) returned.");
                    
                    
                } catch (InterruptedException e) {e.printStackTrace();}
                finally {
                    // Close the statement
                    stmt.close();
                    System.out.println("\n Statement object closed. \n");
                }
            }
            finally {
                // Close the connection
                System.out.println(" Closing connection to Teradata...");
                con.close();
            }
        }
        catch (SQLException ex) {
            // A SQLException was generated.  Catch it and display
            // the error information.
            // Note that there could be multiple error objects chained
            // together.
            System.out.println();
            System.out.println("*** SQLException caught ***");

            while (ex != null)
            {
                System.out.println(" Error code: " + ex.getErrorCode());
                System.out.println(" SQL State: " + ex.getSQLState());
                System.out.println(" Message: " + ex.getMessage());
                ex.printStackTrace();
                System.out.println();
                ex = ex.getNextException();
            }

            throw new IllegalStateException ("Sample failed.") ;
        }
    } 
}
	



