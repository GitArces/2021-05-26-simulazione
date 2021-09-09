package it.polito.tdp.yelp.model;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph grafo;
	private List<Business> vertici;
	private Map<String, Business> verticiIdMap;
	
	public List<String> getAllCities() {
		YelpDao dao = new YelpDao();
			return dao.getAllCities();
	}
	
	public String creaGrafo(String city, Year anno) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		YelpDao dao = new YelpDao();
		this.vertici = dao.getBusinessByCityAndYear(city, anno);
		
		this.verticiIdMap = new HashMap<>();
		for (Business b : this.vertici)
			this.verticiIdMap.put(b.getBusinessId(), b);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		
		// Creo gli archi
		
//		for (Business b1: this.vertici) {
//			for (Business b2: this.vertici) {
//				if (b1.getMediaRecensioni() < b2.getMediaRecensioni()) {
//					Graphs.addEdge(this.grafo, b1, b2, b2.getMediaRecensioni()-b1.getMediaRecensioni);
//				}
//			}
//		}
		
		
		List<ArcoGrafo> archi = dao.calcolaArchi(city, anno);
		for (ArcoGrafo arco : archi) {
			Graphs.addEdge(this.grafo, 
					this.verticiIdMap.get(arco.getBusinessId1()),
					this.verticiIdMap.get(arco.getBusinessId2()), 
					arco.getPeso());
			
		}
		
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
}
