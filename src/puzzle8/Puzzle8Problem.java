package puzzle8;

import java.util.*;
import solver.*;

public class Puzzle8Problem implements AStarProblem {
	private int size = 0;
	private Puzzle8State initial_state = null;
	private Puzzle8State restart_state= null;
	public static final List<Integer> DEFAULT_INITIAL_STATE = Arrays.asList(9,4,7,2,1,6,3,11,8,5,14,10,13,12,15,null);
	
	public Puzzle8Problem() {
	}
	
	public Puzzle8Problem(int size) {
		this.size = size;
		initial_state = new Puzzle8State(size);
		restart_state = new Puzzle8State(size);
	}
	
	public Puzzle8Problem(Puzzle8State state) {
		initial_state = state;
		restart_state = new Puzzle8State(state);
		size = state.getSize();
	}
	
	public Puzzle8Problem(List<Integer> init) {
		initial_state = new Puzzle8State(init);
		restart_state = new Puzzle8State(init);
		size = initial_state.getSize();
	}
	
	public void initWith(List<Integer> init) {
		initial_state = new Puzzle8State(init);
		size = initial_state.getSize();
	}
	
	public void initWith(Puzzle8State init) {
		initial_state = new Puzzle8State(init);
		size = initial_state.getSize();
	}
	
	@Override
	public boolean isReadyToBeSolved() {
		return initial_state != null;
	}

	@Override
	public State initialState() {
		return initial_state;
	}
	
	public State restartState() {
		return restart_state;
	}
	
	public void clearInitialState() {
		initial_state = null;
	}
	
	public int getSize() {
		return size;
	}

	@Override
	public boolean isGoal(State state) {
		return heuristic(state) == 0;
	}

	@Override
	public List<Action> availableActions(State s) {
		if (s instanceof Puzzle8State) {
			Puzzle8State state = (Puzzle8State) s;
			List<Action> ret = new ArrayList<Action>(Arrays.asList(Puzzle8Action.UP, Puzzle8Action.DOWN, Puzzle8Action.LEFT, Puzzle8Action.RIGHT));
			
			if (state.X(state.spaceIndex()) == 1) {
				ret.remove(Puzzle8Action.LEFT);
			} else if (state.X(state.spaceIndex()) == state.getSize()) {
				ret.remove(Puzzle8Action.RIGHT);
			}
			
			if (state.Y(state.spaceIndex()) == 1) {
				ret.remove(Puzzle8Action.UP);
			} else if (state.Y(state.spaceIndex()) == state.getSize()) {
				ret.remove(Puzzle8Action.DOWN);
			}
			
			return ret;
		} else {
			return null;
		}
	}

	@Override
	public State applyAction(State s, Action action) {
		if (s instanceof Puzzle8State) {
			Puzzle8State state = new Puzzle8State(((Puzzle8State) s).getTiles());
			List<Action> actions = availableActions(state);
			
			if (actions.contains(action)) {
				state.applyAction(action);
			}
			
			return state;
		} else {
			return s;
		}
	}

	@Override
	public double stepCost(State state, Action action) {
		return 1;
	}

	@Override
	public double heuristic(State s) {
		if (s instanceof Puzzle8State) {
			Puzzle8State state = (Puzzle8State) s;
			return state.heuristic();
		} else {
			return 0;
		}
	}
	
	public void moveTile(int x, int y) {
		initial_state.moveTile(x, y);
	}
	
	public Puzzle8State moveRandomlyToDepth(State state, int nb_steps) {
		Action last_action = null;
		Random rand = new Random();
		
		for(int i=0; i<nb_steps; i++) {
			List<Action> actions = availableActions(state);
			if (last_action == Puzzle8Action.UP) actions.remove(Puzzle8Action.DOWN);
			else if (last_action == Puzzle8Action.DOWN) actions.remove(Puzzle8Action.UP);
			else if (last_action == Puzzle8Action.LEFT) actions.remove(Puzzle8Action.RIGHT);
			else if (last_action == Puzzle8Action.RIGHT) actions.remove(Puzzle8Action.LEFT);
			
			Action action = actions.get(rand.nextInt(actions.size()));
			state = applyAction(state, action);
			last_action = action;
		}
		
		return (Puzzle8State) state;
	}
}