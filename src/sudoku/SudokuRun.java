package sudoku;

public class SudokuRun {
	
	public static void main(String[] args) {
		long t1, t2, t3, t4;
		
		t1 = System.currentTimeMillis();
		SudokuGrid s = new SudokuGrid(SudokuGrid.CHEAT_GRID);
		t2 = System.currentTimeMillis();
		
		t3 = System.currentTimeMillis();
		if (s.backTracker()) {
			t4 = System.currentTimeMillis();
			s.replay();
			System.out.println("Grid init took " + (t2-t1) + " ms");
			System.out.println("Solved in " + s.nbSteps() + " steps and " + (t4-t3) + " ms");
		} else {
			t4 = System.currentTimeMillis();
			System.out.println("Grid init took " + (t2-t1) + " ms");
			System.out.println("Not solved. (" + (t4-t3) + " ms)");
		}
	}
}

