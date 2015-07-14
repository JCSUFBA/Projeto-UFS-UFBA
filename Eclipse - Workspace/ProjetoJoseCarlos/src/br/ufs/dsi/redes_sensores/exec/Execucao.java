package br.ufs.dsi.redes_sensores.exec;

import javax.swing.JOptionPane;

import br.ufs.dsi.redes_sensores.project.Grid;

public class Execucao {
	
	public static enum tipo_metodo {TIPO_1, TIPO_2};
	
	/**
	 * M�todo main para executar os projetos.
	 */
	public static void main(String[] args) {
		int tipo = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Informe o tipo do projeto:\n" +
				"1- Colora��o com C�lculo de Prioridade\n" +
				"2- Colora��o por M�todo Vetor", "Entrada", JOptionPane.QUESTION_MESSAGE));
		
		// True: M�todo Vetor | False: Colora��o Baseada em Prioridades
		boolean eMetodoVetor = (--tipo == tipo_metodo.TIPO_2.ordinal());
		int dimensaoGrid = Integer.parseInt(JOptionPane.showInputDialog(null, "Entre com os valores da �rea (M) x (M)", "Entrada", JOptionPane.QUESTION_MESSAGE));
		double ladoQuadrado = Double.parseDouble(JOptionPane.showInputDialog(null, "Entre com o valor do lado quadrado do grid \n (Use o ponto como separador de casas decimais)", "Entrada", JOptionPane.QUESTION_MESSAGE));
		int simOuNao = JOptionPane.showConfirmDialog(null, "Deseja executar o projeto com Dilution?", "Entrada", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		boolean comDilution = (simOuNao == JOptionPane.YES_OPTION) ? true : false;
		
		Grid grid = new Grid(ladoQuadrado, dimensaoGrid, comDilution, eMetodoVetor);
		
		// Inicializa o grid com os v�rtices
		grid.inicializaVertices();
		
		// Mostra o grid pelos identificadores
		grid.mostraGrigId();
		System.out.println();
		
		// Mostra o grid pelas prioridades
		if(!grid.isMetodoVetor()){
			grid.mostraGrigPriori();
			System.out.println();
		}
		
		// Vari�vel para determinar o tempo para colorir o grid (tempo em milisegundos)
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
		
		// Mostra informa��es do grid
		System.out.println();
		System.out.println(grid.toString());
	}
}