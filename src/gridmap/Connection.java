package gridmap;

import graph.*;
import solver.*;

public class Connection extends GraphEdge<GridmapState> implements Action {
	public Connection(GraphEdge<GridmapState> connection) {
		this.from = connection.getFrom();
		this.to = connection.getTo();
		this.weight = connection.getWeight();
	}
}
