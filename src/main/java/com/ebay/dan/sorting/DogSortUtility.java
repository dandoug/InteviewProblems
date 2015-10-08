package com.ebay.dan.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.westminsterkenel.IDog;


public class DogSortUtility {
	

	/**
	 * Refer to http://www.geeksforgeeks.org/topological-sorting/
	 * @param inputDogs
	 * @return
	 */
	public static List<IDog> topologicalSort(List<Dog> inputDogs)
	{
		// Build graph from input.
		DirectedGraph<IDog, DefaultEdge> g = 
				new DefaultDirectedGraph<IDog, DefaultEdge>(DefaultEdge.class);
		
		// Add the vertices
		for (IDog d : inputDogs ) {
			g.addVertex(d);
		}
		
		// Add the edges, verticies have to be there 
		for (IDog d : inputDogs ) {
			for (IDog b : d.dogsBittenByThisDog()) {
				g.addEdge(d, b);
			}
		}
		
		// Check that graph is acyclic before we start
		CycleDetector<IDog, DefaultEdge>  detector = new CycleDetector<>(g);
		if (detector.detectCycles()) {
			Set<IDog> cycleVertexSet = detector.findCycles();
			throw new IllegalStateException("Cycles detected among these dogs: "+cycleVertexSet);
		}
		
		// Find the topographical order
		TopologicalOrderIterator<IDog, DefaultEdge> it = new TopologicalOrderIterator<>(g);
		List<IDog> result = new ArrayList<>();
		while(it.hasNext()) {
			result.add(it.next());
		}
		
		return result;
	}

}
