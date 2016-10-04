package solver;

import java.util.*;
import route.*;
import solver.Action;
import solver.AStarNode;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {
	private RouteProblem problem;
	private AStarNode node;
	private AStarNode parent_node;
	
	@Before
	public void setUp() throws Exception {
		problem = new RouteProblem("Timisoara", "Neamt", RouteProblem.DEFAULT_EDGES);
		parent_node = new AStarNode(problem);
		Action action = problem.availableActions(parent_node.getState()).get(0);
		node = parent_node.apply(action);
	}

	@Test
	public void testGetState() {
		assertEquals(node.getParent().getState(), problem.initialState());
		assertNotEquals(node.getState(), problem.goalState());
	}

	@Test
	public void testGetPathCost() {
		assertEquals(node.getPathCost(), 111.0, 0.001);
	}

	@Test
	public void testGetParent() {
		assertEquals(node.getParent(), parent_node);
	}

	@Test
	public void testEstimatedCost() {
		assertEquals(node.estimatedCost(), 112.0, 0.001);
	}

	@Test
	public void testApply() {
		Action action = problem.availableActions(node.getState()).get(0);
		City city = (City) node.apply(action).getState();
		Connection c = (Connection) action;
		assertEquals(city.getName(), c.getTo());
	}

	@Test
	public void testSolution() {
		List<Node> res = node.solution();
		
		assertEquals(((AStarNode)res.get(0)).getPathCost(), 0.0, 0.001);
		assertEquals(((City)res.get(0).getState()).getName(), "Timisoara");
		assertEquals(((AStarNode)res.get(1)).getPathCost(), 111.0, 0.001);
		assertEquals(((City)res.get(1).getState()).getName(), "Lugoj");
	}

	@Test
	public void testEqualsObject() {
		assertEquals(node.getParent(), parent_node);
	}

	@Test
	public void testCompareTo() {
		assertEquals(node.getParent().compareTo(parent_node), 0);
		assertEquals(node.compareTo(parent_node), 111);
	}

}
