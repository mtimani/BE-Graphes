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
        Arc[] predecesseurs = new Arc[data.getGraph().size()]; 
        Label l;
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        List<Node> nodes_graphe = data.getGraph().getNodes();
        Node courant,successeur;
        HashMap<Node,Label> map = new HashMap<Node,Label>();
        double min;
        
        //Initialisation de Dijkstra
        map.put(origin, new Label(origin,null,null,false,0));
        tas.insert(new Label(origin,null,null,false,0));
        
        //Corps de Dijkstra
        boolean nok = false;
        while(!tas.isEmpty()&&!nok) {
        	l = tas.deleteMin();
        	l.setMarque(true);
        	courant = l.getSommet_courant();
        	if(!map.containsKey(courant)) {
    			map.put(courant, l);
    		}
        	else {
        		map.get(courant).setMarque(true);
        	}
        	if (courant == data.getDestination()) {
        		nok = true;
        	}
        	List<Arc> a = courant.getSuccessors();
        	for(int i=0;i<courant.getNumberOfSuccessors();i++) {
        		successeur = a.get(i).getDestination();
        		if (!data.isAllowed(a.get(i))) {
    				continue;
    			}
        		
        		if(!map.containsKey(successeur)) {
        			map.put(successeur, new Label(nodes_graphe.get(i),null,null,false,Double.POSITIVE_INFINITY));
        		}
        		if(!map.get(successeur).isMarque()) {
        			min = map.get(successeur).getCout();
        			if (min > (map.get(courant).getCout()+a.get(i).getLength())) {
        				min = map.get(courant).getCout()+a.get(i).getLength();
        				map.get(successeur).setCout(min);
        				map.get(successeur).setPere_n(courant);
        				map.get(successeur).setPere_a(a.get(i));
        				if (map.get(successeur).isDans_tas()) {
        					tas.remove(map.get(successeur));;
        				}
        				else {
        					map.get(successeur).setDans_tas(true);
        				}
        				tas.insert(map.get(successeur));
        				predecesseurs[a.get(i).getDestination().getId()] = a.get(i);
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
        	Arc arc = predecesseurs[data.getDestination().getId()];
        	System.out.print(map.get(data.getDestination()).getPere_n().getId()+"\n");
        	while (arc != null) {
        		arcs.add(arc);
        		arc = predecesseurs[arc.getOrigin().getId()];
        	}
        	//Collections.reverse(arcs);
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(),arcs));
        }
        
        return solution;
    }

}
