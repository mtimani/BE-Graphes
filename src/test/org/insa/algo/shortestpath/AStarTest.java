package org.insa.algo.shortestpath;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;

import org.junit.BeforeClass;
import org.junit.Test;

public class AStarTest {

	// Small graph use for tests
	private static Graph graph;

	// List of nodes
	private static Node[] nodes;

	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
	@SuppressWarnings("unused")
	private static Arc a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e;

	@BeforeClass
	public static void initAll() throws IOException {

		// Define roads
		RoadInformation RoadInfo = new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null);

		// Create nodes
		nodes = new Node[6];
		for (int i = 0; i < nodes.length; ++i) {
			nodes[i] = new Node(i, null);
		}

		// Add arcs...
		a2b = Node.linkNodes(nodes[0], nodes[1], 7, RoadInfo, null);
		a2c = Node.linkNodes(nodes[0], nodes[2], 8, RoadInfo, null);
		b2d = Node.linkNodes(nodes[1], nodes[3], 4, RoadInfo, null);
		b2e = Node.linkNodes(nodes[1], nodes[4], 1, RoadInfo, null);
		b2f = Node.linkNodes(nodes[1], nodes[5], 5, RoadInfo, null);
		c2a = Node.linkNodes(nodes[2], nodes[0], 7, RoadInfo, null);
		c2b = Node.linkNodes(nodes[2], nodes[1], 2, RoadInfo, null);
		c2f = Node.linkNodes(nodes[2], nodes[5], 2, RoadInfo, null);
		e2c = Node.linkNodes(nodes[4], nodes[2], 2, RoadInfo, null);
		e2d = Node.linkNodes(nodes[4], nodes[3], 2, RoadInfo, null);
		e2f = Node.linkNodes(nodes[4], nodes[5], 3, RoadInfo, null);
		f2e = Node.linkNodes(nodes[5], nodes[4], 3, RoadInfo, null);

