/*
 Copyright (c) 2007, Distributed Computing Group (DCG)
                    ETH Zurich
                    Switzerland
                    dcg.ethz.ch

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 - Neither the name 'Sinalgo' nor the names of its contributors may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package projects.examplecode;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import javax.swing.JOptionPane;
import projects.examplecode.nodes.nodeImplementations.MyNode;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

/**
 * This class holds customized global state and methods for the framework. The
 * only mandatory method to overwrite is <code>hasTerminated</code> <br>
 * Optional methods to override are
 * <ul>
 * <li><code>customPaint</code></li>
 * <li><code>handleEmptyEventQueue</code></li>
 * <li><code>onExit</code></li>
 * <li><code>preRun</code></li>
 * <li><code>preRound</code></li>
 * <li><code>postRound</code></li>
 * <li><code>checkProjectRequirements</code></li>
 * </ul>
 * 
 * @see sinalgo.runtime.AbstractCustomGlobal for more details. <br>
 *      In addition, this class also provides the possibility to extend the
 *      framework with custom methods that can be called either through the menu
 *      or via a button that is added to the GUI.
 */

class ComparadorDeNos implements Comparator<MyNode> {
	public int compare(MyNode node1, MyNode node2) {

		if (node1.getVizinhos().size() > node2.getVizinhos().size()) {
			return -1;
		} else {
			if (node1.getVizinhos().size() < node2.getVizinhos().size()) {
				return +1;
			} else {
				return 0;
			}
		}
	}
}

public class CustomGlobal extends AbstractCustomGlobal {
	ArrayList<MyNode> myNodes;
	Logging myLog = Logging.getLogger("logxxx.txt");

	// define o meu raio de alcance
	private final double raio = 100.0;

ArrayList<	Color> cores;

	/**
	 * (non-Javadoc)
	 * 
	 * @see runtime.AbstractCustomGlobal#hasTerminated()
	 */
	public boolean hasTerminated() {
		return false;
	}

