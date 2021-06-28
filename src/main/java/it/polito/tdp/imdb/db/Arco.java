package it.polito.tdp.imdb.db;

public class Arco {
	
	private int idD1;
	private int idD2;
	private int w;
	public Arco(int idD1, int idD2, int w) {
		super();
		this.idD1 = idD1;
		this.idD2 = idD2;
		this.w = w;
	}
	public int getIdD1() {
		return idD1;
	}
	public int getIdD2() {
		return idD2;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idD1;
		result = prime * result + idD2;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		if (idD1 != other.idD1)
			return false;
		if (idD2 != other.idD2)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Arco [idD1=" + idD1 + ", idD2=" + idD2 + ", w=" + w + "]";
	}
	
	
}
