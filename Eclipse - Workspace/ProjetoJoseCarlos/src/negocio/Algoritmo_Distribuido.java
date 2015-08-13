package negocio;

import java.util.ArrayList;
import java.util.Random;

import model.Distancia;
import model.VerticeDist;
import utils.Utilidade;

public class Algoritmo_Distribuido {
	
	// Constantes
	private static int QUANT_MAX_NIVEL = 3;

	// Vari�veis
	private ArrayList<VerticeDist> vertices;
	private int numTotalVertices;
	private double raio;
	private double areaTotal;
	private boolean comDilution;
	private double ladoArea;
	private double alcanceSensor;
	private int quantCoresUtilizadas;
	
	// Construtor
	public Algoritmo_Distribuido(int numTotalVertices, double raio, double areaTotal, boolean comDilution){
		setNumTotalVertices(numTotalVertices);
		setRaio(raio);
		setAreaTotal(areaTotal);
		setComDilution(comDilution);
		
		calculaValoresSemComDilution();
		vertices = new ArrayList<VerticeDist>();
	}

	// M�todo para inicializar vari�veis sem / com Dilution
	private void calculaValoresSemComDilution(){
		ladoArea = Math.sqrt(areaTotal);
		
		// TODO: Corrigir isso com base no dilution.
		/*
		 if(isComDilution())
			alcanceSensor = raio * Math.sqrt(2);
		else
			alcanceSensor = raio;
		*/
		
		alcanceSensor = raio;
	}
	
	/**
	 * M�todo para iniciar a lista de v�rtices, bem como determina a prioridade de cada v�rtice
	 * e seus vizinhos de um salto com base no alcance do sensor.
	 */
	public void inicializaVertices(){
		Random random = new Random();
		for(int id = 0; id < getNumTotalVertices(); id++){
			
			// Determina aleatoriamente as posi��es
			double posicaoX = (random.nextDouble() * getLadoArea());
			double posicaoY = (random.nextDouble() * getLadoArea());
			
			// Determina um posi��o �nica no plano
			boolean achou = false;
			while(!achou){
				// Procura por v�rtices que estejam nas mesmas posi��es
				for (int v = 0; v < vertices.size() && !achou; v++) {
					if(posicaoX == vertices.get(v).getPosicaoX() && posicaoY == vertices.get(v).getPosicaoY())
						achou = true;
				}
				
				// Se achar, determina uma nova e analisa-se novamente
				if(achou){
					posicaoX = (random.nextDouble() * getLadoArea());
					posicaoY = (random.nextDouble() * getLadoArea());
					achou = false;
				} else
					achou = true;
			}
			
			// Cria um v�rtice e adiciona-o na lista
			vertices.add(new VerticeDist(id, posicaoX, posicaoY));
		}
		
		// Determina os vizinhos de um salto de cada v�rtice e calcula-se a sua prioridade
		determinaPrioridade();
	}
	
	/**
	 * M�todo que determina a prioridade de cada v�rtice, assim como a lista de vizinhos
	 * de um salto.
	 */
	private void determinaPrioridade(){
		// Obt�m a lista de dist�ncias dos vizinhos de um salto de cada v�rtice
		for(VerticeDist v : vertices){
			v.setDistancias(retornaListaDistancias(v.getIdentificador()));
		}
		
		// Calcular a prioridade de cada v�rtice
		for(VerticeDist v : vertices){
			v.setPrioridade(retornaPrioridadeVertice(v.getIdentificador()));
		}
	}
	
	/**
	 * M�todo que calcula a Dist�ncia Euclidiana entre um determinado v�rtice e
	 * os demais v�rtices, bem como adiciona na lista de vizinhos apenas os
	 * identificadores dos v�rtices que est�o ao seu alcance.
	 * 
	 * @param vertice: Identificador do V�rtice.
	 * 
	 * @return ArrayList : Lista de identificadores e dist�ncia dos vizinhos de
	 * um salto.
	 */
	private ArrayList<Distancia> retornaListaDistancias(int vertice){
		ArrayList<Distancia> distancia = new ArrayList<Distancia>();
		
		// Analisa-se todos os v�rtices, exceto o v�rtice passado como par�metro
		for(VerticeDist v : vertices){
			if(v.getIdentificador() != vertices.get(vertice).getIdentificador()){
				// C�lculo da Dist�ncia Euclidiana
				double dist = Math.sqrt(
						Math.pow((vertices.get(vertice).getPosicaoX() - v.getPosicaoX()), 2) +
						Math.pow((vertices.get(vertice).getPosicaoY() - v.getPosicaoY()), 2)
				);
				
				// Se estiver dentro do alcance do sensor, ent�o � vizinho de um salto
				if(dist <= getAlcanceSensor()){
					distancia.add(new Distancia(v.getIdentificador(), dist));
				}
			}
		}
		return distancia;
	}
	
