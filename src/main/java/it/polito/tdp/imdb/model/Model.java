package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.Arco;
import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director, DefaultWeightedEdge> grafo;
	Map<Integer, Director> idMap;
	ImdbDAO dao = new ImdbDAO();
	List<Director> bestSol;
	int bestSum;

	public void creaGrafo(int anno) {
		idMap = new HashMap<>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getYearDirectors(idMap, anno));
		List<Arco> archi = dao.getEdges(anno);
		for(Arco a: archi)
			if(grafo.containsVertex(idMap.get(a.getIdD1())) && grafo.containsVertex(idMap.get(a.getIdD2())))
				Graphs.addEdgeWithVertices(grafo, idMap.get(a.getIdD1()), idMap.get(a.getIdD2()), a.getW());
		
		//System.out.println(grafo.vertexSet().size()+" "+grafo.edgeSet().size());
		
	}

	public Graph<Director, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<RegistaPeso> getAdiacenti(Director dir) {
		
		Set<DefaultWeightedEdge> edges = grafo.edgesOf(dir);
		
		List<DefaultWeightedEdge> list = new ArrayList<>();
		
		List<RegistaPeso> res = new ArrayList<>();
		
		for(DefaultWeightedEdge edge: edges)
			list.add(edge);
		
		list.sort(new Comparator<DefaultWeightedEdge>() {

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				// TODO Auto-generated method stub
				return (int) (grafo.getEdgeWeight(o2)-grafo.getEdgeWeight(o1));
			}
		});
		
		for(DefaultWeightedEdge edge: list)
			res.add(new RegistaPeso(grafo.getEdgeSource(edge), grafo.getEdgeTarget(edge), grafo.getEdgeWeight(edge)));
		
		
		return res;
	}
	
	public List<Director> getCammino(Director dir, int c){
		
		bestSol = new LinkedList<>();
		bestSum = 0;
		
		LinkedList<Director> parSol = new LinkedList<>();
		int parSum = 0;
		int lastW = 0;
		
		parSol.add(dir);
		
		itera(parSol, parSum, c, lastW);
		
		return bestSol;
		
	}

	private void itera(LinkedList<Director> parSol, int parSum, int c, int lastW) {
		
		System.out.println(parSol.size()+" "+parSum);
		
		
		if(parSum>c) {
			if((parSol.size()-1)>bestSol.size()) {
				parSol.remove(parSol.getLast());
				System.out.println("ee");
				bestSol = new LinkedList<>(parSol);
				bestSum = parSum-lastW;
			}
			
			return;
		}
		
		if(Graphs.successorListOf(grafo, parSol.getLast()).isEmpty()) {
			//System.out.println("ee");
			
			if(parSol.size()>bestSol.size()) {
				bestSol = new LinkedList<>(parSol);
				bestSum = parSum;
			}
			
			return;
		}
			
		
		for(Director d: Graphs.successorListOf(grafo, parSol.getLast())) {
			
			if(parSol.contains(d))
				continue;
			
			lastW = (int) grafo.getEdgeWeight(grafo.getEdge(parSol.getLast(), d));
			
			
			parSol.add(d);
			parSum += lastW;
			
			
			itera(parSol, parSum, c, lastW);
			
			parSum -= lastW;
			parSol.remove(d);
		}
		
		
	}

	public int getBestSum() {
		return bestSum;
	}

	public Map<Integer, Director> getIdMap() {
		return idMap;
	}
	
	

	
}
