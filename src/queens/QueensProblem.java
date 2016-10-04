package queens;

import java.util.*;

import solver.*;

public class QueensProblem implements AStarProblem {
	private int size = 0;
	private QueensState initial_state = null;
	
	public QueensProblem() {
		size = 8;
		initial_state = new QueensState(size);
	}
	
	public QueensProblem(int size, int default_value) {
		this.size = size;
		initial_state = new QueensState(size, default_value);
	}
	
	public QueensProblem(int size) {
		this.size = size;
		initial_state = new QueensState(size);
	}
	
	public QueensProblem(List<Integer> init) {
		initial_state = new QueensState(init);
		size = initial_state.getSize();
	}
	
	public QueensProblem initWith(List<Integer> init) {
		initial_state = new QueensState(init);
		size = initial_state.getSize();
		
		return this;
	}
	
	public QueensProblem randomize() {
		if (initial_state == null) {
			if (size == 0) {
				size = 8;
			} else {
				initial_state = new QueensState(size);
			}
		}
		
		initial_state.randomize();
		
		return this;
	}
	
	public void clearInitialState() {
		initial_state = null;
	}
	
	@Override
	public boolean isReadyToBeSolved() {
		return initial_state != null;
	}

	@Override
	public State initialState() {
		return initial_state;
	}

	@Override
	public boolean isGoal(State s) {
		if (s instanceof QueensState) {
			QueensState state = (QueensState) s;
			return state.isGoal();
		}
		
		return false;
	}

	@Override
	public List<Action> availableActions(State s) {
		if (s instanceof QueensState) {
			QueensState state = (QueensState) s;
			return state.availableActions();
		}
		
		return null;
	}

	@Override
	public State applyAction(State s, Action a) {
		if (s instanceof QueensState && a instanceof QueensAction) {
			QueensState state = new QueensState((QueensState) s);
			QueensAction action = (QueensAction) a;
			state.applyAction(action);
			
			return state;
		}
		
		return s;
	}

	@Override
	public double stepCost(State state, Action action) {
		return 1;
	}

	@Override
	public double heuristic(State s) {
		int nb_kos = 0;
		
		if (s instanceof QueensState) {
			QueensState state = (QueensState) s;
			
			for(int i=0; i<state.getSize(); i++) {
				nb_kos += state.nbKos(i);
			}
		}
		
		return nb_kos;
	}
}