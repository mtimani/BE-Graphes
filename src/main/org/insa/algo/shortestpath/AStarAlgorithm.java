package org.insa.algo.shortestpath;

import org.insa.graph.*;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.*;

import java.util.*;

public class AStarAlgorithm extends DijkstraAlgorithm {
	
	protected int nbSommets;
	protected int nbSommetsVisites;

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.nbSommetsVisites = 0;
    }
    
    public LabelStar newLabel(Node n, Node p, Node dest, Arc a, boolean m, double c, ShortestPathData d, Graph g) {
    	return new LabelStar(n,p,dest,a,m,c,d,g);
    }

}