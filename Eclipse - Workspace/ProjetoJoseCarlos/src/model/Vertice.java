package model;

public class Vertice {
	
	// Atributos
	private int cor, identificador, prioridade, ordemListaPriori;
	
	// Métodos Construtores
	public Vertice(){
		this(-1, -1);
	}
	
	public Vertice(int identificador){
		this(-1, identificador);
	}
	
	public Vertice(int cor, int identificador){
		setCor(cor);
		setIdentificador(identificador);
	}

	// Métodos Gets e Sets
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

	public int getOrdemListaPriori() {
		return ordemListaPriori;
	}

	public void setOrdemListaPriori(int ordemListaPriori) {
		this.ordemListaPriori = ordemListaPriori;
	}
}
