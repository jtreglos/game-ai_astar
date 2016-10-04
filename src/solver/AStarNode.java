package solver;

import java.util.*;

/**
 * Node class for AStar solver
 * @author jtreglos
 *
 */
public class AStarNode implements Comparable<AStarNode>, Node {
	private State state = null;
	private double path_cost = 0;
	private AStarNode parent = null;
	private Action action = null;
	private AStarProblem problem = null;
	private double heuristic = 1;
	
	/**
	 * @param state State of the new Node
	 * @param path_cost Path cost of the new Node
	 * @param parent Parent Node from which this Node was created
	 * @param action Action that generated this new node
	 * @see State
	 */
	public AStarNode(AStarProblem problem, State state, double path_cost, AStarNode parent, Action action) {
		this.problem = problem;
		this.state = state;
		this.path_cost = path_cost;
		this.action = action;
		this.parent = parent;
		this.heuristic = problem.heuristic(state);
	}
	
	public AStarNode(AStarProblem problem) {
		this.problem = problem;
		state = problem.initialState();
		this.heuristic = problem.heuristic(state);
	}
	
	/**
	 * @return State of Node
	 * @see State
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * @return Path cost of Node
	 */
	public double getPathCost() {
		return path_cost;
	}
	
	/**
	 * @return Parent Node of current Node
	 */
	public AStarNode getParent() {
		return parent;
	}
	
	/**
	 * @return Action of Node
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Calculates the estimated cost for the Node : f(n) = g(n) + h(n) 
	 * @return The estimated cost for the Node
	 */
	public double estimatedCost() {
		return path_cost + heuristic;
	}
	
	/**
	 * Creates a new child Node by applying the Action action to the current Node
	 * @param action Applied Action
	 * @return The child Node
	 */
	public AStarNode apply(Action action) {
		return new AStarNode(problem, problem.applyAction(state, action), path_cost + problem.stepCost(state, action), this, action);
	}
	
	/**
	 * Returns the solution path from this Node to the top of the chain
	 * @return An array of all Nodes in the current solution
	 */
	public List<Node> solution() {
		List<Node> ret = new ArrayList<Node>();
		
		if (parent != null) {
			ret = parent.solution();
		}
		
		ret.add(this);
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String state_str = state.toString();
		if (state_str.contains("\n")) {
			return state + "\n" + "pc=" + path_cost;
		} else {
			// return state + " / " + path_cost;
			return state + " / " + String.format("%.2f", estimatedCost());
		}
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AStarNode))
			return false;
		AStarNode other = (AStarNode) obj;
		return this.state.equals(other.state);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AStarNode node) {
	if (node == null) {
			return 0;
		} else {
			return (int) (this.estimatedCost() - node.estimatedCost());
		}
	}	
}
