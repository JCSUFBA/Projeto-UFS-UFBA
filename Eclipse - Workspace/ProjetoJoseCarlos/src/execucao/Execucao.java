package execucao;

import java.util.HashMap;
import javax.swing.JOptionPane;

import model.VerticeCent;
import negocio.Algoritmo_Centralizado;
import negocio.Algoritmo_Distribuido;

public class Execucao {
	
	/**
	 * Método main para executar os projetos.
	 */
	public static void main(String[] args) {
				
		int tipo;
		do {
			tipo = Integer.parseInt(JOptionPane.showInputDialog(null,
					"Informe o tipo do projeto:\n" +
					"1- Coloração com Cálculo de Prioridade\n" +
					"2- Coloração por Método Vetor\n" + 
					"3- Coloração Distribuída\n" + 
					"0 - Encerrar o programa", "Entrada", JOptionPane.QUESTION_MESSAGE));
		} while(tipo < 0 && tipo > 3);
		
		int simOuNao = JOptionPane.showConfirmDialog(null, "Deseja executar o projeto com Dilution?", "Entrada", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		boolean comDilution = (simOuNao == JOptionPane.YES_OPTION) ? true : false;
		
		if(tipo > 0 && tipo < 3){
			// True: Método Vetor | False: Coloração Baseada em Prioridades
			boolean eMetodoVetor = (tipo == 2);
			int dimensaoGrid = Integer.parseInt(JOptionPane.showInputDialog(null, "Entre com os valores da área (M) x (M)", "Entrada", JOptionPane.QUESTION_MESSAGE));
			double ladoQuadrado = Double.parseDouble(JOptionPane.showInputDialog(null, "Entre com o valor do lado quadrado do grid \n (Use o ponto como separador de casas decimais)", "Entrada", JOptionPane.QUESTION_MESSAGE));
			
			Algoritmo_Centralizado grid = new Algoritmo_Centralizado(ladoQuadrado, dimensaoGrid, comDilution, eMetodoVetor);
			
			// Inicializa o grid com os vértices
			grid.inicializaVertices();
			
			// Mostra o grid pelos identificadores
			grid.mostraGrigId();
			System.out.println();
			
			// Mostra o grid pelas prioridades
			if(!grid.isMetodoVetor()){
				grid.mostraGrigPriori();
				System.out.println();
			}
			
			// Variável para determinar o tempo para colorir o grid (tempo em milisegundos)
			long tempo = System.currentTimeMillis();
			
			// Colore o grid
			if(!grid.isMetodoVetor())
				grid.coloreGridTresSaltosComBasePrioridades();
			else
				grid.coloreGridTresSaltosMetodoVetor();
			
			// Tempo Total = Tempo Final - Tempo Inicial
			tempo = System.currentTimeMillis() - tempo;
			System.out.println("Tempo de Processamento em milisegundos: " + tempo);
			System.out.println();
			
			// Mostra o grid pelas cores
			grid.mostraGrigCor();
			
			// Mostra informações do grid
			System.out.println();
			System.out.println(grid.toString());
			
			// Verificando os vizinhos de um determinado vértice
			int linha = Integer.parseInt(JOptionPane.showInputDialog(null, "Entre com o valor da linha do vértice (1 - " + grid.getNumParcialVertices() + ")", "Entrada", JOptionPane.QUESTION_MESSAGE));
			int coluna = Integer.parseInt(JOptionPane.showInputDialog(null, "Entre com o valor da coluna do vértice (1 - " + grid.getNumParcialVertices() + ")", "Entrada", JOptionPane.QUESTION_MESSAGE));
			VerticeCent vertice = grid.getGrid()[--linha][--coluna];
			
			System.out.println("\nVizinhos do vértice " + vertice.getIdentificador() + ":\n");
			System.out.println("Vizinho\t\tSalto");
			HashMap<VerticeCent, Integer> vizinhos = grid.retornaVizinhosNSalto(vertice, 3);
			for (VerticeCent v : vizinhos.keySet()) {
				System.out.println(v.getIdentificador() + "\t\t" + vizinhos.get(v));
			}
		} else {
			int numTotalVertices = Integer.parseInt(JOptionPane.showInputDialog(null, "Entre com a quantidade de vértices", "Entrada", JOptionPane.QUESTION_MESSAGE));
			double raio = Double.parseDouble(JOptionPane.showInputDialog(null, "Entre com o valor do raio \n (Use o ponto como separador de casas decimais)", "Entrada", JOptionPane.QUESTION_MESSAGE));
			double areaTotal = Double.parseDouble(JOptionPane.showInputDialog(null, "Entre com o valor da área total \n (Use o ponto como separador de casas decimais)", "Entrada", JOptionPane.QUESTION_MESSAGE));
			
			Algoritmo_Distribuido dist = new Algoritmo_Distribuido(numTotalVertices, raio, areaTotal, comDilution);
			
			// Inicializa os vértices
			dist.inicializaVertices();
			
			// Mostra o vértices pelos identificadores
			dist.mostraId();
			System.out.println();
			
			// Mostra o vértices pelos identificadores e posições
			dist.mostraIdPosicoes();
			System.out.println();
			
			// Mostra o vértices pelos identificadores e prioridades
			dist.mostraPrioridades();
			System.out.println();
			
			// Mostra o vértices pelos identificadores e seus vizinhos de um salto
			dist.mostraVerticesComVizinhosUmSalto();
			System.out.println();
			
			// Variável para determinar o tempo para colorir o grid (tempo em milisegundos)
			long tempo = System.currentTimeMillis();
			
			dist.coloreTresSaltosComBasePrioridades();
			
			// Tempo Total = Tempo Final - Tempo Inicial
			tempo = System.currentTimeMillis() - tempo;
			System.out.println("Tempo de Processamento em milisegundos: " + tempo);
			System.out.println();
			
			// Mostra o grid pelas cores
			dist.mostraCor();
			System.out.println();
		
			// Mostra informações do grid
			System.out.println(dist.toString());
		}
	}
}