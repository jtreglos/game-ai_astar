package graph;

import java.util.*;

public class Graph<V> {
	protected Set<GraphEdge<V>> edges;
	protected Map<V, Set<GraphEdge<V>>> vertices;
	
	public Graph() {
		edges = new HashSet<GraphEdge<V>>();
		vertices = new HashMap<V, Set<GraphEdge<V>>>();
	}
	
	public void clear() {
		edges.clear();
		vertices.clear();
	}
	
	public void addDirectedEdge(V from, V to, double weight) {
		GraphEdge<V> edge = new GraphEdge<V>(from, to, weight);
		edges.add(edge);
		
		Set<GraphEdge<V>> edges_for_from = vertices.get(from);
		if (edges_for_from == null) edges_for_from = new HashSet<GraphEdge<V>>();
		edges_for_from.add(edge);
		vertices.put(from, edges_for_from);
		
		Set<GraphEdge<V>> edges_for_to = vertices.get(to);
		if (edges_for_to == null) {
			vertices.put(to, new HashSet<GraphEdge<V>>());
		}
	}
	
	public void addUndirectedEdge(V from, V to, double weight) {
		addDirectedEdge(from, to, weight);
		addDirectedEdge(to, from, weight);
	}
	
	public int nbEdges() {
		return edges.size();
	}
	
	public Set<GraphEdge<V>> getEdges() {
		return edges;
	}
	
	public int nbVertices() {
		return vertices.size();
	}
	
	public Set<V> getVertices() {
		return vertices.keySet();
	}
	
	public Set<V> adjacentTo(V v) {
		Set<GraphEdge<V>> adjacent_edges = vertices.get(v);
		Set<V> ret = new HashSet<V>();
		
		for(GraphEdge<V> edge : adjacent_edges) ret.add(edge.getTo());
		
		return ret;
	}
	
	public Set<GraphEdge<V>> getConnections(V v) {
		return new HashSet<GraphEdge<V>>(vertices.get(v));
	}
	
	public int degree(V v) {
		return adjacentTo(v).size();
	}
	
	public boolean hasVertex(V v) {
		return vertices.keySet().contains(v);
	}
	
	public boolean hasEdge(V from, V to) {
		Set<GraphEdge<V>> edges_for_from = vertices.get(from);
		for(GraphEdge<V> edge : edges_for_from) {
			if (edge.getTo().equals(to)) {
				return true;
			}
		}
		
		return false;
	}
	
	public double weight(V from, V to) {
		if (hasEdge(from, to)) {
			Set<GraphEdge<V>> edges_for_from = vertices.get(from);
			for(GraphEdge<V> edge : edges_for_from) {
				if (edge.getTo().equals(to)) {
					return edge.getWeight();
				}
			}
		}
		
		return 0.0;
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		for (Map.Entry<V, Set<GraphEdge<V>>> entry : vertices.entrySet()) {
			ret += entry.getKey() + " :\n";
			ret += "\t" + entry.getValue().toString() + "\n";
		}
		
		return ret;
	}
}
