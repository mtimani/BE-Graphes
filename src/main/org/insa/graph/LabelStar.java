package org.insa.graph;

public class LabelStar extends Label{
	
	private double heuristique;
	
	public LabelStar(Node n, Node p, Arc a, boolean m, double c, double h) {
		super(n,p,a,m,c);
		this.heuristique = h;
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