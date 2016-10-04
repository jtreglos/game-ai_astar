package route;

import graph.*;
import solver.*;

public class Connection extends GraphEdge<String> implements Action {
	public Connection(GraphEdge<String> connection) {
		this.from = connection.getFrom();
		this.to = connection.getTo();
		this.weight = connection.getWeight();
	}
}
