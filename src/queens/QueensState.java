package queens;

import java.util.*;

import solver.*;

public class QueensState implements State {
	private int size;
	public List<Integer> columns;
	
	public QueensState(QueensState state) {
		this.size = state.getSize();
		this.columns = new ArrayList<Integer>(state.columns);
	}
	
	public QueensState(int size) {
		this.size = size;
		columns = new ArrayList<Integer>();
		for(int i=0; i<size; i++) {
			columns.add(0);
		}
	}
	
	public QueensState(int size, int default_value) {
		this.size = size;
		columns = new ArrayList<Integer>();
		for(int i=0; i<size; i++) {
			columns.add(default_value);
		}
	}
	
	public QueensState(List<Integer> init) {
		size = init.size();
		columns = init;
	}
	
	public QueensState randomize() {
		Random rand = new Random();
		
		for (int i=0; i<size; i++) {
			columns.set(i, rand.nextInt(size));
		}
		
		return this;
	}
	
	public int getSize() {
		return size;
	}
	
	public List<Integer> getColumns() {
		return columns;
	}
	
	public boolean hasQueen(int col, int row) {
		return columns.get(col) == row;
	}
	
	public boolean isOk(int col) {
		int col_val = columns.get(col);
		if (col_val >= 0 & col_val < size) {
			return nbKos(col) == 0;
		} else {
			return false;
		}
	}
	
	public int nbKos(int col) {
		int nb_kos = 0;
		
		for(int i=0; i<size; i++) {
			if (i != col) {
				// Check lines
				if (columns.get(i) == columns.get(col)) {
					nb_kos += 1;
				}
				
				// Check diagonal top-left to bottom-right
				int val = columns.get(col) + (i - col);
				if (val >= 0 && val < size && columns.get(i) == val) {
					nb_kos += 1;
				}
				
				// Check diagonal top-right to bottom-left
				val = columns.get(col) + (col - i);
				if (val >= 0 && val < size && columns.get(i) == val) {
					nb_kos += 1;
				}
			}
		}
		
		return nb_kos;
	}
	
	public boolean isCellClear(int col, int row) {
		int current_val = columns.get(col);
		boolean ret = false;
		
		columns.set(col, row);
		ret = isOk(col);
		columns.set(col, current_val);
		
		return ret;
	}
	
	public boolean isGoal() {
		for(int i=0; i<size; i++) {
			if (!isOk(i)) {
				return false;
			}
		}
		
		return true;
	}
	
	public List<Action> availableActions() {
		List<Action> ret = new ArrayList<Action>();
		
		for(int col=0; col<size; col++) {
			for(int row=0; row<size; row++) {
				if (row != columns.get(col) && isCellClear(col, row)) {
					ret.add(new QueensAction(col, row));
				}
			}
		}
		
		return ret;
	}
	
	public void applyAction(QueensAction action) {
		if (availableActions().contains(action)) {
			columns.set(action.getCol(), action.getRow());
		}
	}
	
	@Override
	public String toString() {
		int nb_chars = (size > 9 ? 2 : 1);
		String ret = "";

		ret += " " + (nb_chars == 1 ? " " : "  ") + " ";
		for(int i=0; i<size; i++) ret += " " + String.format("%"+nb_chars+"d", i) + " ";
		ret += "\n";
		
		for(int i=0; i<size; i++) {
			ret += " " + String.format("%"+nb_chars+"d", i) + " ";
			for(int j=0; j<size; j++) {
				if (columns.get(j) == i) {
					ret += " " + (nb_chars == 1 ? "" : " ") + "X ";
				} else {
					ret += " " + (nb_chars == 1 ? "_" : "__") + " ";
				}
			}
			ret += "\n";
		}
		
		return ret;
	}
}
