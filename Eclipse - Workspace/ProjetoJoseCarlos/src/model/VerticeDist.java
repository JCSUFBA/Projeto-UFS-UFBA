package model;

import java.util.ArrayList;

public class VerticeDist extends Vertice {
	
	// Atributos
	private double posicaoX, posicaoY;
	private ArrayList<Distancia> distancias;
	
	// M�todos Construtores
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

	// M�todos Gets e Sets
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

	public ArrayList<Distancia> getDistancias() {
		return distancias;
	}

	public void setDistancias(ArrayList<Distancia> distancias) {
		this.distancias = distancias;
	}
}