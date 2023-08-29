package LoICS340;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

// Class DelivB does the work for deliverable DelivB of the Prog340

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	ArrayList<Node> nodeList;
	ArrayList<Integer> coinList = new ArrayList<Integer>();
	int size;
	
	public DelivB( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		ArrayList<Integer> bfMinCoinList = new ArrayList<Integer>();
		ArrayList<Integer> dpMinCoinList = new ArrayList<Integer>();
		int minCoins, count;
		
		nodeList = g.getNodeList();
		size = nodeList.size();
		
		// add value from file into coinList as coins
		for(int i = 0; i < size; i++)
			coinList.add(Integer.parseInt(nodeList.get(i).getVal()));
		
		// brute force programming for minimum number of coins
		for(int i = 1; i <= 61; i++) {
			minCoins = bfFindMinCoins(i);
			bfMinCoinList.add(minCoins);
		}
		
		for(int i = 1; i <= 61; i++) {
			minCoins = dpFindMinCoins(i);
			dpMinCoinList.add(minCoins);
		}
		
		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "_out.txt" );
		outputFile = new File( outputFileName );
		if ( outputFile.exists() ) {    // For retests
			outputFile.delete();
		}
		
		try {
			output = new PrintWriter(outputFile);			
		}
		catch (Exception x ) { 
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		
		count = 0;
		
		// Output list of minimum number of coins for brute force programming
		System.out.println("Here's a list of the minimum number of coins needed using brute force programming:");
		for(int i = 0; i < bfMinCoinList.size(); i++) {
			count = i + 1;
			System.out.println(count + ": " + bfMinCoinList.get(i));
		}
		
		count = 0;
		
		System.out.println("Here's a list of the minimum number of coins needed using dynamic programming:");
		for(int i = 0; i < bfMinCoinList.size(); i++) {
			count = i + 1;
			System.out.println(count + ": " + dpMinCoinList.get(i));
		}
		
		count = 0;
		
		output.println("Here's a list of the minimum number of coins needed using brute force programming:");
		for(int i = 0; i < bfMinCoinList.size(); i++) {
			count = i + 1;
			output.println(count + ": " + bfMinCoinList.get(i));
		}
		
		output.println("Here's a list of the minimum number of coins needed using dynamic programming:");
		for(int i = 0; i < bfMinCoinList.size(); i++) {
			count = i + 1;
			output.println(count + ": " + dpMinCoinList.get(i));
		}
		output.flush();
	}
	
	
	// Brute force algorithm of finding the minimum number of coins
	public int bfFindMinCoins(int unit) {
		int countingCoins, numOfCoins, coin;
		numOfCoins = Integer.MAX_VALUE;
		
		// base case if unit is 0 return 0
		if(unit == 0)
			return 0;
		
		for(int i = 0; i < size; i++) {
			coin = coinList.get(i);
			if(unit >= coin) {
				countingCoins = 1 + bfFindMinCoins(unit - coin);		// recursive call
				
				if(countingCoins < numOfCoins)
					numOfCoins = countingCoins;
			}
		}
		
		return numOfCoins;
	}
	
	// Dynamic programming algorithm of finding the minimum number of coins using bottom up
	public int dpFindMinCoins (int unit) {
		int[] dp = new int[unit + 1];
	    Arrays.fill(dp, Integer.MAX_VALUE);
	    dp[0] = 0;
	    
	    for (int i = 1; i <= unit; i++) {
	        for (int j = 0; j < size; j++) {
	            if (coinList.get(j) <= i && dp[i - coinList.get(j)] != Integer.MAX_VALUE) {
	                dp[i] = Math.min(dp[i], dp[i - coinList.get(j)] + 1);
	            }
	        }
	    }
	    
	    return dp[unit];
	}
}

