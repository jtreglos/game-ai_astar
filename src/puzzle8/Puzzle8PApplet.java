package puzzle8;

import solver.*;

public class Puzzle8PApplet extends AStarPApplet {
	
	public void setup() {
		// problem = new Puzzle8Problem(Puzzle8Problem.DEFAULT_INITIAL_STATE);
		// problem = new Puzzle8Problem(4);
		problem = new Puzzle8Problem(new Puzzle8State(3).randomizeToSolvable(30));
		viz = new Puzzle8Visualizer(this, problem);
		
		super.setup();
	}
	
	public void mouseClicked() {
		((Puzzle8Visualizer)viz).mouseClicked();
	}
}
