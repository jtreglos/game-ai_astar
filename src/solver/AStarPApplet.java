package solver;

import java.util.*;
import processing.core.*;

public class AStarPApplet extends PApplet {
	protected AStarProblem problem;
	protected Visualizer viz;
	private List<Node> solution = null;
	private int index = 0;
	
	public void setup() {
		frameRate(4);
	}
	
	public void settings() {
		size(1024, 768);
	}
	
	public boolean hasSolution() {
		return solution != null;
	}
	
	public void clearSolution() {
		solution = null;
		index = 0;
	}
	
	public void solveIt() {
		if (problem.isReadyToBeSolved() && !hasSolution()) {
			AStarSolver solver = new AStarSolver(problem);
			solution = solver.solver();
		}
	}

	public void draw() {
		background(100);
		
		if (solution == null) {
			viz.display(null);
		} else {
			viz.display((AStarNode)solution.get(index));
			if (index < solution.size()-1) index += 1;
		}
	}
}
