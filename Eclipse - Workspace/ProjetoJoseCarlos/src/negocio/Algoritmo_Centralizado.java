package negocio;

import java.util.ArrayList;
import java.util.HashMap;
import model.VerticeCent;
import utils.Utilidade;

public class Algoritmo_Centralizado {
	
	// Constantes
	private static int QUANT_MAX_NIVEL = 3;

	// Vari�veis
	private VerticeCent[][] grid;
	private int dimensaoGrid;
	private int areaTotalGrid;
	private double ladoQuadrado;
	private double alcanceSensor;
	private double distanciaEntreVertices;
	private int numParcialVertices;
	private int numTotalVertices;
	private boolean comDilution;
	private boolean eMetodoVetor;
	private int quantCoresUtilizadas;
	
	// Construtor
	public Algoritmo_Centralizado(double ladoQuadrado, int dimensaoGrid, boolean comDilution, boolean eMetodoVetor){
		setDimensaoGrid(dimensaoGrid);
		setLadoQuadrado(ladoQuadrado);
		setComDilution(comDilution);
		setEMetodoVetor(eMetodoVetor);

		calculaValoresSemComDilution();

		grid = new VerticeCent[numParcialVertices][numParcialVertices];
	}
	
	// M�todo para inicializar vari�veis sem / com Dilution
	private void calculaValoresSemComDilution(){
		areaTotalGrid = (int) Math.pow(dimensaoGrid, 2);
		
		if(isComDilution())
			alcanceSensor = ladoQuadrado * Math.sqrt(2);
		else
			alcanceSensor = ladoQuadrado;
		
		distanciaEntreVertices = ladoQuadrado;
		numParcialVertices = (int) Math.round((dimensaoGrid / distanciaEntreVertices));
		numTotalVertices = (int) Math.pow(numParcialVertices, 2);
	}
	
	/**
	 * M�todo para iniciar o grid com os v�rtices, bem como determina a prioridade de cada v�rtice.
	 */
	public void inicializaVertices(){
		int id = 1;
		for(int linha = 0; linha < grid.length; linha++)
			for(int coluna = 0; coluna < grid.length; coluna++)
				grid[linha][coluna] = new VerticeCent(id++, linha, coluna);
		
		// Se n�o for M�todo Vetor, calcula-se as prioridades dos v�rtices
		if(!isMetodoVetor())
			determinaPrioridade();
	}
	
	/**
	 * M�todo que determina a prioridade de cada v�rtice no grid.
	 */
	private void determinaPrioridade(){
		for(int linha = 0; linha < grid.length; linha++)
			for(int coluna = 0; coluna < grid.length; coluna++){
				if(isComDilution()){
					grid[linha][coluna].setPrioridade(retornaPrioridadeVertice(grid[linha][coluna]));
				} else {
					grid[linha][coluna].setPrioridade(
							retornaPrioridadeVertice(retornaVizinhosUmSalto(grid[linha][coluna]).size(), 0, new ArrayList<Integer>(), grid[linha][coluna], false, (byte) 0)
						);
				}
			}
	}
	
	/**
	 * M�todo que calcula a prioridade de um v�rtice (Com Dilution).
	 * 
	 * @param vertice - V�rtice qualquer do grid, o qual ser� calculado a prioridade do mesmo.
	 * 
	 * @return Integer - Prioridade do v�rtice.
	 */
	private int retornaPrioridadeVertice(VerticeCent vertice){
		int quantVizinhos = 0;
		
		int linha = ((vertice.getPosicaoX() - 3) < 0) ? 0 : (vertice.getPosicaoX() - 3);
		while(linha <= (vertice.getPosicaoX() + 3) && linha < grid.length){
			int coluna = ((vertice.getPosicaoY() - 3) < 0) ? 0 : (vertice.getPosicaoY() - 3);
			while(coluna <= (vertice.getPosicaoY() + 3) && coluna < grid.length){
				if(grid[linha][coluna].getIdentificador() != vertice.getIdentificador()){
					quantVizinhos++;
				}
				coluna++;
			}
			linha++;
		}
		
		return quantVizinhos;
	}
	
