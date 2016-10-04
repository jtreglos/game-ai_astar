package graph;

public class GraphEdge<V> {
	protected V from, to;
	protected double weight;
	
	public GraphEdge() {
		this.from = null;
		this.to = null;
		this.weight = 0;
	}
	
	public GraphEdge(V from, V to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public V getFrom() {
		return from;
	}
	
	public V getTo() {
		return to;
	}
	
	public double getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return "(" + from.toString() + " -- " + String.format("%.2f", weight) + " --> " + to.toString() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 33;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GraphEdge))
			return false;
		@SuppressWarnings("unchecked")
		GraphEdge<V> other = (GraphEdge<V>) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}
}
