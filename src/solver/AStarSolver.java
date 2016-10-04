package solver;

import java.util.*;

/**
 * A* Solver
 * @author jtreglos
 *
 */
public class AStarSolver implements Solver {
	private PriorityQueue<AStarNode> frontier;
	private AStarNode node;
	private List<State> explored;
	private AStarProblem problem;
	
	/**
	 * @param initial_state Initial State for A* Solver
	 * @see State
	 */
	public AStarSolver(AStarProblem problem) {
		this.problem = problem;
		node = new AStarNode(problem);
		frontier = new PriorityQueue<AStarNode>();
		explored = new ArrayList<State>();
		
		frontier.add(node);
	}
	
	/**
	 * A* Solver call point
	 * @return Null if no solution found, solution list of Nodes otherwise
	 * @see AStarNode
	 */
	@Override
	public List<Node> solver() {
		while(true) {
			if (frontier.isEmpty()) {
				return null;
			} else {
				node = frontier.poll();
				if (problem.isGoal(node.getState())) {
					return node.solution();
				} else {
					State node_state = node.getState();
					explored.add(node_state);
					for(Action action : problem.availableActions(node_state)) {
						AStarNode child = node.apply(action);
						boolean in_frontier = frontier.contains(child);
						if (!explored.contains(child.getState()) && !in_frontier) {
							frontier.add(child);
						} else if (in_frontier) {
							for(AStarNode n : frontier) {
								if (child.equals(n) && child.getPathCost() < n.getPathCost()) {
									frontier.remove(n);
									frontier.add(child);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
}
