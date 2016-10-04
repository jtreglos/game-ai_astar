package sudoku;

import java.util.*;

public class SudokuGrid {
	private List<Integer> original_tiles, current_tiles;
	private List<SudokuAction> actions = new ArrayList<SudokuAction>();
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31;1m";
	public static final String ANSI_CYAN_BOLD = "\u001B[36;1m";
	
	// Source of grids: http://kjell.haxx.se/sudoku/
	public static final List<Integer> EASY_GRID = Arrays.asList(
		0,0,7,1,0,8,0,0,0,
		0,6,0,0,0,4,0,0,0,
		0,8,9,7,5,0,3,0,0,
		3,0,1,0,0,0,7,0,0,
		0,7,0,3,0,9,0,5,0,
		0,0,2,0,0,0,4,0,1,
		0,0,4,0,9,7,6,1,0,
		0,0,0,2,0,0,0,7,0,
		0,0,0,4,0,3,8,0,0
	);
	public static final List<Integer> MEDIUM_GRID = Arrays.asList(
		4,0,8,0,6,0,9,0,2,
		0,6,9,0,4,8,7,5,0,
		7,2,0,1,0,3,0,0,0,
		5,0,0,0,0,2,4,3,8,
		6,0,0,0,0,4,2,0,1,
		2,4,7,3,8,1,0,9,5,
		0,0,0,0,0,7,0,8,0,
		0,1,6,5,2,0,3,0,0,
		3,0,4,0,1,0,5,0,9
	);
	public static final List<Integer> MEDIUM_GRID2 = Arrays.asList( // Seed: 271453535-v3-33-L1
		4,0,0,2,0,0,0,0,7,
		8,0,2,1,0,0,0,6,0,
		0,0,9,3,8,4,0,2,0,
		0,4,0,0,0,3,2,7,0,
		0,9,0,0,0,7,0,0,6,
		0,8,0,0,1,6,0,0,0,
		6,1,5,0,0,0,0,3,0,
		0,0,0,5,0,2,0,0,9,
		9,0,0,6,3,0,0,0,5
	);
	public static final List<Integer> HARD_GRID = Arrays.asList(
		7,0,0,0,4,0,6,0,0,
		2,9,6,0,0,0,4,0,3,
		0,0,3,0,0,0,1,0,8,
		0,0,0,5,0,0,0,2,6,
		0,0,0,0,0,0,8,0,0,
		0,5,0,0,0,4,0,0,0,
		1,0,0,2,0,0,3,0,0,
		0,0,0,7,6,0,0,0,0,
		0,3,0,0,0,8,5,0,7
	);
	public static final List<Integer> HARD_GRID2 = Arrays.asList(
		0,0,0,0,6,1,0,2,0,
		0,0,0,7,0,9,5,0,0,
		0,0,0,0,0,0,6,0,7,
		0,3,1,5,0,0,0,0,4,
		0,7,0,0,1,0,0,6,0,
		9,0,0,0,0,4,2,5,0,
		6,0,9,0,0,0,0,0,0,
		0,0,3,6,0,7,0,0,0,
		0,5,0,9,2,0,0,0,0
	);
	public static final List<Integer> DIFFICULT_GRID = Arrays.asList(
		0,0,0,0,0,5,3,0,2,
		0,0,0,3,0,0,0,0,4,
		0,8,0,0,2,0,0,0,5,
		0,2,0,0,3,8,0,0,0,
		0,3,7,0,0,0,4,1,0,
		0,0,0,7,9,0,0,2,0,
		1,0,0,0,7,0,0,6,0,
		3,0,0,0,0,6,0,0,0,
		5,0,2,4,0,0,0,0,0
	);
	public static final List<Integer> VERY_HARD_GRID = Arrays.asList( // Seed: 657251834-v3-17-L1
		0,0,0,2,0,0,8,0,0,
		9,0,4,0,0,0,0,0,0,
		6,5,0,7,0,0,0,0,0,
		0,0,0,0,6,9,0,0,0,
		0,0,0,0,0,4,0,0,0,
		0,0,7,0,0,0,1,0,0,
		0,2,0,0,0,0,0,6,0,
		0,0,0,0,0,0,0,9,3,
		0,0,0,5,0,0,0,0,0
	);
	public static final List<Integer> HARDEST_GRID = Arrays.asList( // Seed: 990464114-v3-17-L5
		0,0,2,0,4,0,0,0,0,
		1,0,0,0,9,0,0,0,2,
		0,0,0,3,0,0,0,5,0,
		2,0,0,0,0,0,0,4,0,
		0,0,0,0,0,5,0,7,0,
		0,0,0,7,0,8,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,5,7,0,0,0,0,0,0,
		0,0,0,0,0,0,6,0,1
	);
	
