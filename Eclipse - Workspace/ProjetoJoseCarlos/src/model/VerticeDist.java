package model;

import java.util.HashMap;

public class VerticeDist extends Vertice {
	
	// Atributos
	private double posicaoX, posicaoY;
	private HashMap<Integer, Double> vizinhos;
	
	// Métodos Construtores
	public VerticeDist(){
		this(-1, -1, -1, -1);
	}
	
	public VerticeDist(int identificador, double posicaoX, double posicaoY){
		this(identificador, posicaoX, posicaoY, -1);
	}
	
	public VerticeDist(int identificador, double posicaoX, double posicaoY, int cor){
		super(cor, identificador);
		setPosicaoX(posicaoX);
		setPosicaoY(posicaoY);
	}

	// Métodos Gets e Sets
	public double getPosicaoX() {
		return posicaoX;
	}

	public void setPosicaoX(double posicaoX) {
		this.posicaoX = posicaoX;
	}

	public double getPosicaoY() {
		return posicaoY;
	}

	public void setPosicaoY(double posicaoY) {
		this.posicaoY = posicaoY;
	}

	public HashMap<Integer, Double> getVizinhos() {
		return vizinhos;
	}

	public void setVizinhos(HashMap<Integer, Double> vizinhos) {
		this.vizinhos = vizinhos;
	}
}