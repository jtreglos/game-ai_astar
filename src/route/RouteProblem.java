package route;

import java.util.*;

import graph.*;
import solver.*;

public class RouteProblem implements AStarProblem {
	private City initial_state = null;
	private City goal_state = null;
	private Graph<String> world = new Graph<String>();
	public static final String[] DEFAULT_EDGES = {
		"Oradea<>Sibiu:151",
		"Oradea<>Zerind:71",
		"Zerind<>Arad:75",
		"Arad<>Timisoara:118",
		"Arad<>Sibiu:140",
		"Timisoara<>Lugoj:111",
		"Lugoj<>Mehadia:70",
		"Mehadia<>Drobeta:75",
		"Drobeta<>Craiova:120",
		"Craiova<>Rimnicu Vilcea:146",
		"Craiova<>Pitesti:138",
		"Sibiu<>Fagaras:99",
		"Rimnicu Vilcea<>Sibiu:80",
		"Fagaras<>Bucharest:211",
		"Rimnicu Vilcea<>Pitesti:97",
		"Pitesti<>Bucharest:101",
		"Bucharest<>Giurgiu:90",
		"Bucharest<>Urziceni:85",
		"Urziceni<>Hirsova:98",
		"Hirsova<>Eforie:86",
		"Urziceni<>Vaslui:142",
		"Vaslui<>Iasi:92",
		"Iasi<>Neamt:87"
	};
	
	public RouteProblem(String[] edges) {
		initFromString(edges);
	}
	
	public RouteProblem(String start_city, String end_city, String[] edges) {
		this.initial_state = new City(start_city);
		this.goal_state = new City(end_city);
		
		initFromString(edges);
		
		if (!world.hasVertex(initial_state.getName()) || !world.hasVertex(goal_state.getName())) {
			if (!world.hasVertex(initial_state.getName())) System.out.println("Error : Initial city " + initial_state + " doesn't exist in world!");
			if (!world.hasVertex(goal_state.getName())) System.out.println("Error : Goal city " + goal_state + " doesn't exist in world!");
			
			System.exit(1);
		}
	}
	
	public boolean isReadyToBeSolved() {
		return initial_state != null && goal_state != null;
	}
	
	public void initFromString(String[] edges) {
		for(String edge : edges) {
			if (edge.matches("^[a-zA-Z0-9-_ \\p{L}]+(>>|<>)[a-zA-Z0-9-_ \\p{L}]+:[0-9]+$")) {
				String[] elmts = edge.split(":");
				String[] sub_elmts;
				if (elmts[0].contains(">>")) { // Directed edge
					sub_elmts = elmts[0].split(">>");
					world.addDirectedEdge(sub_elmts[0], sub_elmts[1], new Integer(elmts[1]));
				} else if (elmts[0].contains("<>")) { // Un-directed edge
					sub_elmts = elmts[0].split("<>");
					world.addUndirectedEdge(sub_elmts[0], sub_elmts[1], new Integer(elmts[1]));
				}
			}
		}
	}

	@Override
	public State initialState() {
		return initial_state;
	}
	
	public void setInitialState(City initial_state) {
		this.initial_state = initial_state;
	}
	
	public State goalState() {
		return goal_state;
	}
	
	public void setGoalState(City goal_state) {
		this.goal_state = goal_state;
	}

	@Override
	public boolean isGoal(State state) {
		return state.equals(goal_state);
	}

	@Override
	public List<Action> availableActions(State s) {
		if (s instanceof City) {
			City city = (City) s;
			Set<GraphEdge<String>> connections = world.getConnections(city.getName());
			List<Action> actions = new ArrayList<Action>();
			for(GraphEdge<String> c : connections) {
				actions.add(new Connection(c));
			}
			
			return actions;
		} else {
			return null;
		}
	}

	@Override
	public State applyAction(State s, Action a) {
		if (s instanceof City && a instanceof Connection) {
			City city = (City) s;
			Connection action = (Connection) a;
			List<Action> actions = availableActions(city);
			if (actions.contains(action)) {
				return new City(action.getTo());
			}
		}
		
		return s;
	}

	@Override
	public double stepCost(State s, Action a) {
		if (a instanceof Connection) {
			Connection action = (Connection) a;
			
			return action.getWeight();
		}
		
		return 0;
	}

	@Override
	public double heuristic(State s) {
		if (s instanceof City) {
			City city = (City) s;
			if (city == goal_state) {
				return 0;
			}
		}
		
		return 1;
	}
	
}
