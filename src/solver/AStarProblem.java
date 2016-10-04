package solver;

import java.util.List;

public interface AStarProblem extends Problem {
	public List<Action> availableActions(State state);
	public State applyAction(State state, Action action);
	public double stepCost(State state, Action action);
	public double heuristic(State state);
}
