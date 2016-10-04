package route;

import java.util.*;
import solver.*;

public class RouteRun {
	
	public static void main(String[] args) {
		long t1, t2;
		
		t1 = System.currentTimeMillis();
		RouteProblem problem = new RouteProblem("Timisoara", "Neamt", RouteProblem.DEFAULT_EDGES);
		t2 = System.currentTimeMillis();
		
		System.out.println("Initial state generation : " + (t2 - t1) + " ms");
		
		AStarSolver solver = new AStarSolver(problem);
		List<Node> solution = solver.solver();
		if (solution == null) {
			System.out.println("No route to go from " + problem.initialState() + " to " + problem.goalState() + "!");
		} else {
			t1 = System.currentTimeMillis();
			
			System.out.println("Solver : " + (t1 - t2) + " ms");
			
			String ret = "";
			for(int i=0; i<solution.size(); i++) {
				AStarNode n = (AStarNode)solution.get(i);
				if (i != 0) ret += " -> ";
				ret += n.getState();
				if (i != 0) ret +=  " (" + n.getPathCost() + " km)";
			}
			System.out.println(ret);
			System.out.println("Solved in " + (solution.size()-1) + " steps!");
		}
	}

}