	public static final List<Integer> EVIL_GRID = Arrays.asList(
		0,0,4,0,0,0,5,0,0,
		0,7,0,5,0,6,0,4,0,
		6,0,0,0,1,0,0,0,2,
		0,1,0,3,0,9,0,5,0,
		0,0,9,0,0,0,2,0,0,
		0,8,0,2,0,7,0,9,0,
		5,0,0,0,2,0,0,0,9,
		0,4,0,9,0,3,0,2,0,
		0,0,7,0,0,0,6,0,0
	);
	public static final List<Integer> EXCESSIVE_GRID = Arrays.asList(
		4,0,0,0,8,0,0,0,1,
		0,0,7,3,0,0,8,0,0,
		0,5,0,0,4,0,0,7,0,
		0,0,0,0,0,0,0,2,0,
		5,0,2,0,3,0,6,0,7,
		0,7,0,0,0,0,0,0,0,
		0,2,0,0,6,0,0,4,0,
		0,0,4,0,0,1,7,0,0,
		8,0,0,0,5,0,0,0,9
	);
	public static final List<Integer> EGREGIOUS_GRID = Arrays.asList(
		1,0,6,3,0,0,9,0,7,
		0,8,0,0,2,0,0,4,0,
		0,0,0,0,0,0,0,0,0,
		8,0,0,0,0,0,5,0,0,
		0,2,0,0,5,0,0,3,0,
		0,0,7,0,0,0,0,0,9,
		0,0,0,0,0,0,0,0,0,
		0,4,0,0,8,0,0,7,0,
		9,0,5,0,0,7,1,0,2
	);
	public static final List<Integer> EXCRUCIATING_GRID = Arrays.asList(
		0,0,5,0,0,3,0,0,6,
		0,4,0,0,5,0,0,9,0,
		2,0,0,7,0,0,3,0,0,
		5,0,0,3,0,0,1,0,0,
		0,2,0,0,6,0,0,8,0,
		0,0,9,0,0,8,0,0,2,
		0,0,6,0,0,7,0,0,9,
		0,7,0,0,1,0,0,3,0,
		1,0,0,9,0,0,5,0,0
	);
	public static final List<Integer> EXTREME_GRID = Arrays.asList(
		0,0,0,0,0,0,0,0,0,
		0,3,0,0,8,0,0,7,0,
		9,0,6,3,0,0,2,0,1,
		0,0,3,0,0,0,0,0,8,
		0,5,0,0,7,0,0,2,0,
		6,0,0,0,0,0,1,0,0,
		2,0,1,0,0,6,9,0,3,
		0,4,0,0,9,0,0,5,0,
		0,0,0,0,0,0,0,0,0
	);
	public static final List<Integer> HARDEST_GRID_EVER = Arrays.asList(
		8,0,0,0,0,0,0,0,0,
		0,0,3,6,0,0,0,0,0,
		0,7,0,0,9,0,2,0,0,
		0,5,0,0,0,7,0,0,0,
		0,0,0,0,4,5,7,0,0,
		0,0,0,1,0,0,0,3,0,
		0,0,1,0,0,0,0,6,8,
		0,0,8,5,0,0,0,1,0,
		0,9,0,0,0,0,4,0,0
	);
	public static final List<Integer> CHEAT_GRID = Arrays.asList(
		0,0,0,0,8,7,6,0,0,
		0,0,9,0,0,4,0,5,0,
		0,4,0,0,0,5,1,0,0,
		0,0,4,0,0,0,0,3,0,
		0,2,0,9,0,6,0,8,0,
		0,7,0,0,0,0,4,0,0,
		0,0,2,5,0,0,0,7,0,
		0,1,0,4,0,0,2,0,0,
		0,0,5,6,2,0,0,0,0
	);
	
	public SudokuGrid(List<Integer> tiles) {
		this.original_tiles = new ArrayList<Integer>(tiles);
		current_tiles = new ArrayList<Integer>(original_tiles);
	}
	
	public SudokuGrid(SudokuGrid state) {
		original_tiles = new ArrayList<Integer>(state.original_tiles);
		current_tiles = new ArrayList<Integer>(state.current_tiles);
	}
	
	private int toIndex(int x, int y) {
		return (y - 1) * 9 + x - 1;
	}
	
	private int xFromIndex(int index) {
		return (index % 9) + 1;
	}
	
