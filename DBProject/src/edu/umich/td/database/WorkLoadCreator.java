package edu.umich.td.database;

/*Author: Mahmoud Azab
 * Last Updated: November 2014
 * WorkLoadCreator.java
 */

import java.util.ArrayList;

public class WorkLoadCreator {
	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	public static ArrayList<String[]> batches = new ArrayList<String[]>();
	static void printCombination(String queries[], int n, int batchSize)
	{
	    // A temporary array to store all combination one by one
	    String[] batch = new String[batchSize];
	 
	    // Print all combination using temprary array 'data[]'
	    combination(queries, batch, 0, n-1, 0, batchSize);
	}
	 
	/**
	 * 
	 * @param queries --> the array of queries
	 * @param batch --> the batch array of queries
	 * @param start --> the index in array where we start
	 * @param end --> the index in array till where we loop
	 * @param index --> the index in data where we need to add the next element
	 * @param batchSize
	 */
	static void combination(String queries[], String[] batch, int start, int end, int index, int batchSize)
	{
	    // Current combination is ready to be printed, print it
	    if (index == batchSize)
	    {
	        batches.add(batch);
	        return;
	    }
	 
	    for (int i = start; i <= end && end - i + 1 >= batchSize - index; i++)
	    {
	        batch[index] = queries[i];
	        combination(queries, batch, i+1, end, index+1, batchSize);
	    }
	}
	
	/**
	 * 
	 * @param queries --> the set of 100 input queries
	 * @return 
	 */
	public static ArrayList<String[]> createWorkLoads(String[] queries) {
	    int n = queries.length;
	    for(int batchSize = 2; batchSize <= n; batchSize++) {
	    	printCombination(queries, n, batchSize);
	    }
	    System.out.println(batches.size());
	    return batches;
	}
}
