package puzzle8;

import java.util.*;

import processing.core.*;
import solver.*;

public class Puzzle8Visualizer implements Visualizer {
	private Puzzle8Problem problem;
	private Puzzle8PApplet p;
	
	public Puzzle8Visualizer(Puzzle8PApplet p, AStarProblem problem) {
		this.p = p;
		this.problem = (Puzzle8Problem)problem;
	}
	
	public void displayInfo(double path_cost, double estimated_cost) {
		PFont f = p.createFont("HelveticaNeue-LightItalic", 14, true);
		p.textFont(f);
		p.textAlign(PApplet.LEFT);
		p.fill(150);
		p.text("Path cost: " + String.format("%.2f", path_cost) + "\n" + "Estimated cost: " + String.format("%.2f", estimated_cost), 20, 30);
	}
	
	public void mouseClicked() {
		if (problem.isReadyToBeSolved()) {
			if (mouseInRestartButton()) {
				problem.clearInitialState();
				p.clearSolution();
			} else if (mouseInSolveButton() && !p.hasSolution() && !problem.isGoal(problem.initialState())) {
				p.solveIt();
			} else if (mouseInGrid()) {
				int col = 1 + ((p.mouseX - 100) / tileSize());
				int row = 1 + ((p.mouseY - 100) / tileSize());
				problem.moveTile(col, row);
			}
		}
	}
	
	private int tileSize() {
		return (int)Math.floor((Math.min(p.width, p.height) - 200.0) / problem.getSize());
	}
	
	private int getX(int index) {
		return (index % problem.getSize());
	}
	
	private int getY(int index) {
		return (index / problem.getSize());
	}
	
	private void displayTileAtPos(int tile, int pos) {
		PFont f = p.createFont("HelveticaNeue-Bold", 32, true);
		int x, y;
		
		x = 100 + getX(pos) * tileSize();
		y = 100 + getY(pos) * tileSize();
		
		if (tile == pos) {
			p.fill(220);
			p.stroke(0, 128, 0);
		} else {
			p.fill(180);
			p.stroke(120);
		}
		p.strokeWeight(2);
		p.rectMode(PApplet.CORNER);
		p.rect(x, y, tileSize(), tileSize());
		
		if (tile == pos) {
			p.fill(0);
		} else {
			p.fill(120);
		}
		p.textFont(f);
		p.textAlign(PApplet.CENTER);
		p.text(tile, x+(tileSize()/2), y+(tileSize()/2));
	}
	
	private void drawRestartButton() {
		PFont f = p.createFont("HelveticaNeue-Bold", 14, true);
		
		p.noStroke();
		p.fill(180);
		p.rect(p.width - 110, 10, 100, 40);
		p.fill(80);
		p.textFont(f);
		p.textAlign(PApplet.CENTER);
		p.text("Restart", p.width - 60, 35);
	}
	
	private void drawSolveButton() {
		if (!p.hasSolution() && !problem.isGoal(problem.initialState())) {
			PFont f = p.createFont("HelveticaNeue-Bold", 14, true);
			
			p.noStroke();
			p.fill(180);
			p.rect(p.width - 110, 70, 100, 40);
			p.fill(80);
			p.textFont(f);
			p.textAlign(PApplet.CENTER);
			p.text("Solve", p.width - 60, 95);
		}
	}
	
	private boolean mouseInRestartButton() {
		return p.mouseX >= p.width - 110 && p.mouseX <= p.width - 10 && p.mouseY >= 10 && p.mouseY <= 50;
	}
	
	private boolean mouseInSolveButton() {
		return p.mouseX >= p.width - 110 && p.mouseX <= p.width - 10 && p.mouseY >= 70 && p.mouseY <= 110;
	}
	
	private boolean mouseInGrid() {
		int grid_size = problem.getSize() * tileSize();
		return p.mouseX >= 100 && p.mouseX <= 100 + grid_size && p.mouseY >= 100 && p.mouseY <= 100 + grid_size;
	}
	
	@Override
	public void display(AStarNode n) {
		if (problem.isReadyToBeSolved()) {
			if (p.hasSolution()) {
				Puzzle8State state = (Puzzle8State)n.getState();
				List<Integer> tiles = state.getTiles();
				
				for(int i=0; i<tiles.size(); i++) {
					if (tiles.get(i) != null) {
						displayTileAtPos(tiles.get(i), i);
					}
				}
				
				displayInfo(n.getPathCost(), n.estimatedCost());
			} else {
				Puzzle8State state = (Puzzle8State)problem.initialState();
				List<Integer> tiles = state.getTiles();
				
				for(int i=0; i<tiles.size(); i++) {
					if (tiles.get(i) != null) {
						displayTileAtPos(tiles.get(i), i);
					}
				}
				
				drawSolveButton();
			}
		} else {
			problem.initWith((Puzzle8State)problem.restartState());
		}
		
		drawRestartButton();
	}
}
