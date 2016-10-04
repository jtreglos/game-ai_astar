package solver;

import java.util.*;
import route.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AStarSolverTest {
	private AStarSolver solver;
	private AStarProblem problem;

	@Before
	public void setUp() throws Exception {
		problem = new RouteProblem("Timisoara", "Neamt", RouteProblem.DEFAULT_EDGES);
		solver = new AStarSolver(problem);
	}

	@Test
	public void testSolver() {
		List<Node> solution = solver.solver();
		Node result = solution.get(solution.size() - 1);
		
		assertTrue(problem.isGoal(result.getState()));
	}
}
