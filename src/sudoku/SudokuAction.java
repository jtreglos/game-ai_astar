package sudoku;

import solver.*;

public class SudokuAction implements Action {
	private int x;
	private int y;
	private int value;
	
	public SudokuAction(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public SudokuAction(SudokuAction action) {
		this(action.x, action.y, action.value);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+") -> "+value+"\n";
	}
}
