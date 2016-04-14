package projects.examplecode;

import java.awt.Color;

public class Cor {

	
	private int posicao;
	private Color cor;
	private boolean isUsada;
	private boolean isRemovida;

	public Cor(Color cor) {
		this.isUsada = false;
		this.cor = cor;
		this.isRemovida = false;
		this.posicao = -1;
	}
	public Cor() {
		this.isUsada = false;
		this.isRemovida = false;
		this.posicao = -1;
	}
	public Color getCor() {
		return cor;
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}

	public boolean isUsada() {
		return isUsada;
	}

	public void setUsada(boolean isUsada) {
		this.isUsada = isUsada;
	}
	public boolean isRemovida() {
		return isRemovida;
	}
	public void setRemovida(boolean isRemovida) {
		this.isRemovida = isRemovida;
	}
	public int getPosicao() {
		return posicao;
	}
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	
	
}
