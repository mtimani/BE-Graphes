package org.insa.graph;

import org.insa.algo.AbstractInputData;
import org.insa.algo.shortestpath.ShortestPathData;

public class LabelStar extends Label{
	
	private double heuristique;
	Point p1,p2;
	
	public LabelStar(Node n, Node p, Node dest, Arc a, boolean m, double c, ShortestPathData d) {
		super(n,p,dest,a,m,c,d);
		p1 = n.getPoint();
		p2 = dest.getPoint();
		if (d.getMode()==AbstractInputData.Mode.TIME) {
			this.heuristique = (p1.distanceTo(p2))/((double) d.getMaximumSpeed());
		}
		else {
			this.heuristique = p1.distanceTo(p2);
		}
	}

	public double getHeuristique() {
		return heuristique;
	}

	public void setHeuristique(double heuristique) {
		this.heuristique = heuristique;
	}
	
	public double getTotalCost() {
		return this.cout+this.heuristique;
	}
	
}