	/**
	 * M�todo que calcula a prioridade de um v�rtice (Sem Dilution).
	 * 
	 * @param quantVizinhosTotal - Quantidade de vizinhos de um salto de um v�rtice.
	 * @param quantVizinhosAtual - Quantidade de vizinhos de um salto analisados.
	 * @param idVerticesVisitados - Lista com os identificadores dos vizinhos visitados.
	 * @param verticeAtual - V�rtice qualquer do grid, o qual ser� calculado a prioridade do mesmo.
	 * @param pararSalto - Vari�vel booleana que indica se deve realizar mais um salto ou n�o.
	 * @param nivel - N�mero do Salto. Seu valor vai at� 3 saltos.
	 * 
	 * @return Integer - Prioridade do v�rtice.
	 */
	private int retornaPrioridadeVertice(int quantVizinhosTotal, int quantVizinhosAtual, ArrayList<Integer> idVerticesVisitados, VerticeCent verticeAtual, boolean pararSalto, byte nivel){
		/* O processamento recursivo ir� parar quando os vizinhos dos vizinhos do seu
		 * �ltimo vizinho forem visitados. 
		 */
		if(quantVizinhosAtual == quantVizinhosTotal){
			// Remove o v�rtice desejado da lista de v�rtices visitados
			if(nivel == 0){
				idVerticesVisitados.remove(0);
			}
		} else {
			// Se um v�rtice n�o est� contido na lista, deve-se inclu�-lo
			if(!idVerticesVisitados.contains(verticeAtual.getIdentificador())){
				idVerticesVisitados.add(verticeAtual.getIdentificador());
			}
			
			// Determina se deve realizar um salto ou n�o
			pararSalto = (nivel == QUANT_MAX_NIVEL) ? true : false;
			
			// Caso deva-se realizar um salto, ser�o visitados os vizinhos dos vizinhos dos vizinhos do v�rtice em quest�o
			if(!pararSalto){
				VerticeCent novoAtual = retornaVizinhosUmSalto(verticeAtual).get(quantVizinhosAtual);
				int novoTotal = retornaVizinhosUmSalto(novoAtual).size();
				nivel++;
				retornaPrioridadeVertice(novoTotal, 0, idVerticesVisitados, novoAtual, pararSalto, nivel);
				nivel--;
				quantVizinhosAtual++;
				retornaPrioridadeVertice(quantVizinhosTotal, quantVizinhosAtual, idVerticesVisitados, verticeAtual, pararSalto, nivel);
			}
		}
		return idVerticesVisitados.size();
	}
	
	/**
	 * M�todo que retorna uma lista com os vizinhos de um salto de um v�rtice (Sem Dilution).
	 * 
	 * @param vertice - V�rtice qualquer do grid.
	 * 
	 * @return ArrayList - Lista com os vizinhos de um salto do v�rtice escolhido.
	 */
	public ArrayList<VerticeCent> retornaVizinhosUmSalto(VerticeCent vertice){
		ArrayList<VerticeCent> vizinhos = new ArrayList<VerticeCent>();
		
		// Posi��o Vertical: (X - 1, Y) e (X + 1, Y)
		int posicao = vertice.getPosicaoX() - 1;
		if(posicao >= 0){
			vizinhos.add(grid[posicao][vertice.getPosicaoY()]);
		}
		if((posicao + 2) < grid.length){
			posicao += 2;
			vizinhos.add(grid[posicao][vertice.getPosicaoY()]);
		}
		
		// Posi��o Horizontal: (X, Y - 1) e (X, Y + 1)
		posicao = vertice.getPosicaoY() - 1;
		if(posicao >= 0){
			vizinhos.add(grid[vertice.getPosicaoX()][posicao]);
		}
		if((posicao + 2) < grid.length){
			posicao += 2;
			vizinhos.add(grid[vertice.getPosicaoX()][posicao]);
		}
				
		return vizinhos;
	}
	
