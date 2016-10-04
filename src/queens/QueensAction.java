package queens;

import solver.Action;

public class QueensAction implements Action {
	private int col, row;
	
	public QueensAction(int col, int row) {
		this.col = col;
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	@Override
	public String toString() {
		return "(" + col + ", " + row + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof QueensAction) {
			QueensAction action = (QueensAction) o;
			return col == action.getCol() && row == action.getRow();
		}
		
		return false;
	}
}