		// Initialize the graph
		graph = new Graph("ID", "", Arrays.asList(nodes), null);

	}

	//@Test
	public void testDoRun() {
		System.out.println("#####-----Test de validité avec oracle sur un exemple simple-----#####");
		/* Tableau contenant les arcs*/
		//Arc[] arcs = new Arc[] { a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e };

		for (int i=0;  i < nodes.length; ++i) {

			/* Affichage du point de départ */
			System.out.print("x"+(nodes[i].getId()+1) + ":");

			for (int j=0;  j < nodes.length; ++j) {

				if(nodes[i]==nodes[j]) {
					System.out.print("     -    ");
				}
				else{

					ArcInspector arcInspectorAStar = new ArcInspectorFactory().getAllFilters().get(0);
					ShortestPathData data = new ShortestPathData(graph, nodes[i],nodes[j], arcInspectorAStar);

					BellmanFordAlgorithm B = new BellmanFordAlgorithm(data);
					AStarAlgorithm A = new AStarAlgorithm(data);

					/* Récupération des solutions de Bellman et AStar pour comparer */
					ShortestPathSolution solution = A.run();
					ShortestPathSolution expected = B.run();

					/* Pas de chemin trouvé */
					if (solution.getPath() == null) {
						assertEquals(expected.getPath(), solution.getPath());
						System.out.print("(infini)  ");
					}
					/* Un plus court chemin trouvé */
					else {

						/* Calcul du coût de la solution */
						float costSolution = solution.getPath().getLength();
						float costExpected = expected.getPath().getLength();
						assertEquals(costExpected, costSolution, 0);

						/* On récupère l'avant dernier sommet du chemin de la solution (=sommet père de la destination) */
						List<Arc> arcs = solution.getPath().getArcs();
						Node originOfLastArc = arcs.get(arcs.size()-1).getOrigin();

						/* Affiche le couple (coût, sommet père du Dest) */
						System.out.print("("+costSolution+ ", x" + (originOfLastArc.getId()+1) + ") ");
					}
					
				}

			}

			/* Retour à la ligne */ 
			System.out.println();

		}
		System.out.println();
	}

	//@Test
	public void testDoScenarioDistanceHG() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
		String mapName = "/Users/timani/Desktop/BE/haute-garonne.mapgr";
		
		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 38926;
		destination = 59015;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 59015;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 38926;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
	}

	
	//@Test
	public void testDoScenarioTempsHG() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
		String mapName = "/Users/timani/Desktop/BE/haute-garonne.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println("#####----- Mode : TEMPS ----------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 38926;
		destination = 59015;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	
	
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 59015;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 38926;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	
	}

	//@Test
	public void testDoScenarioDistanceINSA() throws Exception {

		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
		String mapName = "/Users/timani/Desktop/BE/insa.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : INSA ----------------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 300 ;
		destination = 300;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 607;
		destination = 857;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
	
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = 2000;
		destination = 857;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 607;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 2000;
		destination = 2000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   
	}

	//@Test
	public void testDoScenarioTempsINSA() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
		String mapName = "/Users/timani/Desktop/BE/insa.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : INSA ----------------------------------######");
		System.out.println("#####----- Mode : TEMPS ----------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 300 ;
		destination = 300;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 607;
		destination = 857;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	
	
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = 2000;
		destination = 857;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 607;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 2000;
		destination = 2000;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");  
	}
	
	//@Test
	public void testDoScenarioDistanceCarreDense() throws Exception {

		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
		String mapName = "/Users/timani/Desktop/BE/carre-dense.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : CARRE DENSE ---------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 0;
		destination = 204097;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    		
	}

	//@Test
	public void testDoScenarioTempsCarreDense() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
		String mapName = "/Users/timani/Desktop/BE/carre-dense.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : CARRE DENSE ---------------------------######");
		System.out.println("#####----- Mode : TEMPS -------------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin simple ------");
		origine = 0;
		destination = 204097;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   			
	}
	
	
	//@Test
	public void testDoScenarioDistanceGuadeloupe() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/guadeloupe.mapgr";
		String mapName = "/Users/timani/Desktop/BE/guadeloupe.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : GUADELOUPE ----------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
	
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 9922;
		destination = 34328;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
	
		System.out.println("----- Cas de sommets non connexes ------");
		origine = 9950;
		destination = 15860;
		startTime = System.nanoTime();
		test.testScenario(mapName, 1,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	

	}
	
	//@Test
	public void testDoScenarioTempsGuadeloupe() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/guadeloupe.mapgr";
		String mapName = "/Users/timani/Desktop/BE/guadeloupe.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : GUADELOUPE ----------------------------######");
		System.out.println("#####----- Mode : TEMPS ----------------------------------######");
		System.out.println();
	
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 9922;
		destination = 34328;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);      
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	
	
		System.out.println("----- Cas de sommets non connexes ------");
		origine = 9950;
		destination = 15860;
		startTime = System.nanoTime();
		test.testScenario(mapName, 0,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");   	

	}

	@Test
	public void testDoScenarioMinTempsDistHG() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
		String mapName = "/Users/timani/Desktop/BE/haute-garonne.mapgr";
		
		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);        
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");  
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 38926;
		destination = 59015;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");     	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 59015;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 38926;
		destination = 200000;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");     	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 200000; 
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");    
	}

	@Test
	public void testDoScenarioMinTempsDistCarreDense() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
		String mapName = "/Users/timani/Desktop/BE/carre-dense.mapgr";
		
		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : CARRE DENSE ---------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin simple ------");
		origine = 0;
		destination = 100052;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");     
	}
	
	@Test
	public void testDoScenarioMinTempsDistGuadeloupe() throws Exception {
		//String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/guadeloupe.mapgr";
		String mapName = "/Users/timani/Desktop/BE/guadeloupe.mapgr";

		AStarTestWithMap test = new  AStarTestWithMap();
		int origine;
		int destination;
		long startTime, endTime; 
		float duration;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : GUADELOUPE ----------------------------######");
		System.out.println();
	
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 9922;
		destination = 34328;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");     	
	
		System.out.println("----- Cas de sommets non connexes ------");
		origine = 9950;
		destination = 15860;
		startTime = System.nanoTime();
		test.testScenarioSansOracle(mapName,origine,destination);    
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Le temps d'execution de l'Algorithme Dijkstra est de : " + duration + " ms.\n\n");     
	}
	

}