	/**
	 * M�todo que retorna uma lista com os vizinhos de N saltos de um v�rtice (Com Dilution).
	 * 
	 * @param vertice - V�rtice qualquer do grid.
	 * 
	 * @return ArrayList - Lista com os vizinhos de N saltos do v�rtice escolhido.
	 */
	public HashMap<VerticeCent, Integer> retornaVizinhosNSalto(VerticeCent vertice, int quantSaltos){
		HashMap<VerticeCent, Integer> vizinhos = new HashMap<VerticeCent, Integer>();
		
		int salto = 1;
		
		while(salto <= quantSaltos){		
			int linha = ((vertice.getPosicaoX() - salto) < 0) ? 0 : (vertice.getPosicaoX() - salto);
			while(linha <= (vertice.getPosicaoX() + salto) && linha < grid.length){
				int coluna = ((vertice.getPosicaoY() - salto) < 0) ? 0 : (vertice.getPosicaoY() - salto);
				while(coluna <= (vertice.getPosicaoY() + salto) && coluna < grid.length){
					if(grid[linha][coluna].getIdentificador() != vertice.getIdentificador() && !vizinhos.containsKey(grid[linha][coluna])){
						vizinhos.put(grid[linha][coluna], salto);
					}
					coluna++;
				}
				linha++;
			}
			salto++;
		}
		
		return vizinhos;
	}
	
	/**
	 * M�todo que transforma um grid em um vetor ordenado em ordem decrescente pela
	 * prioridade e crescente pelo identificador.
	 * 
	 * @return VerticeCent[] - um vetor de v�rtices ordenado em ordem decrescente pela
	 * prioridade e crescente pelo identificador.
	 */
	private VerticeCent[] transformaGridEmVetorOrdenadoPriori(){
		
		// Cria um vetor com a quantidade de v�rtices do grid
		VerticeCent[] retorno = new VerticeCent[grid.length * grid.length];
		
		// Atribui cada a posi��o do vetor um v�rtice do grid 
		int posicao = 0;
		for(int linha = 0; linha < grid.length; linha++)
			for(int coluna = 0; coluna < grid.length; coluna++)
				retorno[posicao++] = grid[linha][coluna];
		
		// Ordena o vetor
		Utilidade.ordenaListaVertices(retorno, 0, retorno.length - 1);
		
		// Atribui a cada v�rtice, a sua posi��o na lista de prioridades
		for(posicao = 0; posicao < retorno.length; posicao++){
			retorno[posicao].setOrdemListaPriori(posicao);
		}
		
		return retorno;
	}
	
	/**
	 * M�todo que retorna uma lista com as posi��es na lista de prioridades para os v�rtices
	 * mais pr�ximos do v�rtice passado como par�metro, os quais devem ter a mesma cor desse
	 * v�rtice.
	 *  
	 * @return ArrayList - lista com as posi��es na lista de prioridades para os v�rtices
	 * mais pr�ximos do v�rtice passado como par�metro, os quais devem ter a mesma cor
	 * desse v�rtice.
	 * 
	 * @param vertice - V�rtice qualquer no grid.
	 */
	private ArrayList<Integer> retornaPosicaoListaPrioriVerticesComMesmaCor(VerticeCent vertice){
		ArrayList<Integer> verticesMesmaCor = new ArrayList<Integer>();
		
		// Posi��o Vertical: (X - 4, Y) e (X + 4, Y)
		int posicao = vertice.getPosicaoX() - 4;
		if(posicao >= 0){
			verticesMesmaCor.add(grid[posicao][vertice.getPosicaoY()].getOrdemListaPriori());
		}
		if((posicao + 8) < grid.length){
			posicao += 8;
			verticesMesmaCor.add(grid[posicao][vertice.getPosicaoY()].getOrdemListaPriori());
		}
		
		// Posi��o Horizontal: (X, Y - 4) e (X, Y + 4)
		posicao = vertice.getPosicaoY() - 4;
		if(posicao >= 0){
			verticesMesmaCor.add(grid[vertice.getPosicaoX()][posicao].getOrdemListaPriori());
		}
		if((posicao + 8) < grid.length){
			posicao += 8;
			verticesMesmaCor.add(grid[vertice.getPosicaoX()][posicao].getOrdemListaPriori());
		}
		
		// Determina-se os v�rtices das diagonais, quando � sem dilution
		if(!isComDilution()){
			
			// Diagonal Prim�ria: (X - 2, Y - 2) e (X + 2, Y + 2)
			posicao = vertice.getPosicaoX() - 2;
			int posicao2 = vertice.getPosicaoY() - 2;
	
			if(posicao >= 0 && posicao2 >= 0){
				verticesMesmaCor.add(grid[posicao][posicao2].getOrdemListaPriori());
			}
			if((posicao + 4) < grid.length && (posicao2 + 4) < grid.length){
				posicao += 4;
				posicao2 += 4;
				verticesMesmaCor.add(grid[posicao][posicao2].getOrdemListaPriori());
			}
			
			// Diagonal Secund�ria: (X + 2, Y - 2) e (X - 2, Y + 2) 
			posicao = vertice.getPosicaoX() + 2;
			posicao2 = vertice.getPosicaoY() - 2;
	
			if(posicao < grid.length && posicao2 >= 0){
				verticesMesmaCor.add(grid[posicao][posicao2].getOrdemListaPriori());
			}
			if((posicao - 4) >= 0 && (posicao2 + 4) < grid.length){
				posicao -= 4;
				posicao2 += 4;
				verticesMesmaCor.add(grid[posicao][posicao2].getOrdemListaPriori());
			}
		}
		
		return verticesMesmaCor;
	}
	
