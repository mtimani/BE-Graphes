package org.insa.graph;
import org.insa.algo.AbstractInputData;
import org.insa.algo.shortestpath.ShortestPathData;;

public class Label implements Comparable<Label>{

	//Déclaration de variables
	Node sommet_courant,pere_n;
	Arc pere_a;
	boolean marque;
	boolean dans_tas;
	double cout;
	Graph graphe;
	
	
	public Label(Node n, Node p, Node dest, Arc a, boolean m, double c, ShortestPathData d, Graph g) {
		this.sommet_courant = n;
		this.pere_n = p;
		this.pere_a = a;
		this.marque = m;
		this.cout = c;
		this.dans_tas = false;
		this.graphe = g;
	}
	
	
	
	public boolean isDans_tas() {
		return dans_tas;
	}



	public void setDans_tas(boolean dans_tas) {
		this.dans_tas = dans_tas;
	}



	public Node getSommet_courant() {
		return sommet_courant;
	}




	public void setSommet_courant(Node sommet_courant) {
		this.sommet_courant = sommet_courant;
	}




	public Node getPere_n() {
		return pere_n;
	}




	public void setPere_n(Node pere_n) {
		this.pere_n = pere_n;
	}




	public Arc getPere_a() {
		return pere_a;
	}




	public void setPere_a(Arc pere_a) {
		this.pere_a = pere_a;
	}




	public boolean isMarque() {
		return marque;
	}




	public void setMarque(boolean marque) {
		this.marque = marque;
	}




	public double getCout() {
		return cout;
	}


	public void setCout(double cout) {
		this.cout = cout;
	}

	public double getTotalCost() {
		return this.cout;
	}
	
	public void setTotalCost(double TotalCost) {
		this.cout = TotalCost;
	}

	public double getCost() {
		return this.cout;
	}
	
	public int compareTo(Label other) {
        return Double.compare(getTotalCost(), other.getTotalCost());
    } 
}