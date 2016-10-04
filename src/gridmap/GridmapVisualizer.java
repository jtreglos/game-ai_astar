package gridmap;

import java.util.*;

import graph.*;
import processing.core.*;
import solver.*;

public class GridmapVisualizer implements Visualizer {
	private GridmapProblem problem;
	private Graph<GridmapState> world;
	private GridmapPApplet p;
	private List<Connection> displayed_connections = new ArrayList<Connection>();
	
	public GridmapVisualizer(GridmapPApplet p, AStarProblem problem) {
		this.p = p;
		this.problem = (GridmapProblem)problem;
		this.world = this.problem.getWorld();
	}
	
	private boolean mouseInRestartButton() {
		return p.mouseX >= p.width - 110 && p.mouseX <= p.width - 10 && p.mouseY >= 10 && p.mouseY <= 50;
	}
	
	private boolean mouseInResetButton() {
		return p.mouseX >= p.width - 110 && p.mouseX <= p.width - 10 && p.mouseY >= 70 && p.mouseY <= 110;
	}
	
	public void mouseClicked() {
		if (!problem.isReadyToBeSolved()) {
			Set<GridmapState> rooms = world.getVertices();

			for(GridmapState r : rooms) {
				long rx = toX(r.getX());
				long ry = toY(r.getY());

				if (d(rx, ry, p.mouseX, p.mouseY) < 10) {
					if (problem.initialState() == null) {
						problem.setInitialState(r);
					} else {
						problem.setGoalState(r);
					}
				}
			}
		}
		
		if (mouseInRestartButton()) {
			problem.initFromString(GridmapProblem.DEFAULT_EDGES);
			p.clearSolution();
		} else if (mouseInResetButton()) {
			problem.setInitialState(null);
			problem.setGoalState(null);
			p.clearSolution();
		}
	}
	
	private double d(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private long toX(double x) {
		return Math.round(p.width * x / (problem.getMaxX() + 2));
	}
	
	private long toY(double y) {
		return p.height - Math.round(p.height * y / (problem.getMaxY() + 2));
	}
	
	public void displayRoom(GridmapState room, boolean is_path) {
		PFont f = p.createFont("HelveticaNeue-Bold", 16, true);
		
		long x = toX(room.getX());
		long y = toY(room.getY());
		
		if (is_path) {
			p.fill(0, 255, 0);
			p.stroke(0, 128, 0);
		} else {
			p.fill(180);
			p.stroke(80);
		}
		
		p.strokeWeight(2.0f);
		p.ellipse(x, y, 10, 10);
		
		if (is_path) {
			p.fill(0, 128, 0);
		} else {
			p.fill(200);
		}
		p.textFont(f);
		p.textAlign(PApplet.CENTER);
		p.text(room.getName(), x+15, y);
	}
	
	public void displayConnection(GraphEdge<GridmapState> connection, boolean is_followed) {
		PFont f = p.createFont("HelveticaNeue-LightItalic", 14, true);
		boolean is_both_ways = false;
		boolean is_displayed = true;
		
		if (problem.getWorld().hasEdge(connection.getTo(), connection.getFrom())) {
			is_both_ways = true;
			if (connection.getTo().getX() > connection.getFrom().getX()) {
				is_displayed = false;
			}
		}
		
		if (is_displayed || is_followed) {
			double x1 = toX(connection.getFrom().getX());
			double y1 = toY(connection.getFrom().getY());
			
			double x2 = toX(connection.getTo().getX());
			double y2 = toY(connection.getTo().getY());
			
			if (is_followed) {
				p.stroke(0, 128, 0);
			} else {
				if (is_both_ways) {
					p.stroke(0, 0, 128);
				} else {
					p.stroke(128, 0, 0);
				}
			}
			
			if (is_both_ways) {
				p.strokeWeight(6);
			} else {
				p.strokeWeight(2);
			}
			p.line((float)x1, (float)y1, (float)x2, (float)y2);
			
			if (!is_both_ways) {
				double norm = problem.dist(x1, y1, x2, y2);
				double dx = (x1 - x2) / norm;
				double dy = (y1 - y2) / norm;
				
				p.line((float)x2, (float)y2, (float)(x2 + 10 * dx - 5 * dy), (float)(y2 + 10 * dy + 5 * dx));
				p.line((float)x2, (float)y2, (float)(x2 + 10 * dx + 5 * dy), (float)(y2 + 10 * dy - 5 * dx));
			}
			
			double center_x = (x1 + x2) / 2.0;
			double center_y = (y1 + y2) / 2.0;
			
			if (is_followed) {
				p.fill(255);
			} else {
				p.fill(150);
			}
			
			p.textFont(f);
			p.textAlign(PApplet.CENTER);
			p.text("" + String.format("%.2f", connection.getWeight()), (float)center_x, (float)center_y);
		}
	}
	
	public void displayInfo(String path, double path_cost, double estimated_cost) {
		PFont f = p.createFont("HelveticaNeue-LightItalic", 14, true);
		p.textFont(f);
		p.textAlign(PApplet.LEFT);
		p.fill(150);
		p.text("Path: " + path + "\n" + "Path cost: " + String.format("%.2f", path_cost) + "\n" + "Estimated cost: " + String.format("%.2f", estimated_cost), 20, 30);
	}
	
	public void drawRestartButton() {
		PFont f = p.createFont("HelveticaNeue-Bold", 14, true);
		
		p.noStroke();
		p.fill(180);
		p.rect(p.width - 110, 10, 100, 40);
		p.fill(80);
		p.textFont(f);
		p.textAlign(PApplet.CENTER);
		p.text("Restart", p.width - 60, 35);
	}
	
	public void drawResetButton() {
		PFont f = p.createFont("HelveticaNeue-Bold", 14, true);
		
		p.noStroke();
		p.fill(180);
		p.rect(p.width - 110, 70, 100, 40);
		p.fill(80);
		p.textFont(f);
		p.textAlign(PApplet.CENTER);
		p.text("Reset", p.width - 60, 95);
	}
	
	@Override
	public void display(AStarNode n) {
		displayed_connections.clear();
		List<String> path = new ArrayList<String>();
		Set<GraphEdge<GridmapState>> followed_connections = new HashSet<GraphEdge<GridmapState>>();
		Set<GridmapState> followed_rooms = new HashSet<GridmapState>();
		AStarNode cn = n;

		if (problem.isReadyToBeSolved()) {
			if (p.hasSolution()) {
				followed_rooms.add((GridmapState)problem.initialState());
				while(cn.getParent() != null) {
					followed_connections.add((Connection)cn.getAction());
					followed_rooms.add((GridmapState)cn.getState());
					path.add(((GridmapState)cn.getState()).getName());
					cn = cn.getParent();
				}
			} else {
				p.solveIt();
			}
		}
		
		Set<GraphEdge<GridmapState>> connections = world.getEdges();
		for(GraphEdge<GridmapState> c : connections) {
			if (!followed_connections.contains(c)) displayConnection(c, false);
		}
		for(GraphEdge<GridmapState> c : connections) {
			if (followed_connections.contains(c)) displayConnection(c, true);
		}
			
		Set<GridmapState> rooms = world.getVertices();
		for(GridmapState r : rooms) {
			displayRoom(r, followed_rooms.contains(r));
		}
		
		if (problem.isReadyToBeSolved() && n != null) {
			path.add(((GridmapState)problem.initialState()).getName());
			Collections.reverse(path);
			displayInfo(path.toString(), n.getPathCost(), n.estimatedCost());
		}
		
		drawRestartButton();
		drawResetButton();
	}
}