	/**
	 * M�todo que retorna uma lista com os identificadores dos vizinhos dos vizinhos
	 * dos vizinhos de um salto de um v�rtice.
	 * 
	 * @param quantVizinhosTotal - Quantidade de vizinhos de um salto de um v�rtice.
	 * @param quantVizinhosAtual - Quantidade de vizinhos de um salto analisados.
	 * @param idVerticesVisitados - Lista com os identificadores dos vizinhos
	 * que foram visitados.
	 * @param vertice - Identificador do v�rtice a ser analisado. 
	 * @param pararSalto - Vari�vel booleana que indica se deve realizar mais um salto ou n�o.
	 * @param nivel - N�mero do Salto. Seu valor vai at� 3 saltos.
	 * 
	 * @return ArrayList - Lista com os identificadores dos vizinhos que foram visitados.
	 */
	private ArrayList<Integer> retornaPosicaoVizinhosAte3Saltos(int quantVizinhosTotal, int quantVizinhosAtual, ArrayList<Integer> idVerticesVisitados, int vertice, boolean pararSalto, byte nivel){
		/* O processamento recursivo ir� parar quando os vizinhos dos vizinhos do seu
		   �ltimo vizinho forem visitados.
		*/
		if(quantVizinhosAtual == quantVizinhosTotal){
			// Remove o v�rtice desejado da lista de v�rtices visitados
			if(nivel == 0){
				if(idVerticesVisitados.size() > 0)
					idVerticesVisitados.remove(0);
			}
		} else {
			// Se um v�rtice n�o est� contido na lista, deve-se inclu�-lo
			if(!idVerticesVisitados.contains(vertice)){
				idVerticesVisitados.add(vertice);
			}
			
			// Determina se deve realizar um salto ou n�o
			pararSalto = (nivel == QUANT_MAX_NIVEL) ? true : false;
			
			// Caso deva-se realizar um salto, ser�o visitados os vizinhos dos vizinhos dos vizinhos do v�rtice em quest�o
			if(!pararSalto){
				int idNovoAtual = vertices.get(vertice).getDistancias().get(quantVizinhosAtual).getVertice();
				int novoTotal = vertices.get(idNovoAtual).getDistancias().size();
				nivel++;
				retornaPosicaoVizinhosAte3Saltos(novoTotal, 0, idVerticesVisitados, idNovoAtual, pararSalto, nivel);
				nivel--;
				quantVizinhosAtual++;
				retornaPosicaoVizinhosAte3Saltos(quantVizinhosTotal, quantVizinhosAtual, idVerticesVisitados, vertice, pararSalto, nivel);
			}
		}
		return idVerticesVisitados;
	}
	
	/**
	 * M�todo que calcula a prioridade de um v�rtice.
	 * 
	 * @param vertice - V�rtice qualquer da lista, o qual ser� calculado a prioridade do mesmo.
	 * 
	 * @return Integer - Prioridade do v�rtice.
	 */
	private int retornaPrioridadeVertice(int vertice){
		ArrayList<Integer> idVerticesVisitados = new ArrayList<Integer>();
		retornaPosicaoVizinhosAte3Saltos(vertices.get(vertice).getDistancias().size(), 0, idVerticesVisitados, vertice, false, (byte) 0);
		return idVerticesVisitados.size();
	}
	
	/**
	 * M�todo que transforma uma lista de v�rtices em um vetor ordenado em ordem
	 * decrescente pela prioridade e crescente pelo identificador.
	 * 
	 * @return VerticeDist[] - um vetor de v�rtices ordenado em ordem decrescente pela
	 * prioridade e crescente pelo identificador.
	 */
	private VerticeDist[] transformaListaVerticesEmVetorOrdenadoPriori(){
		// Cria um vetor com a quantidade de v�rtices
		VerticeDist[] retorno = new VerticeDist[vertices.size()];
		
		// Transforma a lista em um vetor
		retorno = vertices.toArray(retorno);		
		
		// Ordena o vetor
		Utilidade.ordenaListaVertices(retorno, 0, retorno.length - 1);
		
		// Atribui-se a posi��o do v�rtice no vetor no pr�prio v�rtice 
		for(int posicao = 0; posicao < retorno.length; posicao++){
			retorno[posicao].setOrdemListaPriori(posicao);
		}
		
		return retorno;
	}
	
