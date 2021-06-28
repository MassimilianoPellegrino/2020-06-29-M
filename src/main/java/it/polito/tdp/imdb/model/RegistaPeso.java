package it.polito.tdp.imdb.model;

public class RegistaPeso {
	
	private Director d1;
	private Director d2;
	private double w;
	public RegistaPeso(Director d1, Director d2, double w) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.w = w;
	}
	public Director getD1() {
		return d1;
	}
	public Director getD2() {
		return d2;
	}
	public double getW() {
		return w;
	}
	
	

}
