package gridmap;

import solver.*;

public class GridmapPApplet extends AStarPApplet {
	public void setup() {
		problem = new GridmapProblem(GridmapProblem.DEFAULT_EDGES);
		// problem = new GridmapProblem().initRandom(30);
		viz = new GridmapVisualizer(this, problem);
		
		super.setup();
	}
	
	public void mouseClicked() {
		((GridmapVisualizer)viz).mouseClicked();
	}
}