	/**
	 * M�todo que permite colorir os v�rtices com colora��o de tr�s saltos.
	 */
	public void coloreTresSaltosComBasePrioridades() {
		VerticeDist[] vetorVerticesOrdPriori = transformaListaVerticesEmVetorOrdenadoPriori();
		
		// Vari�vel que indica se podemos ou n�o colorir o v�rtice com certa cor
		boolean colorir = true;

		// V�rtice com maior prioridade que n�o foi colorido, deve-se colorir com a cor 1
		int cor = 1;
		if(vetorVerticesOrdPriori[0].getCor() == -1) {
			vetorVerticesOrdPriori[0].setCor(cor++);
		}
		
		// Lista dos vizinhos
		ArrayList<Integer> vizinhos = new ArrayList<Integer>();

		// para cada v�rtice, procurar a cor para color�-lo
		for (int vertice = 1; vertice < vetorVerticesOrdPriori.length; vertice++) {
			
			// Pega os vizinhos do v�rtice em quest�o
			// TODO: Ver a quest�o do dilution nos vizinhos
			vizinhos = retornaPosicaoVizinhosAte3Saltos(vetorVerticesOrdPriori[vertice].getDistancias().size(), 0, new ArrayList<Integer>(), vetorVerticesOrdPriori[vertice].getIdentificador(), false, (byte) 0);
			
			int corAtual = 1;
			
			while(corAtual <= cor) {
				colorir = true;
				
				// Analisa se a cor dos vizinhos � igual a corAtual 
				int cont = 0;
				while(cont < vizinhos.size() && colorir == true){
					// Ver a cor do vizinho
					if (vetorVerticesOrdPriori[vertices.get(vizinhos.get(cont)).getOrdemListaPriori()].getCor() == corAtual)
						// Se a cor j� utilizada pelo vizinho, ent�o n�o pode utilizar esta cor
						colorir = false;
					cont++;
				}
				
				// Colore o v�rtice quando colorir � igual a true
				if (colorir == true) {
					vetorVerticesOrdPriori[vertice].setCor(corAtual);
					break;
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
	 * M�todo para mostrar os ids dos v�rtices.
	 */
	public void mostraId(){
		System.out.println("Visualiza��o dos V�rtices por Id");
		for (VerticeDist vertice : vertices) {
			System.out.println(vertice.getIdentificador());
		}
	}
	
	/**
	 * M�todo para mostrar os ids e posi��es dos v�rtices.
	 */
	public void mostraIdPosicoes(){
		System.out.println("Visualiza��o dos V�rtices por Id e Posi��o");
		for (VerticeDist vertice : vertices) {
			System.out.println(vertice.getIdentificador() + " - " +
					vertice.getPosicaoX() + " - " + vertice.getPosicaoY()
			);
		}
	}
	
	/**
	 * M�todo para mostrar as prioridades dos v�rtices.
	 */
	public void mostraPrioridades(){
		System.out.println("Visualiza��o dos V�rtices por Prioridade");
		for (VerticeDist vertice : vertices) {
			System.out.println(vertice.getIdentificador() + " - " + vertice.getPrioridade());
		}
	}
	
	/**
	 * M�todo para mostrar as cores dos v�rtices.
	 */
	public void mostraCor(){
		System.out.println("Visualiza��o dos V�rtices por Cor");
		for (VerticeDist vertice : vertices) {
			System.out.println(vertice.getIdentificador() + " - " + vertice.getCor());
		}
	}
	
	/**
	 * M�todo para mostrar os v�rtices e seus vizinhos de um salto.
	 */
	public void mostraVerticesComVizinhosUmSalto(){
		System.out.println("Visualiza��o dos V�rtices com Seus Vizinhos de Um Salto");
		for (VerticeDist vertice : vertices) {
			System.out.println("V�rtice " + vertice.getIdentificador() + ":");
			if(vertice.getDistancias().size() > 0){
				for(Distancia distancia : vertice.getDistancias()){
					System.out.print(distancia.getVertice() + "\t");
				}
			} else {
				System.out.print("NULL");
			}
			System.out.println();
		}
	}
	
	/**
	 * M�todo toString, o qual retorna uma string com as informa��es dos atributos.
	 * 
	 * @return String - Sequ�ncia de caracteres contendo as informa��es dos atributos.
	 */
	@Override
	public String toString(){
		return "Lado da �rea: " + getLadoArea() + "\n�rea total: " + getAreaTotal() +
				"\nAlcance do Sensor: " + getAlcanceSensor() + "\nN�mero Total de V�rtices: " + getNumTotalVertices() +
				"\nRaio: " + getRaio() + "\nQuantidade de Cores Utilizadas: " + getQuantCoresUtilizadas();
	}

	// M�todos Getters e Setters

	public double getAreaTotal() {
		return areaTotal;
	}

	public void setAreaTotal(double areaTotal) {
		this.areaTotal = areaTotal;
	}

	public double getRaio() {
		return raio;
	}

	public void setRaio(double raio) {
		this.raio = raio;
	}

	public int getNumTotalVertices() {
		return numTotalVertices;
	}

	public void setNumTotalVertices(int numTotalVertices) {
		this.numTotalVertices = numTotalVertices;
	}

	public boolean isComDilution() {
		return comDilution;
	}

	public void setComDilution(boolean comDilution) {
		this.comDilution = comDilution;
	}

	public ArrayList<VerticeDist> getVertices() {
		return vertices;
	}

	public double getLadoArea() {
		return ladoArea;
	}

	public double getAlcanceSensor() {
		return alcanceSensor;
	}

	public int getQuantCoresUtilizadas() {
		return quantCoresUtilizadas;
	}
}