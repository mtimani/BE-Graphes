package org.insa.graph;

import org.insa.algo.AbstractInputData;
import org.insa.algo.shortestpath.ShortestPathData;

public class LabelStar extends Label{
	
	private double heuristique = Double.POSITIVE_INFINITY;
	Point p1,p2;
	
	public LabelStar(Node n, Node p, Node dest, Arc a, boolean m, double c, ShortestPathData d, Graph g) {
		super(n,p,dest,a,m,c,d,g);
		p1 = n.getPoint();
		p2 = dest.getPoint();
		if (d.getMode()==AbstractInputData.Mode.TIME) {
			int vitesse = Math.max(g.getGraphInformation().getMaximumSpeed(),d.getMaximumSpeed());
			this.heuristique = Point.distance(p1, p2)/((double) vitesse/3.6);
		}
		else {
			this.heuristique = Point.distance(p1, p2);
		}
	}

	public double getHeuristique() {
		return heuristique;
	}

	public void setHeuristique(double heuristique) {
		this.heuristique = heuristique;
	}
	
	@Override
	public double getTotalCost() {
		return this.cout+this.heuristique;
	}
	
}