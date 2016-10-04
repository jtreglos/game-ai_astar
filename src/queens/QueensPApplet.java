package queens;

import solver.*;

public class QueensPApplet extends AStarPApplet {
	
	public void setup() {
		problem = new QueensProblem(16).randomize();
		viz = new QueensVisualizer(this, problem);
		
		super.setup();
		
		frameRate(2);
	}
	
	public void mouseClicked() {
		((QueensVisualizer)viz).mouseClicked();
	}
}
