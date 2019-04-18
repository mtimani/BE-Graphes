package org.insa.algo.shortestpath;

import org.insa.graph.*;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.*;

import java.util.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	protected int nbSommets;
	protected int nbSommetsVisites;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.nbSommetsVisites = 0;
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        //Initialisation des variables
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        Label l;
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        Node courant,successeur;
        HashMap<Node,Label> map = new HashMap<Node,Label>();
        double min;
        
        //Initialisation de Dijkstra
        map.put(origin, new Label(origin,null,null,false,0));
        tas.insert(map.get(origin));
        notifyOriginProcessed(origin);
        
        //Corps de Dijkstra
        boolean found = false;
        while(!tas.isEmpty()&&!found) {
        	l = tas.deleteMin();
        	l.setMarque(true);
        	courant = l.getSommet_courant();
        	notifyNodeMarked(courant);
        	if (courant == data.getDestination()) {
        		found = true;
        	}
        	else {
        		List<Arc> a = courant.getSuccessors();
            	for(int i=0;i<courant.getNumberOfSuccessors();i++) {
            		successeur = a.get(i).getDestination();
            		notifyNodeReached(successeur);
            		if (!data.isAllowed(a.get(i))) {
        				continue;
        			}
            		
            		if(!map.containsKey(successeur)) {
            			map.put(successeur, new Label(successeur,null,null,false,Double.POSITIVE_INFINITY));
            		}
            		if(!map.get(successeur).isMarque()) {
            			min = map.get(successeur).getCout();
            			if (min > (l.getCout()+a.get(i).getLength())) {
            				min = l.getCout()+a.get(i).getLength();
            				map.get(successeur).setCout(min);
            				map.get(successeur).setPere_n(courant);
            				map.get(successeur).setPere_a(a.get(i));
            				if (map.get(successeur).isDans_tas()) {
            					tas.remove(map.get(successeur));
            				}
            				map.get(successeur).setDans_tas(true);
            				tas.insert(map.get(successeur));
            			}
            		}
            	}	
        	}
        }
        if(map.get(destination).getPere_n() == null) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	notifyDestinationReached(data.getDestination());
        	ArrayList<Arc> arcs = new ArrayList<>();
        	Arc arc = map.get(data.getDestination()).getPere_a();
        	successeur = map.get(data.getDestination()).getPere_n();
        	while (arc != null) {
        		arcs.add(arc);
        		arc = map.get(successeur).getPere_a();
        		successeur = map.get(successeur).getPere_n();
        	}
        	Collections.reverse(arcs);
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(),arcs));
        }
        
        return solution;
    }

}
