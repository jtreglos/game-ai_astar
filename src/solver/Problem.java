package solver;

public interface Problem {
	public State initialState();
	public boolean isGoal(State state);
	public boolean isReadyToBeSolved();
}
