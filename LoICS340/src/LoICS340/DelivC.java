package LoICS340;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

// DelivC uses the lowest cost first search algorithm to find the lowest cost between the start and goal node of a given graph.

public class DelivC {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	
	public DelivC( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
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
		
		List<Node> nodeList = g.getNodeList();
		List<Node> lowestCostPath = new ArrayList<>();
		List<Node> finishedNodePath = new ArrayList<>();
		
		Map<Node, Node> cameFrom = new HashMap<>();
		Map<Node, Integer> costSoFar = new HashMap<>();
		
		Set<Node> finished = new HashSet<>();
		
		PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(costSoFar::get));
		
		Node startNode = new Node("");
		Node goalNode = new Node("");
		
		int sizeOfNodeList = nodeList.size();
		
		// finds the start node and goal node
		for(int i = 0; i < sizeOfNodeList; i++) {
			String getVal = nodeList.get(i).getVal();
			
			if(getVal.equals("S")) {
				startNode = nodeList.get(i);
			}
			
			if(getVal.equals("G"))
				goalNode = nodeList.get(i);
		}
		
		// initialize priority queue and hash maps to the start node
		frontier.add(startNode);
		cameFrom.put(startNode, null);
		costSoFar.put(startNode, 0);
		
		// output
		System.out.printf("%-30s %-4s\t %-20s %-4s\t %s\n", "Path", "Dist", "City", "Min_Dist", "Path");
		System.out.printf("%-30s%4d\n", startNode.getAbbrev(), costSoFar.get(startNode));
		output.printf("%-30s %-4s\t %-20s %-4s\t %s\n", "Path", "Dist", "City", "Min_Dist", "Path");
		output.printf("%-30s%4d\n", startNode.getAbbrev(), costSoFar.get(startNode));
		
		// lowest cost first search
		while(!frontier.isEmpty()) {
			Node currentNode = frontier.poll();
			List<Edge> currentNodeEdges = currentNode.getOutgoingEdges();
			
			finished.add(currentNode);
			
			// goal has been reached
			if(currentNode.getVal().equals("G")) 
				break;
			
			// grabs all the finished nodes and adds it to a linked list called finishedNodePath
			Node finishedNode = currentNode;
			while(!finishedNode.getVal().equals("S")) {
				finishedNodePath.add(finishedNode);
				finishedNode = cameFrom.get(finishedNode);
			}
			
			finishedNodePath.add(startNode);
			Collections.reverse(finishedNodePath);
			
			// builds a string with all the finished nodes
			String finishedNodePathString = "";
			for(int i = 0; i < finishedNodePath.size(); i++) {
				finishedNodePathString = finishedNodePathString + finishedNodePath.get(i).getAbbrev();
				
				if(i+1 < finishedNodePath.size()) 
					finishedNodePathString = finishedNodePathString + "-";
			}
			
			// outputs the the city name of the node we just finished, the minimum distance to that city, and the path to the city
			System.out.printf("%-30s %-4s\t %-20s %4d\t ", "", "", currentNode.getName(), costSoFar.get(currentNode));
			System.out.println(finishedNodePathString);
			output.printf("%-30s %-4s\t %-20s %4d\t ", "", "", currentNode.getName(), costSoFar.get(currentNode));
			output.println(finishedNodePathString);
			
			// iterate over all the node edges in the current node
			for(int i = 0; i < currentNodeEdges.size(); i++) {
				Node newNode = currentNodeEdges.get(i).getHead();
				int newCost = costSoFar.get(currentNode) + currentNodeEdges.get(i).getDist();
				
				// if a node is finished, skip over it
				if(finished.contains(newNode)) {
					continue;
				}
				
				// if costSoFar contains the newNode or newCost is less than the cost of the newNode, add it to the frontier and hash tables
				if(!costSoFar.containsKey(newNode) || newCost < costSoFar.get(newNode)) {
					costSoFar.put(newNode, newCost);
					frontier.add(newNode);
					cameFrom.put(newNode, currentNode);
				}
				
				// output the path we are on and the cost
				String expandPathString = finishedNodePathString + "-" + newNode.getAbbrev();
				System.out.printf("%-30s %4d\n", expandPathString, newCost);
				output.printf("%-30s %4d\n", expandPathString, newCost);
				
			}
			
			finishedNodePath.clear();
		}
		
		// output the final result from our start node to our goal node
		Node currentNode = goalNode;
		while(!currentNode.getVal().equals("S")) {
			lowestCostPath.add(currentNode);
			currentNode = cameFrom.get(currentNode);
		}
		
		lowestCostPath.add(startNode);
		Collections.reverse(lowestCostPath);
		
		System.out.print("Lowest cost path from " + startNode.getName() + " to " + goalNode.getName() + " is " + costSoFar.get(goalNode)
				+ " and the path is ");
		output.print("Lowest cost path from " + startNode.getName() + " to " + goalNode.getName() + " is " + costSoFar.get(goalNode)
				+ " abd the path is ");
		for(int i = 0; i < lowestCostPath.size(); i++) {
			System.out.print(lowestCostPath.get(i).getAbbrev());
			output.print(lowestCostPath.get(i).getAbbrev());
			
			if(i+1 < lowestCostPath.size()) {
				System.out.print("-");
				output.print("-");
			}
			else {
				System.out.println();
				output.println();
			}
		}
		
		output.flush();
	}
	
}


