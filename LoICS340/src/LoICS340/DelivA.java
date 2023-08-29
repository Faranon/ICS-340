package LoICS340;
import java.io.*;
import java.util.ArrayList;

// DelivA uses Edge.java and Node.java to find the amount of nodes, edges, most outgoing edges, and longest edge between two nodes given a graph

public class DelivA {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	
	public DelivA( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		ArrayList<Node> nodeList;
		ArrayList<Node> largestEdgeNodeList = new ArrayList<Node>();
		ArrayList<Edge> edgeList;
		ArrayList<Edge> longestEdgeList = new ArrayList<Edge>();
		Node tail = new Node("");
		Node head = new Node("");
		String graphName;
		int mostEdge, nodeListSize, nodeListSize1, maxValue, edgeListSize, longestEdgeListSize;
		
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
		
		// Gets graph name and removes ".txt" from the end
		graphName = inputFile.getName().replace(".txt", "");
		
		// Sets nodeList and edgeList equal to the graphs nodeList and edgeList
		nodeList = g.getNodeList();
		edgeList = g.getEdgeList();
		
		if(!edgeList.isEmpty() && !nodeList.isEmpty()) {
			// get sizes for nodeListSize and edgeListSize used for for loops
			nodeListSize = nodeList.size();
			edgeListSize = edgeList.size();
			
			// Finds the most amount of edges
			mostEdge = nodeList.get(0).getOutgoingEdges().size();
			for(int i = 1; i < nodeListSize; i++) {
				if(mostEdge < nodeList.get(i).getOutgoingEdges().size()) {
					mostEdge = nodeList.get(i).getOutgoingEdges().size();
				}
			}
			
			// All nodes with the most edges are put into an arrayList largestEdgeNodeList
			for(int i = 0; i < nodeListSize; i++) {
				if(mostEdge == nodeList.get(i).getOutgoingEdges().size())
					largestEdgeNodeList.add(nodeList.get(i));
			}
			
			// Finds the largest edge within all the nodes
			maxValue = edgeList.get(0).getDist();
			for(int i = 1; i < edgeListSize; i++) {
				if(maxValue < edgeList.get(i).getDist()) {
					maxValue = edgeList.get(i).getDist();
				}

			}
			
			// All nodes that have the largest distance are put into an arrayList longestEdgeList
			for(int i = 0; i < edgeListSize; i++) {
				if(edgeList.get(i).getDist() == maxValue)
					longestEdgeList.add(edgeList.get(i));
			}
			
			longestEdgeListSize = longestEdgeList.size();
			
			// Output
			System.out.println("Graph: " + graphName);
			System.out.println("There are " + nodeList.size() + " nodes in the graph");
			System.out.println("There are " + edgeList.size() + " edges in the graph");
			
			// Output all nodes that have the most outgoing edges
			System.out.print("Node ");
			nodeListSize1 = largestEdgeNodeList.size();
			for(int i = 0; i < nodeListSize1; i++) {
				System.out.print(largestEdgeNodeList.get(i).getAbbrev());
				if(nodeListSize1 == 2 && i != nodeListSize1-1)
					System.out.print(" and ");
				else if(nodeListSize1 >= 3 && i != nodeListSize1-1) {
					System.out.print(", ");
					if(i == nodeListSize1-2)
						System.out.print("and ");
				}
			}
			System.out.println(" has the most outgoing edges (" + mostEdge + ").");
			
			// Output the longest edges
			System.out.print("The longest edges are edges ");
			for(int i = 0; i < longestEdgeListSize; i++) {
				tail = longestEdgeList.get(i).getTail();
				head = longestEdgeList.get(i).getHead();
				System.out.print(tail.getAbbrev() + head.getAbbrev());
				if(longestEdgeListSize == 2 && i != longestEdgeListSize-1)
					System.out.print(" and ");
				else if(longestEdgeListSize >= 3 && i != longestEdgeListSize-1) {
					System.out.print(", ");
					if(i == longestEdgeListSize-2)
						System.out.print("and ");
				}
			}
			System.out.println(" (" + maxValue + ")");
			
			
			// Write to file
			output.println("Graph: " + graphName);
			output.println("There are " + nodeList.size() + " nodes in the graph");
			output.println("There are " + edgeList.size() + " edges in the graph");
			output.print("Node ");
			for(int i = 0; i < nodeListSize1; i++) {
				output.print(largestEdgeNodeList.get(i).getAbbrev());
				if(nodeListSize1 == 2 && i != nodeListSize1-1)
					output.print(" and ");
				else if(nodeListSize1 >= 3 && i != nodeListSize1-1) {
					output.print(", ");
					if(i == nodeListSize1-2)
						output.print("and ");
				}
			}
			
			output.println(" has the most outgoing edges (" + mostEdge + ").");
			output.print("The longest edges are edges ");
			for(int i = 0; i < longestEdgeListSize; i++) {
				tail = longestEdgeList.get(i).getTail();
				head = longestEdgeList.get(i).getHead();
				output.print(tail.getAbbrev() + head.getAbbrev());
				if(longestEdgeListSize == 2 && i != longestEdgeListSize-1)
					output.print(" and ");
				else if(longestEdgeListSize >= 3 && i != longestEdgeListSize-1) {
					output.print(", ");
					if(i == longestEdgeListSize-2)
						output.print("and ");
				}
			}
			output.println(" (" + maxValue + ")");
			output.flush();
		}
		else {
			System.out.println("Graph: " + graphName);
			System.out.println("There are " + nodeList.size() + " nodes in the graph");
			System.out.println("There are 0 edges in the graph.");
			
			output.println("Graph: " + graphName);
			output.println("There are " + nodeList.size() + " nodes in the graph");
			output.println("There are 0 edges in the graph.");
		}
	}
}
