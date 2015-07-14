package br.ufs.dsi.redes_sensores.project;

public class Vertice {
	
	// Atributos
	private int cor, identificador, prioridade, posicaoX, posicaoY, ordemListaPriori;
	
	// M�todos Construtores
	public Vertice(){
		this(-1, -1, -1, -1, -1);
	}
	
	public Vertice(int identificador, int posicaoX, int posicaoY){
		this(-1, identificador, -1, posicaoX, posicaoY);
	}
	
	public Vertice(int cor, int identificador, int posicaoX, int posicaoY){
		this(cor, identificador, -1, posicaoX, posicaoY);
	}
	
	public Vertice(int cor, int identificador, int prioridade, int posicaoX, int posicaoY){
		setCor(cor);
		setIdentificador(identificador);
		setPrioridade(prioridade);
		setPosicaoX(posicaoX);
		setPosicaoY(posicaoY);
	}

	// M�todos Gets e Sets
	public int getCor() {
		return cor;
	}

	public void setCor(int cor) {
		this.cor = cor;
	}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public int getPosicaoX() {
		return posicaoX;
	}

	public void setPosicaoX(int posicaoX) {
		this.posicaoX = posicaoX;
	}

	public int getPosicaoY() {
		return posicaoY;
	}

	public void setPosicaoY(int posicaoY) {
		this.posicaoY = posicaoY;
	}

	public int getOrdemListaPriori() {
		return ordemListaPriori;
	}

	public void setOrdemListaPriori(int ordemListaPriori) {
		this.ordemListaPriori = ordemListaPriori;
	}
}
