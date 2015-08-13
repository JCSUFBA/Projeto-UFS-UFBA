package model;

public class Distancia {

	// Atributos
	private int vertice;
	private double distancia;
	
	// Construtor
	public Distancia(int vertice, double distancia) {
		setVertice(vertice);
		setDistancia(distancia);
	}

	// Métodos Gets e Sets
	public int getVertice() {
		return vertice;
	}

	public void setVertice(int vertice) {
		this.vertice = vertice;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
}
