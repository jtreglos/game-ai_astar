package puzzle8;

import java.util.*;
import solver.*;

public class Puzzle8State implements State {
	private int size;
	private List<Integer> tiles;
	private double heuristic = 1;
	
	public Puzzle8State(Puzzle8State state) {
		this.size = state.getSize();
		tiles = new ArrayList<Integer>(state.getTiles());
		heuristic = calcHeuristic();
	}
	
	public Puzzle8State(int size) {
		this.size = size;
		tiles = new ArrayList<Integer>();
		
		tiles.add(null);
		for(int i=1; i<(size*size); i++) {
			tiles.add(i);
		}
		
		heuristic = calcHeuristic();
	}
	
	public Puzzle8State(List<Integer> seq) {
		size = (int) Math.sqrt(seq.size());
		tiles = new ArrayList<Integer>(seq);
		heuristic = calcHeuristic();
	}
	
	public double calcHeuristic() {
		return h2();
		// return Math.max(Math.max(h1(), h2()), h3());
	}
	
	public double heuristic() {
		return heuristic;
	}
	
	public int getSize() {
		return size;
	}
	
	private int toIndex(int x, int y) {
		return (y - 1) * size + x - 1;
	}
	
	public void moveTile(int x, int y) {
		int i = toIndex(x, y);
		Action action = null;
		
		if (x != size && tiles.get(i+1) == null)
			action = Puzzle8Action.LEFT;
		else if (x != 1 && tiles.get(i-1) == null)
			action = Puzzle8Action.RIGHT;
		else if (y != size && tiles.get(i+size) == null)
			action = Puzzle8Action.UP;
		else if (y != 1 && tiles.get(i-size) == null)
			action = Puzzle8Action.DOWN;
		
		if (action != null) {
			applyAction(action);
		}
	}
	
	public List<Integer> getTiles() {
		return tiles;
	}
	
	private void randomize() {
		Collections.shuffle(tiles);
	}
	
	public Puzzle8State randomizeToSolvable(Integer heuristic_limit) {
		if (heuristic_limit == null) {
			do {
				randomize();
			} while(!isSolvable());
		} else {
			do {
				randomize();
			} while(!(isSolvable() && h2() < heuristic_limit));
		}
		
		heuristic = calcHeuristic();
		
		return this;
	}
	
	private List<Integer> linearize() {
		return new ArrayList<Integer>(tiles);
	}
	
	public int spaceIndex() {
		return tiles.indexOf(null);
	}
	
	public int X(int index) {
		return (index % size) + 1;
	}
	
	public int Y(int index) {
		return (index / size) + 1;
	}
	
	private int nbInversions() {
		int nb_elmts = tiles.size();
		int nb_inv = 0;
		
		for (int k=0; k<size*size; k++) {
			if (tiles.get(k) != null) {
				int v = tiles.get(k);
				List<Integer> sub_lin = tiles.subList(k, nb_elmts);
				for(Integer vv : sub_lin) {
					if (vv != null && vv < v) {
						nb_inv += 1;
					}
				}
			}
		}
		
		return nb_inv;
	}
		
	public boolean isSolvable() {
		int nb_inv = nbInversions();
		
		if (size % 2 != 0) {
			return nb_inv % 2 == 0;
		} else if (Y(spaceIndex()) % 2 != 0) {
			return nb_inv % 2 != 0;
		} else {
			return nb_inv % 2 == 0;
		}
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		for(int i=0; i<size*size; i++) {
			Integer val = tiles.get(i);
			if (val == null) {
				ret += " -- ";
			} else {
				ret += String.format(" %2d ", val);
			}
						
			if (X(i) == size) {
				ret += "\n";
			}
		}
		
		return ret;
	}
	
	public void applyAction(Action action) {
		int space_index = spaceIndex();
		
		if (action == Puzzle8Action.UP) {
			tiles.set(space_index, tiles.get(space_index - size));
			tiles.set(space_index - size, null);
		} else if (action == Puzzle8Action.DOWN) {
			tiles.set(space_index, tiles.get(space_index + size));
			tiles.set(space_index + size, null);
		} else if (action == Puzzle8Action.LEFT) {
			tiles.set(space_index, tiles.get(space_index - 1));
			tiles.set(space_index - 1, null);
		} else if (action == Puzzle8Action.RIGHT) {
			tiles.set(space_index, tiles.get(space_index + 1));
			tiles.set(space_index + 1, null);
		}
		
		heuristic = calcHeuristic();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Puzzle8State) {
			Puzzle8State s = (Puzzle8State) o;
			return size == s.size && tiles.hashCode() == s.tiles.hashCode();
		} else {
			return super.equals(o);
		}
	}
	
	public int h1() {
		int total = 0;
		
		for(int i=0; i<size*size; i++) {
			Integer val = tiles.get(i);
			if (val != null) {
				if (val != i) {
					total += 1;
				}
			}
		}
		
		return total;
	}
		
	public int h2() {
		int total = 0;
		
		for(int i=0; i<size*size; i++) {
			Integer val = tiles.get(i);
			if (val != null) {
				if (val != i) {
					total += Math.abs(X(val) - X(i)) + Math.abs(Y(val) - Y(i));
				}
			}
		}
		
		return total;
	}
	
	public int h3() {
		int total = 0;
		List<Integer> lin = linearize();
		List<Integer> obj = new ArrayList<Integer>();
		
		obj.add(null);
		for(int i=1; i<(size * size); i++) {
			obj.add(i);
		}
		
		while (!lin.equals(obj)) {
			if (lin.get(0) == null) { // Blank in goal position
				// Find first mismatched tile
				int mismatch_pos = 0;
				for(int i=1; i<lin.size(); i++) {
					if (lin.get(i) != i) {
						mismatch_pos = i;
						break;
					}
				}
				
				lin.set(0, lin.get(mismatch_pos));
				lin.set(mismatch_pos, null);
			} else {
				int space_index = lin.indexOf(null);
				int tile_index = lin.indexOf(space_index);
				lin.set(space_index, space_index);
				lin.set(tile_index, null);
			}
			
			total += 1;
		}
		
		
		return total;
	}
}
