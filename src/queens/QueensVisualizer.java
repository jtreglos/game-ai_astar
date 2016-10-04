package queens;

import processing.core.*;
import solver.*;

public class QueensVisualizer implements Visualizer {
	private QueensProblem problem;
	private QueensPApplet p;
	private int size;
	
	public QueensVisualizer(QueensPApplet p, AStarProblem problem) {
		this.p = p;
		this.problem = (QueensProblem)problem;
	}
	
	public void mouseClicked() {
		if (problem.isReadyToBeSolved()) {
			if (p.mouseX >= p.width - 110 && p.mouseX <= p.width - 10 && p.mouseY >= 10 && p.mouseY <= 50) {
				problem.clearInitialState();
				p.clearSolution();
			}
		}
	}

	public void displayInfo(double path_cost, double estimated_cost) {
		PFont f = p.createFont("HelveticaNeue-LightItalic", 14, true);
		p.textFont(f);
		p.textAlign(PApplet.LEFT);
		p.fill(150);
		p.text("Path cost: " + String.format("%.2f", path_cost) + "\n" + "Estimated cost: " + String.format("%.2f", estimated_cost), 20, 30);
	}

	private void displayTile(int col, int row, boolean has_queen, boolean is_ok, boolean is_tile_clear) {
		int tile_size = (int)Math.floor((Math.min(p.width, p.height) - 60.0) / size);
		int x, y;
		
		x = 60 + col * tile_size;
		y = 60 + row * tile_size;
		
		if (is_ok) {
			if (col % 2 == row % 2) {
				p.fill(0, 120, 0);
			} else {
				p.fill(0, 180, 0);
			}
			p.stroke(0, 220, 0);
		} else if (is_tile_clear) {
			if (col % 2 == row % 2) {
				p.fill(120);
			} else {
				p.fill(180);
			}
			p.stroke(220);
		} else {
			if (col % 2 == row % 2) {
				p.fill(120, 0, 0);
			} else {
				p.fill(180, 0, 0);
			}
			p.stroke(220, 0, 0);
		}
		
		p.strokeWeight(2);
		p.rectMode(PApplet.CORNER);
		p.rect(x, y, tile_size, tile_size);
		
		if (has_queen) {
			if (is_ok) {
				p.fill(0, 30, 0);
			} else {
				p.fill(220);
			}
			
		int font_size = (int)(tile_size * 0.8);
		PFont f = p.createFont("HelveticaNeue-Bold", font_size, true);
		p.textFont(f);
		p.textAlign(PApplet.CENTER);
		p.text("♛", x+(tile_size/2), y+(font_size/3)+(tile_size/2));
		}
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
	
	@Override
	public void display(AStarNode n) {
		if (problem.isReadyToBeSolved()) {
			if (p.hasSolution()) {
				QueensState state = (QueensState)n.getState();
				size = state.getSize();
				
				for(int col=0; col<size; col++) {
					boolean col_is_ok = state.isOk(col);
					
					for(int row=0; row<size; row++) {
						boolean tile_has_queen = state.hasQueen(col, row);
						displayTile(col, row, tile_has_queen, col_is_ok && tile_has_queen, state.isCellClear(col, row));
					}
				}
				
				displayInfo(n.getPathCost(), n.estimatedCost());
			} else {
				p.solveIt();
			}
		} else {
			problem.randomize();
		}
		
		drawRestartButton();
	}

}
