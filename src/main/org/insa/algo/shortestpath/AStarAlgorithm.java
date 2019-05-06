package org.insa.algo.shortestpath;

import org.insa.graph.*;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.*;

import java.util.*;

public class AStarAlgorithm extends DijkstraAlgorithm {

	public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        //Initialisation des variables
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        LabelStar l;
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        Node courant,successeur;
        HashMap<Node,LabelStar> map = new HashMap<Node,LabelStar>();
        double min;
        Point p1 = origin.getPoint();
        Point p2 = destination.getPoint();
        
        //Initialisation de Dijkstra
        map.put(origin, new LabelStar(origin,null,null,false,0,p1.distanceTo(p2)));
        tas.insert(map.get(origin));
        notifyOriginProcessed(origin);
        
        //Corps de Dijkstra
        boolean found = false;
        while(!tas.isEmpty()&&!found) {
        	l = (LabelStar) tas.deleteMin();
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
            			p1 = successeur.getPoint();
            			map.put(successeur, new LabelStar(successeur,null,null,false,Double.POSITIVE_INFINITY,p1.distanceTo(p2)));//heuristique
            		}
            		if(!map.get(successeur).isMarque()) {
            			min = map.get(successeur).getTotalCost();
            			if (min > (l.getTotalCost()+a.get(i).getLength())) {
            				min = l.getTotalCost()+a.get(i).getLength();
            				map.get(successeur).setTotalCost(min);
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
        if(!map.containsKey(destination)) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
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
        }
        
        return solution;
    }

}