	/**
	 * M�todo que retorna uma lista com as posi��es na lista de prioridades
	 * para os vizinhos dos vizinhos dos vizinhos de um salto de um v�rtice
	 * (Sem Dilution).
	 * 
	 * @param quantVizinhosTotal - Quantidade de vizinhos de um salto de um v�rtice.
	 * @param quantVizinhosAtual - Quantidade de vizinhos de um salto analisados.
	 * @param posicaoVerticesVisitados - Lista com a posi��o no vetor de v�rtices dos vizinhos
	 * que foram visitados.
	 * @param posicaoVerticeAtual - Posi��o do v�rtice a ser analisado no vetor de v�rtices. 
	 * @param pararSalto - Vari�vel booleana que indica se deve realizar mais um salto ou n�o.
	 * @param nivel - N�mero do Salto. Seu valor vai at� 3 saltos.
	 * @param vertices - Vetor de v�rtices ordenados pela prioridade e pelo identificador.
	 * 
	 * @return ArrayList - Lista com a posi��o no vetor de v�rtices dos vizinhos
	 * que foram visitados.
	 */
	private ArrayList<Integer> retornaPosicaoListaPrioriVizinhosAte3Saltos(int quantVizinhosTotal, int quantVizinhosAtual, ArrayList<Integer> posicaoVerticesVisitados, int posicaoVerticeAtual, boolean pararSalto, byte nivel, VerticeCent[] vertices){
		/* O processamento recursivo ir� parar quando os vizinhos dos vizinhos do seu
		 * �ltimo vizinho forem visitados. 
		 */
		if(quantVizinhosAtual == quantVizinhosTotal){
			// Remove o v�rtice desejado da lista de v�rtices visitados
			if(nivel == 0){
				posicaoVerticesVisitados.remove(0);
			}
		} else {
			// Se um v�rtice n�o est� contido na lista, deve-se inclu�-lo
			if(!posicaoVerticesVisitados.contains(posicaoVerticeAtual)){
				posicaoVerticesVisitados.add(posicaoVerticeAtual);
			}
			
			// Determina se deve realizar um salto ou n�o
			pararSalto = (nivel == QUANT_MAX_NIVEL) ? true : false;
			
			// Caso deva-se realizar um salto, ser�o visitados os vizinhos dos vizinhos dos vizinhos do v�rtice em quest�o
			if(!pararSalto){
				int idNovoAtual = retornaVizinhosUmSalto(vertices[posicaoVerticeAtual]).get(quantVizinhosAtual).getOrdemListaPriori();
				int novoTotal = retornaVizinhosUmSalto(vertices[idNovoAtual]).size();
				nivel++;
				retornaPosicaoListaPrioriVizinhosAte3Saltos(novoTotal, 0, posicaoVerticesVisitados, idNovoAtual, pararSalto, nivel, vertices);
				nivel--;
				quantVizinhosAtual++;
				retornaPosicaoListaPrioriVizinhosAte3Saltos(quantVizinhosTotal, quantVizinhosAtual, posicaoVerticesVisitados, posicaoVerticeAtual, pararSalto, nivel, vertices);
			}
		}
		return posicaoVerticesVisitados;
	}
	
