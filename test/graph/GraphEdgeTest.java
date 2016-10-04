package graph;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GraphEdgeTest {
	private GraphEdge<String> edge;

	@Before
	public void setUp() throws Exception {
		edge = new GraphEdge<String>("Start", "Destination", 123);
	}

	@Test
	public void testHashCode() {
		GraphEdge<String> edge_same = new GraphEdge<String>("Start", "Destination", 123);
		GraphEdge<String> edge_different_start = new GraphEdge<String>("Other Start", "Destination", 123);
		GraphEdge<String> edge_different_destination = new GraphEdge<String>("Start", "Other Destination", 123);
		GraphEdge<String> edge_different_weight = new GraphEdge<String>("Start", "Destination", 234);
		GraphEdge<String> edge_different_all = new GraphEdge<String>("Other Start", "Other Destination", 234);
		
		assertEquals(edge.hashCode(), edge_same.hashCode());
		assertNotEquals(edge.hashCode(), edge_different_start.hashCode());
		assertNotEquals(edge.hashCode(), edge_different_destination.hashCode());
		assertNotEquals(edge.hashCode(), edge_different_weight.hashCode());
		assertNotEquals(edge.hashCode(), edge_different_all.hashCode());
	}

	@Test
	public void testGetFrom() {
		assertEquals(edge.getFrom(), "Start");
		assertNotEquals(edge.getFrom(), "Other Start");
	}

	@Test
	public void testGetTo() {
		assertEquals(edge.getTo(), "Destination");
		assertNotEquals(edge.getTo(), "Other Destination");
	}

	@Test
	public void testGetWeight() {
		assertEquals(edge.getWeight(), 123.0, 0.001);
		assertNotEquals(edge.getWeight(), 234.0);
	}

	@Test
	public void testEqualsObject() {
		GraphEdge<String> edge_same = new GraphEdge<String>("Start", "Destination", 123);
		GraphEdge<String> edge_different_start = new GraphEdge<String>("Other Start", "Destination", 123);
		GraphEdge<String> edge_different_destination = new GraphEdge<String>("Start", "Other Destination", 123);
		GraphEdge<String> edge_different_weight = new GraphEdge<String>("Start", "Destination", 234);
		GraphEdge<String> edge_different_all = new GraphEdge<String>("Other Start", "Other Destination", 234);
		
		assertEquals(edge, edge_same);
		assertNotEquals(edge, edge_different_start);
		assertNotEquals(edge, edge_different_destination);
		assertNotEquals(edge, edge_different_weight);
		assertNotEquals(edge, edge_different_all);
	}

}
