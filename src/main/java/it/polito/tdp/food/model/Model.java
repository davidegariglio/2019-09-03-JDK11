package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao ;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<VerticeAttraversato> bestCammino;
	
	public Model() {
		this .dao = new FoodDao();
	}
	
	/*	Per punto successivo
	 * 
	 * public List<Portion> getPortionTypes(){
		return null;
	}*/
	
	public void creaGrafo(Double c) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Ora abbiamo tutti i vertici e possiamo inserirli
		//Graphs.addAllVertices(this.grafo, this.dao.getVertici(c));
		
		for(Collegamento coll : this.dao.getArchi(c)) {
			if(!this.grafo.containsVertex(coll.getP1())) {
				this.grafo.addVertex(coll.getP1());
			}
			if(!this.grafo.containsVertex(coll.getP2())) {
				this.grafo.addVertex(coll.getP2());
			}
			if(this.grafo.getEdge(coll.getP1(), coll.getP2()) == null) {
				Graphs.addEdgeWithVertices(this.grafo, coll.getP1(), coll.getP2(), coll.getPeso());
			}
		}
		/*
		//Ora inseriamo gli archi
		for(Collegamento coll : this.dao.getArchi()) {
			//Se entrambi i vertici sono inseriti nel grafo...
			if(this.grafo.containsVertex(coll.getP1()) && this.grafo.containsVertex(coll.getP2())) {
				//Creo arco con peso
				this.grafo.addEdge(coll.getP1(), coll.getP2());
				this.grafo.setEdgeWeight(coll.getP1(), coll.getP2(), coll.getPeso());
			}
		}
		*/
		System.out.println("Grafo creato con #"+this.grafo.vertexSet().size()+" vertici e #"+this.grafo.edgeSet().size()+" archi");	
	
	}

	public Collection<String> getVertici() {
		if(this.grafo != null) {
			return this.grafo.vertexSet();
		}
		return null;
	}

	public Map<String, Double> getCorrelate(String scelta) {
		Map<String, Double> result = new HashMap<>();
		for(String s : Graphs.neighborListOf(this.grafo, scelta)) {
			result.put(s, this.grafo.getEdgeWeight(this.grafo.getEdge(scelta, s)));
		}
		return result;
	}

	public List<VerticeAttraversato> trovaCammino(String scelta, int n) {
		
		this.bestCammino = new ArrayList<>();
		List<VerticeAttraversato> parziale = new ArrayList<>(); 
		parziale.add(new VerticeAttraversato(scelta, 0));
		this.ricorsione(parziale, n);
		
		return this.bestCammino;
		
	}

	private void ricorsione(List<VerticeAttraversato> parziale, int n) {
		//Caso terminale
		//Controllo che parziale abbia dimensione n
		//Se è così vedo se è la best e in caso aggiorno
		if(parziale.size() == n+1) {
			if(calcolaPeso(parziale) > calcolaPeso(bestCammino)) {
				this.bestCammino = new ArrayList<>(parziale);
				return;
			}
			return;
		}
		//prendo l'ultimo elemento inserito in parziale e vedo i vicini
		for(String s : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1).getNome())) {

			//controllo che il vicino che sto provando ad inderire non sia già presente nella lista parziale
				
			if(!contiene(parziale, s)) {
				parziale.add(new VerticeAttraversato(s, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1).getNome(), s)))); 
				ricorsione(parziale, n);
				
				//Backtracking
				parziale.remove(parziale.size()-1);
			}	
			
		}
		
	}

	private boolean contiene(List<VerticeAttraversato> parziale, String s) {
		for(VerticeAttraversato v : parziale) {
			if(v.getNome().compareTo(s) == 0)
				return true;
		}
		return false;
	}

	public int calcolaPeso(List<VerticeAttraversato> parziale) {
		int result = 0;
		if(parziale != null) {
			for(VerticeAttraversato v : parziale) {
				result += v.getPeso();
			}
		}
		return result;
	}
	
}
