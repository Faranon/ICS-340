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
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;


// DelivD uses the A* search algorithm to find the lowest cost between a start and goal node given a graph. Another graph called the heuristic graph is also needed.

public class DelivD {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	JFileChooser fileChooser;
	
	public DelivD( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
		Graph hg = new Graph();
		
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
		
		// Gets node list from graph
		List<Node> nodeList = g.getNodeList();
		List<Node> hNodeList;
		List<Node> finishedNodePath = new ArrayList<>();
		List<Node> lowestCostPath = new ArrayList<>();
		
		// Gets file for heuristics graph
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose a file");
		fileChooser.setCurrentDirectory(new File("."));
		readGraphInfo(hg);
		hNodeList = hg.getNodeList();
		
		Map<Node, Node> cameFrom = new HashMap<>();
		Map<Node, Integer> costSoFar = new HashMap<>();
		
		Set<Node> finished = new HashSet<>();
		
		PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(costSoFar::get));
		
		Node startNode = new Node("");
		Node goalNode = new Node("");
		
		int sizeOfNodeList = nodeList.size();
		int totalCost = 0;
		int heuristicCost = 0;
		
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
		costSoFar.put(startNode, totalCost + heuristicCost);
		
		// output
		System.out.println("Shortest path from " + startNode.getName() + " (" + startNode.getAbbrev() + ") to " + goalNode.getName()
				+ " (" + goalNode.getAbbrev() + ")");
		System.out.printf("%-30s %-4s\t %-4s\t %-4s\n", "Path", "Dist", "Heur", "Value");
		System.out.printf("%-30s %-4s\t %-4s\t %-4s\n", startNode.getAbbrev(), totalCost, heuristicCost, costSoFar.get(startNode));
		
		output.println("Shortest path from " + startNode.getName() + " (" + startNode.getAbbrev() + ") to " + goalNode.getName()
			+ " (" + goalNode.getAbbrev() + ")");
		output.printf("%-30s %-4s\t %-4s\t %-4s\n", "Path", "Dist", "Heur", "Value");
		output.printf("%-30s %-4s\t %-4s\t %-4s\n", startNode.getAbbrev(), totalCost, heuristicCost, costSoFar.get(startNode));
		
		// A* Search
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
			
			// iterate over all the node edges in the current node
			for(int i = 0; i < currentNodeEdges.size(); i++) {
				Node newNode = currentNodeEdges.get(i).getHead();
				int edgeCost = costSoFar.get(currentNode) + currentNodeEdges.get(i).getDist();
				
				// if a node is finished, skip over it
				if(finished.contains(newNode)) {
					continue;
				}
				
				for(int x = 0; x < hNodeList.size(); x++) {
					Node hNode = hNodeList.get(x);
					if(currentNode.getName().equals(hNode.getName())) {
						List<Edge> hEdgeList = hNode.getOutgoingEdges();
						
						for(int j = 0; j < hEdgeList.size(); j++) {
							Edge hEdge = hEdgeList.get(j);
							if(newNode.getAbbrev().equals(hEdge.getHead().getAbbrev()))
								heuristicCost = hEdge.getDist();
						}
					}
				}
				
				totalCost = edgeCost + heuristicCost;
				
				// if costSoFar contains the newNode or newCost is less than the cost of the newNode, add it to the frontier and hash tables
				if(!costSoFar.containsKey(newNode) || totalCost < costSoFar.get(newNode)) {
					costSoFar.put(newNode, totalCost);
					frontier.add(newNode);
					cameFrom.put(newNode, currentNode);
				}
				
				// output the path we are on and the cost
				String expandPathString = finishedNodePathString + "-" + newNode.getAbbrev();
				System.out.printf("%-30s %-4s\t %-4s\t %-4s\n", expandPathString, edgeCost, heuristicCost, totalCost);
				output.printf("%-30s %-4s\t %-4s\t %-4s\n", expandPathString, edgeCost, heuristicCost, totalCost);
				
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
			+ " and the path is ");
		
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
	}
	
	/** Read the file containing the Strings, line by line, then process each line as it is read.
	**/
	  public void readGraphInfo( Graph g ) {

		try {
			
		   	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

		   		// Instantiate the selected input and output files.
		   		inputFile = fileChooser.getSelectedFile();
		   		System.out.println( "Chosen file = " + inputFile + "\n");
		   	}
		   	
			// read text file
			Scanner sc = new Scanner( inputFile );
			// First line special:  It contains "~", and "val", and the nodes with the edges.
			String firstLine = sc.nextLine();
			String[] splitString = firstLine.split( " +" );
			
			// Ignore first two fields of first line,  Every other field is a node.
			for ( int i = 2; i < splitString.length; i++ ) {
				Node n = new Node( splitString[i] );
				g.addNode( n );
			}
			
			// Every other line gives the name and value of the Node, and any edges.
			int nodeIndex = 0;
			ArrayList<Node> nodeList = g.getNodeList();
			
			while ( sc.hasNextLine() ) {
				String nextLine = sc.nextLine();
				splitString = nextLine.split(" +");

				Node n = nodeList.get( nodeIndex );
				n.setName( splitString[0] );
				n.setVal( splitString[1] );

				for ( int i = 2; i < splitString.length; i++ ) {
					if ( !splitString[i].equals("~") ) {
						Node head = nodeList.get(i-2);
						int dist = Integer.parseInt( splitString[i] );
						Edge e = new Edge( n, head, dist );
						g.addEdge( e );
						n.addOutgoingEdge( e );
						head.addIncomingEdge( e );
					}
				}
				nodeIndex++;

			}
			sc.close();

		}
		catch (Exception x) {
			System.err.format("ExceptionOuter: %s%n", x);
		}
	  }	
}


