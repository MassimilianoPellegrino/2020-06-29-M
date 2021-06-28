package it.polito.tdp.imdb.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model model = new Model();
		model.creaGrafo(2006);
		model.getCammino(model.getIdMap().get(16997), 4);
	}

}