	/**
	 * M�todo que retorna uma lista com as posi��es na lista de prioridades
	 * para os vizinhos dos vizinhos dos vizinhos de um salto de um v�rtice
	 * (Sem Dilution).
	 * 
	 * @param quantVizinhosTotal - Quantidade de vizinhos de um salto de um v�rtice.
	 * @param quantVizinhosAtual - Quantidade de vizinhos de um salto analisados.
	 * @param posicaoVerticesVisitados - Lista com a posi��o no vetor de v�rtices dos vizinhos
	 * que foram visitados.
	 * @param posicaoVerticeAtual - Posi��o do v�rtice a ser analisado no vetor de v�rtices. 
	 * @param pararSalto - Vari�vel booleana que indica se deve realizar mais um salto ou n�o.
	 * @param nivel - N�mero do Salto. Seu valor vai at� 3 saltos.
	 * @param vertices - Vetor de v�rtices ordenados pela prioridade e pelo identificador.
	 * 
	 * @return ArrayList - Lista com a posi��o no vetor de v�rtices dos vizinhos
	 * que foram visitados.
	 */
	private ArrayList<Integer> retornaPosicaoListaPrioriVizinhosAte3Saltos(VerticeCent vertice){
		ArrayList<Integer> listaPrioriVizinhos = new ArrayList<Integer>();
		
		int linha = ((vertice.getPosicaoX() - 3) < 0) ? 0 : (vertice.getPosicaoX() - 3);
		while(linha <= (vertice.getPosicaoX() + 3) && linha < grid.length){
			int coluna = ((vertice.getPosicaoY() - 3) < 0) ? 0 : (vertice.getPosicaoY() - 3);
			while(coluna <= (vertice.getPosicaoY() + 3) && coluna < grid.length){
				if(grid[linha][coluna].getIdentificador() != vertice.getIdentificador()){
					listaPrioriVizinhos.add(grid[linha][coluna].getOrdemListaPriori());
				}
				coluna++;
			}
			linha++;
		}
		
		return listaPrioriVizinhos;
	}
	
	
	/**
	 * M�todo que permite colorir o grid com colora��o de tr�s saltos.
	 */
	public void coloreGridTresSaltosComBasePrioridades() {
		VerticeCent[] vetorVerticesOrdPriori = transformaGridEmVetorOrdenadoPriori();
		
		// Vari�vel que indica se podemos ou n�o colorir o v�rtice com certa cor
		boolean colorir = true;

		// V�rtice com maior prioridade que n�o foi colorido, deve-se colorir com a cor 1
		int cor = 1;
		if(vetorVerticesOrdPriori[0].getCor() == -1) {
			vetorVerticesOrdPriori[0].setCor(cor++);
		}
		
		// Lista dos vizinhos
		ArrayList<Integer> vizinhos = new ArrayList<Integer>();

		// para cada v�rtice do grid, procurar a cor para color�-lo
		for (int vertice = 1; vertice < vetorVerticesOrdPriori.length; vertice++) {
			
			// Pega os vizinhos do v�rtice em quest�o
			vizinhos = (isComDilution()) ?
				retornaPosicaoListaPrioriVizinhosAte3Saltos(vetorVerticesOrdPriori[vertice]) :
				retornaPosicaoListaPrioriVizinhosAte3Saltos(retornaVizinhosUmSalto(vetorVerticesOrdPriori[vertice]).size(), 0, new ArrayList<Integer>(), vetorVerticesOrdPriori[vertice].getOrdemListaPriori(), false, (byte) 0, vetorVerticesOrdPriori);
			
			int corAtual = 1;
			
			// Vari�vel booleana que indica se o v�rtice foi colorido
			boolean flag = false;
			while(flag == false && corAtual <= cor) {
				colorir = true;
				
				// Analisa se a cor dos vizinhos � igual a corAtual 
				int cont = 0;
				while(cont < vizinhos.size() && colorir == true){
					// Ver a cor do vizinho
					if (vetorVerticesOrdPriori[vizinhos.get(cont)].getCor() == corAtual)
						// Se a cor j� utilizada pelo vizinho, ent�o n�o pode utilizar esta cor
						colorir = false;
					cont++;
				}
				
				// Colore o v�rtice quando colorir � igual a true
				if (colorir == true) {
					
					// Analisar os poss�veis v�rtices mais pr�ximos, os quais devem ter a mesma cor
					ArrayList<Integer> verticesMesmaCor = retornaPosicaoListaPrioriVerticesComMesmaCor(vetorVerticesOrdPriori[vertice]);
					cont = 0;
					// Se existe um padr�o de cores, ent�o colore-se o v�rtice com a cor do padr�o
					while(cont < verticesMesmaCor.size() && flag == false){
						if(vetorVerticesOrdPriori[verticesMesmaCor.get(cont)].getCor() != -1){
							vetorVerticesOrdPriori[vertice].setCor(vetorVerticesOrdPriori[verticesMesmaCor.get(cont)].getCor());
							// Indica que o v�rtice foi colorido
							flag = true;
						}
						cont++;
					}
					
					// Se o v�rtice n�o foi colorido, ent�o colore-o com a cor atual
					if(flag == false){
						vetorVerticesOrdPriori[vertice].setCor(corAtual);
						flag = true;
					}
				}
				corAtual++;
			}
			
			// Se n�o pode colorir o v�rtice com uma cor, colore-o com a pr�xima cor
			if (colorir == false) {
				cor++;
				vetorVerticesOrdPriori[vertice].setCor(cor);
			}
		}
		
		// Armazenando a quantidade de cores utilizada
		quantCoresUtilizadas = cor;
	}
	
