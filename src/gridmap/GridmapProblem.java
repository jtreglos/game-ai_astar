package gridmap;

import java.util.*;
import java.util.regex.*;

import graph.*;
import solver.*;

public class GridmapProblem implements AStarProblem {
	private GridmapState initial_state = null;
	private GridmapState goal_state = null;
	private Graph<GridmapState> world = new Graph<GridmapState>();
	private double max_x = 0, max_y = 0;
	private Random rand = new Random();
	public static final String[] DEFAULT_EDGES = {
		"Westcliffe(14.5,15)<>Gasquet(14.5,13):1",
		"Gasquet<>Blindvaagen(11.5,10.5):4",
		"Gasquet<>Al Khamrah(13.5,10.5):4",
		"Gasquet>>Collevecchio(16.5,10.5):1",
		"Al Khamrah>>Blindvaagen:1",
		"Blindvaagen<>Takayashiki(11,7.5):1",
		"Al Khamrah<>Collevecchio:1",
		"Mihambwe(13.5,2.5)>>Al Khamrah:3",
		"Collevecchio<>Naucelle(19.5,10):1",
		"Takayashiki>>North Escobares(7.5,13):5",
		"Takayashiki<>Rockport(7,6):2",
		"Mihambwe<>Rockport:3",
		"Rockport<>Kitadai(3.5,6):1",
		"Kitadai<>Kleppestø(1.5,11):2",
		"Kleppestø<>North Escobares:2",
		"North Escobares<>Crustumerium(8.5,16.5):1",
		"Crustumerium<>Lang Duoc(14,17):1",
		"Lang Duoc<>Colognac(11.5,14.5):1",
		// "Westcliffe<>Colognac:8",
		"Lang Duoc<>Troisfontaines(19,16):1"
	};
	
	public GridmapProblem() {
	}
	
	public GridmapProblem(String[] edges) {
		initFromString(edges);
	}
	
	public GridmapProblem(String start_room, String end_room, String[] edges) {
		initFromString(edges);
		
		this.initial_state = getRoom(start_room);
		if (this.initial_state == null) System.out.println("Error : Initial room " + start_room + " doesn't exist in world!");
		
		this.goal_state = getRoom(end_room);
		if (this.goal_state == null) System.out.println("Error : Goal room " + end_room + " doesn't exist in world!");
		
		if (this.initial_state == null || this.goal_state == null) System.exit(1);
	}
	
	public boolean isReadyToBeSolved() {
		return initial_state != null && goal_state != null;
	}
	
	private boolean linesIntersect(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		double s1_x = x1 - x0;
		double s1_y = y1 - y0;
		double s2_x = x3 - x2;
		double s2_y = y3 - y2;
		double s = (-s1_y * (x0 - x2) + s1_x * (y0 - y2)) / (-s2_x * s1_y + s1_x * s2_y);
		double t = ( s2_x * (y0 - y2) - s2_y * (x0 - x2)) / (-s2_x * s1_y + s1_x * s2_y);
		
		return s > 0 && s < 1 && t > 0 && t < 1;
	}
	
	public double getMaxX() {
		return max_x;
	}
	
	public double getMaxY() {
		return max_y;
	}
	
	private double dist(GridmapState c0, GridmapState c1) {
		return dist(c0.getX(), c0.getY(), c1.getX(), c1.getY());
	}
	
	private double dist(double x, double y, GridmapState c) {
		return dist(c.getX(), c.getY(), x, y);
	}
	
	public double dist(double x0, double y0, double x1, double y1) {
		return Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
	}
	
	private boolean isSettlable(double x, double y, List<GridmapState> cities, GridmapState city) {
		if (x <= 0 || y <= 0) {
			return false;
		}
		
		for(GridmapState c : cities) {
			if (dist(x, y, c) < 25) {
				return false;
			}
		}
		
		Set<GraphEdge<GridmapState>> edges = world.getEdges();
		for(GraphEdge<GridmapState> edge : edges) {
			if (linesIntersect(x, y, city.getX(), city.getY(), edge.getFrom().getX(), edge.getFrom().getY(), edge.getTo().getX(), edge.getTo().getY())) {
				return false;
			}
		}
		
		return true;
	}
	
