package model;

public class VerticeCent extends Vertice {
	
	// Atributos
	private int posicaoX, posicaoY;
	
	// M�todos Construtores
	public VerticeCent(){
		this(-1, -1, -1);
	}
	
	public VerticeCent(int identificador, int posicaoX, int posicaoY){
		this(identificador, posicaoX, posicaoY, -1);
	}
	
	public VerticeCent(int identificador, int posicaoX, int posicaoY, int cor){
		super(cor, identificador);
		setPosicaoX(posicaoX);
		setPosicaoY(posicaoY);
	}

	// M�todos Gets e Sets
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
}