	/**
	 * M�todo que permite colorir o grid com colora��o de tr�s saltos com o M�todo do Vetor
	 * com ou sem Dilution.
	 */
	public void coloreGridTresSaltosMetodoVetor(){
		
		// A colora��o inicia-se pela cor 1 
		int cor = 1;
		
		// Vari�vel que conta o n�mero de v�rtices visitados ou que foram coloridos
		int quantVerticesVisitados = 0;
		
		// Whiles que ir�o varrer o todo o grid ou at� que todos os v�rtices tenham sido coloridos
		int i = 0;

		while(i < grid.length && quantVerticesVisitados < numTotalVertices){
			int j = 0;
			while(j < grid.length && quantVerticesVisitados < numTotalVertices){
				
				// Analisa os v�rtices n�o coloridos
				if(grid[i][j].getCor() == -1){
					
					if(!isComDilution()){
						// Quando os v�rtices s�o coloridos sem Dilution						
						
						/*
						 * Vari�vel boolean que determina se a an�lise de
						 * colora��o iniciar� na primeira coluna ou n�o
						 * 
						 * True: Ser� analisado a primeira coluna
						 * False: Caso contr�rio.
						 *  
						 */
						boolean flag = true;
					
						// Colorir determinados v�rtices com uma cor
						for(int linha = i; linha < grid.length; linha += 2){
							
							// Determina��o do valor da vari�vel coluna a partir do flag
							int coluna;
							if(flag){
								coluna = j;
							} else {
								if((j - 2) >= 0){
									coluna = j - 2;
								} else {
									coluna = j + 2;
								}
							}
							
							// Colore os v�rtices com uma determinada cor a cada tr�s saltos 
							while(coluna < grid.length){
								if(grid[linha][coluna].getCor() == -1){
									grid[linha][coluna].setCor(cor);
									quantVerticesVisitados++;
								}
								coluna += 4;
							}
							
							/*
							 *  Altern�ncia do valor de flag, isto �, determinar� se
							 *  a an�lise iniciar� na primeira coluna (true) ou n�o (false).
							 */
							if(flag){
								flag = false;
							} else {
								flag = true;
							}
						}
					} else {
						// Quando os v�rtices s�o coloridos com Dilution
						
						// Colorir determinados v�rtices com uma cor
						for(int linha = i; linha < grid.length; linha += 4){
													
							// Colore os v�rtices com uma determinada cor a cada tr�s saltos 
							for(int coluna = j; coluna < grid.length; coluna += 4){
								if(grid[linha][coluna].getCor() == -1){
									grid[linha][coluna].setCor(cor);
									quantVerticesVisitados++;
								}
							}
						}
					}
					
					/*
					 *  Ap�s ter colorido os poss�veis v�rtices com uma determinada cor,
					 *  ser� utilizada outra cor para colorir outros v�rtices.
					 */
					cor++;
				}
				// Incremento da coluna j do grid
				j++;
			}
			// Incremento da linha i do grid
			i++;
		}
		
		// Armazenando a quantidade de cores utilizada
		quantCoresUtilizadas = --cor;
	}
	