	/**
	 * An example of a method that will be available through the menu of the
	 * GUI.
	 */
	@AbstractCustomGlobal.GlobalMethod(menuText = "Echo")
	public void echo() {
		// Query the user for an input
		String answer = JOptionPane.showInputDialog(null, "This is an example.\nType in any text to echo.");
		// Show an information message
		JOptionPane.showMessageDialog(null, "You typed '" + answer + "'", "Example Echo",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * An example to add a button to the user interface. In this sample, the
	 * button is labeled with a text 'GO'. Alternatively, you can specify an
	 * icon that is shown on the button. See AbstractCustomGlobal.CustomButton
	 * for more details.
	 */
	@AbstractCustomGlobal.CustomButton(buttonText = "Coloring", toolTipText = "A sample button")
	public void sampleButton() {
		inicializaVertices();
	}

	private void determinaPrioridade() {

		// Obt�m a lista das dist�ncias dos vizinhos de um salto de cada v�rtice
		for (MyNode node : myNodes) {
			node.setVizinhos(retornaListaDistancias(node.ID));

		}

		// Calcular a prioridade de cada v�rtice ou seja ele ordena o vetor pela
		// quantidade de vizinhos de um salto (sort)

		myNodes.sort(new ComparadorDeNos());

	}

	private ArrayList<Integer> retornaListaDistancias(int vertice) {
		ArrayList<Integer> vizinhos = new ArrayList<>();

		// Analisa-se todos os v�rtices, exceto o v�rtice passado como par�metro
		for (MyNode node : myNodes) {
			if (node.ID != myNodes.get(vertice).ID) {

				// C�lculo da Dist�ncia Euclidiana

				double distancia = Math
						.sqrt(Math.pow((myNodes.get(vertice).getPosition().xCoord - node.getPosition().xCoord), 2)
								+ Math.pow((myNodes.get(vertice).getPosition().yCoord - node.getPosition().yCoord), 2));

				// Se estiver dentro do alcance do sensor, ent�o � vizinho de um
				// salto
				if (distancia <= raio) {
					vizinhos.add(node.ID);
				}
			}

		}
		return vizinhos;
	}

	// metodo responsavel por adicionar todos os nos, chamar o metodos de
	// colorir estancia de vetor delta*4, imprimir todos os nos com seus
	// respectivos vizinhos

	public void inicializaVertices() {

		// calcula a area total

		double ladoArea = Tools.getGraphPanel().getHeight();

		// faz a instancia da lista onde ira conter todos os nos
		myNodes = new ArrayList<>();
		MyNode newNode;
		Random random = new Random();

		// remove todos os componente da tela
		Tools.removeAllNodes();

		// esse loop � responsavel por identificar quantos n�s ter�o na execu��o
		// e em seguinda realiza o teste de posicao de cada um dos n�s
		for (int id = 0; id < 30; id++) {
			newNode = new MyNode();

			// Determina aleatoriamente as posi��es x, y. O +10 � para nao
			// adicionar nenhum no na borda da area
			double posicaoX = (random.nextDouble() * ladoArea) + 10;
			double posicaoY = (random.nextDouble() * ladoArea) + 10;

			// Determina um posi��o �nica no plano, ou seja, para n�o haver dois
			// n� na mesma posi��o
			boolean achou = false;
			while (!achou) {

				// Procura por v�rtices que estejam nas mesmas posi��es
				for (int v = 0; v < myNodes.size() && !achou; v++) {
					if (posicaoX == myNodes.get(v).getPosition().xCoord
							&& posicaoY == myNodes.get(v).getPosition().yCoord)
						achou = true;
				}

				// Se achar, determina uma nova rodada de n�s randomicamente e
				// analisar novamente
				if (achou) {
					posicaoX = (random.nextDouble() * ladoArea) + 10;
					posicaoY = (random.nextDouble() * ladoArea) + 10;
					achou = false;
				} else
					achou = true;
			}

			//seta a posi��o para o no
			newNode.setPosition(posicaoX, posicaoY, 0);
			newNode.ID = id;

			// Cria um v�rtice e adiciona-o na lista
			newNode.finishInitializationWithDefaultModels(true);
			myNodes.add(newNode);

		}

		// metodo responsavel por ordenar e determinar que a prioridade � do no
		// que tem a maior quantidade de vizinhos
		determinaPrioridade();

		// esse vetor � o (delta*4) ou seja, o vetor que tem o tamanho da
		// quantidade de vizinhos do no com a maior prioridade
		//cores = (myNodes.get(0).getVizinhos().size() > 0) ? new Color[myNodes.get(0).getVizinhos().size() * 4]
			//	: new Color[1];
		int quantidadeCores = (myNodes.get(0).getVizinhos().size() > 0) ? myNodes.get(0).getVizinhos().size() * 4 : 1;	
		// metodo responsavel por realizar a adi��o de todas as cores no vetor
		CriarVetorDeCores(quantidadeCores);

		// determina as cores de cada no
		determiarCor();

		Tools.getGraphPanel().forceDrawInNextPaint();
		Tools.getGraphPanel().repaint();

		// imprimo cada no com seus respectivos vizinhos
		// =============================================
		// N� ATUAL ID: 14
		// COR: 16344315
		// _____________________________________________
		// VIZINHO ID: 23
		// COR: 3135670
		// =============================================

		ImprimirNo();

	}

	/**
	 * metodo responsavel por imprimir todos os vizinhos de cada no que pasasr
	 * pelo parametro
	 * 
	 * @param node
	 */
	private void imprimirVizinho(MyNode node) {

		for (int vizinho : node.getVizinhos()) {

			System.out.print(String.format("%-50s", "_____________________________________________"));
			System.out.println();
			System.out.print(String.format("%10s", "VIZINHO"));
			System.out.println();
			for (MyNode nodeTwo : myNodes) {
				if (vizinho == nodeTwo.ID) {
					System.out.print(String.format("%10s", "ID: "));
					System.out.print(String.format("%-5s", vizinho));
					System.out.println();
					
					// System.out.print(String.format("%10s", "COR: "));
					// System.out
					// .print(String.format("%-10s",
					// nodeTwo.getColor().toString().replace("java.awt.Color",
					// "")));
					// System.out.println();
					// System.out.print(String.format("%10s", "OU"));
					// System.out.println();
					
					System.out.print(String.format("%10s", "COR: "));
					System.out.print(String.format("%-10s", nodeTwo.getColor().getRGB() * (-1)));
					System.out.println();
					System.out.print(String.format("%-5s", "POSICAO X: "));
					System.out.print(String.format("%-5s", nodeTwo.getPosition().xCoord));
					System.out.println();
					System.out.print(String.format("%-5s", "POSICAO Y: "));
					System.out.print(String.format("%-5s", nodeTwo.getPosition().yCoord));

					System.out.println();
					break;
				}

			}

		}
	}

	/**
	 * metodo responsavel por realizar a impressao de cada no
	 */
	private void ImprimirNo() {

		System.out.print(String.format("%-50s", "#================ RESULTADO ================#"));
		System.out.println();
		System.out.print(String.format("%-5s", "Quantidade de Cores Utilizadas para Colorir a Rede: "));
		System.out.print(String.format("%-5s", cores.size()));
		System.out.println("\n");
		for (int i = 0; i < myNodes.size(); i++) {
			System.out.print(String.format("%-10s", "N� ATUAL"));
			System.out.println();
			System.out.print(String.format("%-5s", "ID: "));
			System.out.print(String.format("%-5s", myNodes.get(i).ID));
			System.out.println();
			// System.out.print(String.format("%-5s", "COR: "));
			//
			// System.out
			// .print(String.format("%-10s",
			// myNodes.get(i).getColor().toString().replace("java.awt.Color",
			// "")));
			// System.out.println();
			// System.out.print(String.format("%-5s", "OU"));
			// System.out.println();
			System.out.print(String.format("%-5s", "COR: "));
			System.out.print(String.format("%-10s", myNodes.get(i).getColor().getRGB() * (-1)));
			System.out.println();
			System.out.print(String.format("%-5s", "POSICAO X: "));
			System.out.print(String.format("%-5s", myNodes.get(i).getPosition().xCoord));
			System.out.println();
			System.out.print(String.format("%-5s", "POSICAO Y: "));
			System.out.print(String.format("%-5s", myNodes.get(i).getPosition().yCoord));
			System.out.println();
			imprimirVizinho(myNodes.get(i));
			System.out.print(String.format("%-50s", "============================================="));
			System.out.println();
		}
	}

	/**
	 * metodo responsavel por colorir os nos e seus vizinhos
	 */
	private void determiarCor() {
		
		// atributo o qual serve de posicao para recuperar a cor no array de
		// cores
		int corSelecionada;
		
		// loop varre cada no realizando a colora��o atual e dos seus vizinhos
		for (MyNode node : myNodes) {
			
			// atribuindo uma posicao aleatoria para recuperar uma cor do array
			// de cores
			corSelecionada = randomDeCores();
			
			// metodo responsavel por realizar a colora��o do no atual "node"
			pintarNoAtual(corSelecionada, node);
			
			// metodo responsavel por realizar a colora��o dos vizinhos do no
			// atual "node"
			PintarVizinhoNew(node, corSelecionada);
		}

	}

	private void PintarVizinhoNew(MyNode node, int corSelecionada) {
		int posicaoVizinho = -1;

		// atributo que serve de teste para saber se tem alguma cor igual
		boolean corIgual = true;

		// for responsavel por varrer todos os vizinhos de cada no
		for (int vizinho : node.getVizinhos()) {

			// atributo passado por parametro que serve de ponteiro para
			// recuperar a cor
			corSelecionada = randomDeCores();

			// atribuindo true para garantir que cada vizinho ira entrar no
			// while
			corIgual = true;

			// o while garante que enquanto n�o adicionar uma cor que n�o seja a
			// cor igual aos seus vizinhos ou at� mesmo a seu no atual
			while (corIgual) {

				// realiza o teste para saber se a cor que foi recuperada �
				// igual a alguma cor que j� tenha em algum no. Se n�o tiver
				// nenhuma cor igual a cor selecionada ele retorna false
				corIgual = conflitoCoresNumber(node, corSelecionada);

				// realiza o teste se essa cor n�o tem conflito com alguma cor
				// j� inserida nos vizinhos
				if (!corIgual) {

					// realiza o processo de colora��o dos vizinhos
					posicaoVizinho = AcharId(vizinho);

					// testa se o no vizinho atual est� pitado caso n�o esteja
					// ele realiza a pintura
					if (posicaoVizinho >= 0) {
						if (!myNodes.get(posicaoVizinho).isColored()) {

							// adiciona a cor ao no
							// nodeTwo.setColor(cores[corSelecionada]);
							myNodes.get(posicaoVizinho).setColor(cores.get(corSelecionada));

							// informa que o no j� esta colorido
							myNodes.get(posicaoVizinho).setColored(true);
						}
					}
				}

				// caso a cor esteja em uso ja ele gera uma nova cor e volta
				// para o
				corSelecionada = randomDeCores();

			}

		}
	}

	/**
	 * realiza o teste de valida��o de cada vizinho, ou seja, ele testa todos os
	 * vizinhos e o no atual desse vizinho para saber se tem algum n� com a
	 * mesma cor
	 * 
	 * 
	 * @param node
	 * @param corSelecionada
	 * @return
	 */
	@Deprecated
	private boolean conflitoCores(MyNode node, int corSelecionada) {

		// pega todos os vizinhos do no atual
		for (int vizinho : node.getVizinhos()) {

			// realiza o teste para saber se tem algum vizinho com a cor
			// escolhida; se tiver alguma cor igual retorna true
			if (cores.get(corSelecionada).getRGB() == node.getColor().getRGB()
					| myNodes.get(vizinho).getColor().getRGB() == cores.get(corSelecionada).getRGB()) {
				return true;
			}
		}

		// caso n�o tenha nenhum n� com cor igual a atual ele retorna false
		return false;
	}

	private boolean conflitoCoresNumber(MyNode node, int corSelecionada) {
		int posicaoNode = -1;

		// pega todos os vizinhos do no atual
		for (int vizinho : node.getVizinhos()) {

			// realiza o teste para saber se tem algum vizinho com a cor que foi
			// sorteada se tiver alguma cor igual retorna true
			posicaoNode = AcharId(vizinho);
			if (posicaoNode >= 0) {

				if (cores.get(corSelecionada).getRGB() == node.getColor().getRGB()
						| myNodes.get(posicaoNode).getColor().getRGB() == cores.get(corSelecionada).getRGB()) {
					return true;
				}

			}
		}

		// caso n�o tenha nenhum no com cor igual a atual ele retorna false
		return false;
	}

	/**
	 * realiza o teste de valida��o de cada vizinho, ou seja, ele testa todos os
	 * viziho e o no de atual desse vizinho para saber se tem algum com a mesma
	 * com que foi gerada
	 * 
	 * @param node
	 * @param corSelecionada
	 * @return
	 */
	@Deprecated
	private boolean conflitoCoresNoAtual(MyNode node, int corSelecionada) {

		// pega todos os vizinhos do no atual
		for (int vizinho : node.getVizinhos()) {

			// realiza o teste para saber se tem algum vizinho com a cor que foi
			// sorteada se tiver alguma cor igual retorna true
			if (myNodes.get(vizinho).getColor().getRGB() == cores.get(corSelecionada).getRGB()) {
				return true;
			}
		}

		// caso n�o tenha nenhum no com cor igual a atual ele retorna false
		return false;
	}

	private boolean conflitoCoresNoAtualInteiro(MyNode node, int corSelecionada) {
		int posicaoNode = -1;

		// pega todos os vizinhos do no atual
		for (int vizinho : node.getVizinhos()) {

			// retorna a posi��o com o no que tenha o id igual
			posicaoNode = AcharId(vizinho);

			if (posicaoNode >= 0) {

				// realiza o teste para saber se tem algum vizinho com a cor que
				// foi
				// sorteada se tiver alguma cor igual retorna true
				if (myNodes.get(posicaoNode).getColor().getRGB() == cores.get(corSelecionada).getRGB()) {
					return true;
				}
			}
		}

		// caso n�o tenha nenhum no com cor igual a atual ele retorna false
		return false;
	}

	private int AcharId(int idVizinho) {

		for (int i = 0; i < myNodes.size(); i++) {
			if (myNodes.get(i).ID == idVizinho) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * realiza a colora��o do no atual
	 * 
	 * @param corSelecionada
	 * @param node
	 */

	private void pintarNoAtual(int corSelecionada, MyNode node) {
		boolean existe = true;

		// testa para saber se esse n� atual ja esta colorido
		if (!node.isColored()) {

			while (existe) {
				System.out.println("Loop No atual");

				// teste para ver se tem algum vizinho com a cor que foi
				// selecionada para colorir o no atual
				existe = conflitoCoresNoAtualInteiro(node, corSelecionada);

				if (!existe) {

					// colori o no atual
					// node.setColor(cores[corSelecionada]);
					node.setColor(cores.get(corSelecionada));

					// informa que o no j� foi colorido
					node.setColored(true);

					break;
				}

				corSelecionada = randomDeCores();

			}
		}
	}

	/**
	 * metodo responsavel por retornar aleatoriamente a posi��o da cor
	 * 
	 * @return
	 */
	private int randomDeCores() {
		Random aleatorio = new Random();
		int posicao = aleatorio.nextInt(cores.size());
		return posicao;
	}

	/**
	 * criar vetores de cores de delta * 4 obs: n�o entendi
	 * 
	 * @param cores
	 */
	private void CriarVetorDeCores(int quantidadeCores) {
cores = new ArrayList<Color>();
		Random aleatorio = new Random();

		int r;
		int g;
		int b;
		Color cor;

		// adiciona a cor em todas as posi�oes do vetor de cores
		for (int i = 0; i < quantidadeCores; i++) {
			r = aleatorio.nextInt(256);
			g = aleatorio.nextInt(256);
			b = aleatorio.nextInt(256);
			cor = new Color(r, g, b);
			
			// Determina uma cor �nica na lista, ou seja, para n�o haver duas cores repetidas
			while (cores.contains(cor)) {
				r = aleatorio.nextInt(256);
				g = aleatorio.nextInt(256);
				b = aleatorio.nextInt(256);
				cor = new Color(r, g, b);
			}
			cores.add(cor);
		

		}

		System.out.println("#========== Dados de Entrada da Rede ==========#");
		// System.out.print(String.format("%-5s", "Quantidade de Cores
		// Utilizadas: "));
		// System.out.print(String.format("%-5s", cores.length));
		System.out.println();
		System.out.print(String.format("%-5s", "Quantidade de N�s na Rede: "));
		System.out.print(String.format("%-5s", myNodes.size()));
		System.out.println("\nRaio de Alcance: " + raio);

		System.out.println();
		System.out.print(String.format("%-5s", "Quantidade de Cores Utilizadas: "));
		System.out.print(String.format("%-5s", cores.size()));

		System.out.println();
	}

	public void preRound() {
	}

	public void postRound() {
	}

	public void onExit() {
		System.out.println();
		System.out.println("Fim");
	}

}