	public GridmapProblem initRandom(int nb) {
		initial_state = null;
		goal_state = null;
		max_x = 0;
		max_y = 0;
		world.clear();
		
		List<String> names = new ArrayList<String>();
		List<GridmapState> cities = new ArrayList<GridmapState>();
		GridmapState city, new_city;
		double angle, dist, x=0, y=0;
		String name;
		
		names.clear();
		cities.clear();
		
		for(int i=0; i<nb; i++) {
			do {
				name = GridmapState.randomName();
			} while (names.contains(name));
			names.add(name);
		}
		
		cities.add(new GridmapState(names.get(0), 10, 10));
		
		for(int i=1; i<nb; i++) {
			city = cities.get(rand.nextInt(cities.size()));
			int nb_attempts = 0;
			do {
				angle = rand.nextDouble() * Math.PI;
				dist = rand.nextDouble() * 200 + 20;
				x = (max_x / 2) + dist * Math.cos(angle);
				y = (max_y / 2) + dist * Math.sin(angle);
				nb_attempts += 1;
			} while(nb_attempts < 1000 && !isSettlable(x, y, cities, city));
			if (nb_attempts >= 1000) {
				i -= 1;
			} else {
				new_city = new GridmapState(names.get(i), x, y);
				max_x = Math.max(max_x, x);
				max_y = Math.max(max_y, y);
				cities.add(new_city);
				int mod = rand.nextInt(5) % 5;
				if (mod == 0) {
					world.addDirectedEdge(city, new_city, dist(city, new_city));
				} else if (mod == 1) {
					world.addDirectedEdge(new_city, city, dist(city, new_city));
				} else {
					world.addUndirectedEdge(city, new_city, dist(city, new_city));
				}
			}
		}
		
		for(int i=0; i<=0; i++) {
			city = cities.get(rand.nextInt(cities.size()));
			do {
				new_city = cities.get(rand.nextInt(cities.size()));
			} while (new_city == city);
			if (rand.nextInt(5) % 5 == 0) {
				world.addDirectedEdge(city, new_city, dist(city, new_city));
			} else {
				world.addUndirectedEdge(city, new_city, dist(city, new_city));
			}
		}
		
		return this;
	}
	
	public void initFromString(String[] edges) {
		Pattern init_pattern = Pattern.compile("^([a-zA-Z0-9-_ \\p{L}]+)(\\((\\d+.*\\d*),(\\d+.*\\d*)\\)){0,1}(>>|<>)([a-zA-Z0-9-_ \\p{L}]+)(\\((\\d+.*\\d*),(\\d+.*\\d*)\\)){0,1}:([0-9]+)$");
		// Groups : 1 = from.name ; 2 = from.coordinates ; 3 = from.x ; 4 = from.y ; 5 = type of connection ; 6 = to.name ; 7 = to.coordinates ; 8 = to.x ; 9 = to.y ; 10 = connection weight
		for(String edge : edges) {
			Matcher m = init_pattern.matcher(edge);
			if (m.matches()) {
				GridmapState from_room, to_room;
				
				if (getRoom(m.group(1)) == null) {
					max_x = Math.max(max_x, new Double(m.group(3)));
					max_y = Math.max(max_y, new Double(m.group(4)));
					from_room = new GridmapState(m.group(1), new Double(m.group(3)), new Double(m.group(4))); 
				} else {
					from_room = getRoom(m.group(1));
				}

				if (getRoom(m.group(6)) == null) {
					max_x = Math.max(max_x, new Double(m.group(8)));
					max_y = Math.max(max_y, new Double(m.group(9)));
					to_room = new GridmapState(m.group(6), new Double(m.group(8)), new Double(m.group(9))); 
				} else {
					to_room = getRoom(m.group(6));
				}
				
				Integer weight = new Integer(m.group(10));
				if (m.group(5).equals(">>")) { // Directed edge
					world.addDirectedEdge(from_room, to_room, weight);
				} else if (m.group(5).equals("<>")) { // Un-directed edge
					world.addUndirectedEdge(from_room, to_room, weight);
				}
			}
		}
	}

	@Override
	public State initialState() {
		return initial_state;
	}
	
	public void setInitialState(GridmapState initial_state) {
		this.initial_state = initial_state;
	}
	
	public State goalState() {
		return goal_state;
	}
	
	public void setGoalState(GridmapState goal_state) {
		this.goal_state = goal_state;
	}

	@Override
	public boolean isGoal(State state) {
		return state.equals(goal_state);
	}

	@Override
	public List<Action> availableActions(State s) {
		if (s instanceof GridmapState) {
			GridmapState room = (GridmapState) s;
			Set<GraphEdge<GridmapState>> connections = world.getConnections(room);
			List<Action> actions = new ArrayList<Action>();
			for(GraphEdge<GridmapState> c : connections) {
				actions.add(new Connection(c));
			}
			
			return actions;
		} else {
			return null;
		}
	}

	@Override
	public State applyAction(State s, Action a) {
		if (s instanceof GridmapState && a instanceof Connection) {
			GridmapState room = (GridmapState) s;
			Connection action = (Connection) a;
			List<Action> actions = availableActions(room);
			if (actions.contains(action)) {
				return new GridmapState(action.getTo());
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
		if (s instanceof GridmapState) {
			GridmapState room = (GridmapState) s;
			if (room == goal_state) {
				return 0;
			} else {
				return room.distanceTo(goal_state) / 4.0;
				// return 1;
			}
		}
		
		return 999999;
	}
	
	public GridmapState getRoom(String room_name) {
		Set<GridmapState> rooms = world.getVertices();
		for(GridmapState r : rooms) {
			if (r.getName().equals(room_name)) {
				return r;
			}
		}
		
		return null;
	}
	
	public Graph<GridmapState> getWorld() {
		return world;
	}
}