	/**
	 * M�todo para mostrar o grid pelo id dos v�rtices.
	 */
	public void mostraGrigId(){
		System.out.println("Visualiza��o do Grid por Id");
		for(int linha = 0; linha < grid.length; linha++){
			for(int coluna = 0; coluna < grid.length; coluna++)
				System.out.print(grid[linha][coluna].getIdentificador() + "\t");
			System.out.println();
		}
	}
	
	/**
	 * M�todo para mostrar o grid pela prioridade dos v�rtices.
	 */
	public void mostraGrigPriori(){
		System.out.println("Visualiza��o do Grid por Prioridade");
		for(int linha = 0; linha < grid.length; linha++){
			for(int coluna = 0; coluna < grid.length; coluna++)
				System.out.print(grid[linha][coluna].getPrioridade() + "\t");
			System.out.println();
		}
	}
	
	/**
	 * M�todo para mostrar o grid pela cor dos v�rtices.
	 */
	public void mostraGrigCor(){
		System.out.println("Visualiza��o do Grid por Cor");
		for(int linha = 0; linha < grid.length; linha++){
			for(int coluna = 0; coluna < grid.length; coluna++)
				System.out.print(grid[linha][coluna].getCor() + "\t");
			System.out.println();
		}
	}
	
	/**
	 * M�todo toString, o qual retorna uma string com as informa��es dos atributos do grid.
	 * 
	 * @return String - Sequ�ncia de caracteres contendo as informa��es dos atributos do grid.
	 */
	@Override
	public String toString(){
		return "Lado do Quadrado do Grid: " + getLadoQuadrado() + "\nDimens�o do Grid: " + getDimensaoGrid() + "\n�rea total do Grid: " +
				getAreaTotalGrid() + "\nAlcance do Sensor: " + getAlcanceSensor() + "\n" + "Dist�ncia entre V�rtices: " + getDistanciaEntreVertices() +
				"\nN�mero Parcial de V�rtices: " + getNumParcialVertices() + "\nN�mero Total de V�rtices: " + getNumTotalVertices() +
				"\nQuantidade de Cores Utilizadas: " + getQuantCoresUtilizadas();
	}

	// M�todos Getters e Setters
	public int getDimensaoGrid() {
		return dimensaoGrid;
	}

	public void setDimensaoGrid(int dimensaoGrid) {
		this.dimensaoGrid = dimensaoGrid;
	}

	public double getLadoQuadrado() {
		return ladoQuadrado;
	}

	public void setLadoQuadrado(double ladoQuadrado) {
		this.ladoQuadrado = ladoQuadrado;
	}

	public VerticeCent[][] getGrid() {
		return grid;
	}

	public int getAreaTotalGrid() {
		return areaTotalGrid;
	}

	public double getAlcanceSensor() {
		return alcanceSensor;
	}

	public double getDistanciaEntreVertices() {
		return distanciaEntreVertices;
	}

	public int getNumParcialVertices() {
		return numParcialVertices;
	}

	public int getNumTotalVertices() {
		return numTotalVertices;
	}
	
	public boolean isComDilution() {
		return comDilution;
	}

	public void setComDilution(boolean comDilution) {
		this.comDilution = comDilution;
	}

	public boolean isMetodoVetor() {
		return eMetodoVetor;
	}

	public void setEMetodoVetor(boolean eMetodoVetor) {
		this.eMetodoVetor = eMetodoVetor;
	}

	public int getQuantCoresUtilizadas() {
		return quantCoresUtilizadas;
	}
}