	private int yFromIndex(int index) {
		return (index / 9) + 1;
	}
	
	private int getSquareOrigin(int x, int y) {
		int sx = 3 * ((x - 1) / 3) + 1;
		int sy = 3 * ((y - 1) / 3) + 1;
		
		return toIndex(sx, sy);
	}
	
	private List<Integer> getEmptyTiles() {
		List<Integer> empty_tiles = new ArrayList<Integer>();
		int val;
		
		for (int index=0; index<81; index++) {
			val = getValue(index);
			if (val == 0) {
				empty_tiles.add(index);
			}
		}
		
		return empty_tiles;
	}
	
	private boolean isEmpty(int x, int y) {
		return isEmpty(toIndex(x, y));
	}
	
	private boolean isEmpty(int index) {
		return current_tiles.get(index) == 0;
	}
	
	private int getValue(int x, int y) {
		return getValue(toIndex(x, y));
	}
	
	private int getValue(int index) {
		return current_tiles.get(index);
	}
	
	private boolean isFixedValue(int x, int y) {
		return isFixedValue(toIndex(x, y));
	}
	
	private boolean isFixedValue(int index) {
		return original_tiles.get(index) != 0;
	}
	
	private boolean isTileValid(int x, int y) {
		if (isEmpty(x, y)) {
			return false;
		}
		
		int val = getValue(x, y);
		// Line
		for (int dx=1; dx<=9; dx++) {
			if (dx != x) {
				if (getValue(dx, y) == val) {
					return false;
				}
			}
		}
		
		// Col
		for (int dy=1; dy<=9; dy++) {
			if (dy != y) {
				if (getValue(x, dy) == val) {
					return false;
				}
			}
		}
		
		// Square
		int sq_index = getSquareOrigin(x, y);
		int sx = xFromIndex(sq_index);
		int sy = yFromIndex(sq_index);
		
		for (int dx=sx; dx<sx+3; dx++) {
			for (int dy=sy; dy<sy+3; dy++) {
				if ((dy != y || dx != x)) {
					if (getValue(dx, dy) == val) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private void applyAction(SudokuAction action) {
		int index = toIndex(action.getX(), action.getY());
		current_tiles.set(index, action.getValue());
	}
	
	private boolean isTileValid(int index) {
		int x = xFromIndex(index);
		int y = yFromIndex(index);
		
		return isTileValid(x, y);
	}
	
	public boolean backTracker() {
		int tile;
		List<Integer> empty_tiles = getEmptyTiles();
		
		if (empty_tiles.isEmpty()) {
			return true;
		}
		
		tile = empty_tiles.get(0);
		
		for(int v=1; v<=9; v++) {
			current_tiles.set(tile, v);
			if (isTileValid(tile)) {
				if (backTracker()) {
					actions.add(new SudokuAction(xFromIndex(tile), yFromIndex(tile), v));
					return true;
				} else {
					current_tiles.set(tile, 0);
				}
			} else {
				current_tiles.set(tile, 0);
			}
		}
		
		return false;
	}
	
	public int nbSteps() {
		return actions.size();
	}
	
	public void replay() {
		SudokuGrid grid = new SudokuGrid(original_tiles);
		Collections.reverse(actions);
		
		if (!actions.isEmpty()) {
			System.out.println(grid);
			
			for(SudokuAction action : actions) {
				grid.applyAction(action);
				System.out.println(grid.display(action));
			}
		}
	}
	
	public String display(SudokuAction action) {
		int val;
		List<String> line = new ArrayList<String>();
		String ret = "-------------------------\n";
		
		for(int y=1; y<=9; y++) {
			line.clear();
			line.add("|");
			for(int x=1; x<=9; x++) {
				val = getValue(x, y);
				if (val == 0) {
					line.add(" ");
				} else {
					String term_string = System.getenv("TERM");
					if (term_string != null && (term_string.equals("xterm-256color") || term_string.equals("xterm"))) {
						if (isFixedValue(x, y)) {
							line.add(ANSI_RED+val+ANSI_RESET);
						} else {
							if (action != null && x == action.getX() && y == action.getY()) {
								line.add(ANSI_CYAN_BOLD+val+ANSI_RESET);
							} else {
								line.add(""+val);
							}
						}
					} else {
						line.add(""+val);
					}
				}
				if (x % 3 == 0) {
					line.add("|");
				}
			}
			
			ret += String.join(" ", line);
			ret += "\n";
			
			if (y % 3 == 0) {
				ret += "-------------------------\n";
			}
		}
		
		return ret;
	}
	
	@Override
	public String toString() {
		return display(null);
	}
}
