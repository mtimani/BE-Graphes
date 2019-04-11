package org.insa.algo.shortestpath;

import org.insa.graph.*;
import org.insa.algo.utils.*;

import java.util.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        //Initialisation
        ArrayList<Label> labels = new ArrayList<Label>(); 
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        
        List<Node> nodes_graphe = data.getGraph().getNodes();
        
        	//Origine
        Node courant,successeur;
        Label l;
        HashMap<Node,Label> map = new HashMap<Node,Label>();
        double min;
        	//Le reste
        /*for (int i=0;i<nodes_graphe.size();i++) {
        	if (nodes_graphe.get(i) != origin) {
        		labels.add(i,new Label(nodes_graphe.get(i),null,null,false,Double.POSITIVE_INFINITY));
        	}
        	else {
        		labels.add(i,new Label(origin,null,null,false,0));
        		tas.insert(labels.get(0));
        	}
        }*/
        map.put(origin, new Label(origin,null,null,false,0));
        tas.insert(new Label(origin,null,null,false,0));
        
        int cpt_marque = 0;
        while(cpt_marque < nodes_graphe.size()) {
        	tas.findMin().setMarque(true);
        	courant = tas.findMin().getSommet_courant();
        	List<Arc> a = courant.getSuccessors();
        	for(int i=0;i<courant.getNumberOfSuccessors();i++) {
        		successeur = a.get(i).getDestination();
        		
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
        				tas.insert(map.get(successeur));
        			}
        		}
        	}
        }
        
        return solution;
    }

}
