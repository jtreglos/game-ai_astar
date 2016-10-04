package graph;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {
	private Graph<String> graph;
	
	@Before
	public void setUp() throws Exception {
		graph = new Graph<String>();
		graph.addDirectedEdge("A", "B", 10);
		graph.addUndirectedEdge("B", "C", 15);
		graph.addDirectedEdge("B", "D", 10);
		graph.addDirectedEdge("D", "C", 4);
		graph.addUndirectedEdge("C", "E", 10);
	}

	@Test
	public void testAddDirectedEdge() {
		assertFalse(graph.adjacentTo("B").contains("F"));
		graph.addDirectedEdge("B", "F", 10);
		assertTrue(graph.adjacentTo("B").contains("F"));
		assertFalse(graph.adjacentTo("F").contains("B"));
	}

	@Test
	public void testAddUndirectedEdge() {
		assertFalse(graph.adjacentTo("B").contains("F"));
		graph.addUndirectedEdge("B", "F", 10);
		assertTrue(graph.adjacentTo("B").contains("F"));
		assertTrue(graph.adjacentTo("F").contains("B"));
	}

	@Test
	public void testNbEdges() {
		assertEquals(graph.nbEdges(), 7);
	}

	@Test
	public void testNbVertices() {
		assertEquals(graph.nbVertices(), 5);
	}

	@Test
	public void testGetVertices() {
		Set<String> vertices = graph.getVertices();
		assertTrue(vertices.containsAll(Arrays.asList("A", "B", "C", "D", "E")));
		assertFalse(vertices.contains("F"));
	}

	@Test
	public void testAdjacentTo() {
		assertEquals(graph.adjacentTo("A"), new HashSet<String>(Arrays.asList("B")));
		assertEquals(graph.adjacentTo("B"), new HashSet<String>(Arrays.asList("D", "C")));
		assertEquals(graph.adjacentTo("C"), new HashSet<String>(Arrays.asList("B", "E")));
	}

	@Test
	public void testDegree() {
		assertEquals(graph.degree("A"), 1);
		assertEquals(graph.degree("B"), 2);
		assertEquals(graph.degree("C"), 2);
	}

	@Test
	public void testHasVertex() {
		assertTrue(graph.hasVertex("C"));
		assertFalse(graph.hasVertex("F"));
	}

	@Test
	public void testHasEdge() {
		assertTrue(graph.hasEdge("A", "B"));
		assertTrue(graph.hasEdge("C", "E"));
		assertFalse(graph.hasEdge("A", "C"));
		assertFalse(graph.hasEdge("D", "B"));
	}

	@Test
	public void testWeight() {
		assertEquals(graph.weight("A", "B"), 10.0, 0.001);
		assertEquals(graph.weight("C", "E"), 10.0, 0.001);
	}

}
