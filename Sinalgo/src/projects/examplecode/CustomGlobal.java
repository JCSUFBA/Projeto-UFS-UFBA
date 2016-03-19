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
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.tools.Tool;

import com.sun.tracing.dtrace.ArgsAttributes;

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
	private final double raio = 100.0;
	Color[] cores;

	/*
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
	@AbstractCustomGlobal.CustomButton(buttonText = "Delta + 1 Coloring", toolTipText = "A sample button")
	public void sampleButton() {
		inicializaVertices();
	}

	private void determinaPrioridade() {
		// Obtém a lista de distâncias dos vizinhos de um salto de cada vértice
		for (MyNode node : myNodes) {
			node.setVizinhos(retornaListaDistancias(node.ID));

		}
		/**
		 * Calcular a prioridade de cada vértice ou seja ele ordena o vertor
		 * pela quantidade de vizinhos
		 */

		myNodes.sort(new ComparadorDeNos());

	}

	private HashMap<Integer, Double> retornaListaDistancias(int vertice) {
		HashMap<Integer, Double> vizinhos = new HashMap<Integer, Double>();

		// Analisa-se todos os vértices, exceto o vértice passado como parâmetro
		for (MyNode node : myNodes) {
			if (node.ID != myNodes.get(vertice).ID) {
				// Cálculo da Distância Euclidiana

				double distancia = Math
						.sqrt(Math.pow((myNodes.get(vertice).getPosition().xCoord - node.getPosition().xCoord), 2)
								+ Math.pow((myNodes.get(vertice).getPosition().yCoord - node.getPosition().yCoord), 2));

				// Se estiver dentro do alcance do sensor, então é vizinho de um
				// salto
				if (distancia <= raio) {
					vizinhos.put(node.ID, distancia);
				}
			}

		}
		return vizinhos;
	}

	/**
	 * metodo responsavel por adicionar todos os nos, chamar o metodos de
	 * colorir estancia de vetor delta*4, imprimir todos os nos com seus
	 * respectivos vizinhos
	 */
	public void inicializaVertices() {
		/**
		 * calcula a area total
		 */
		double ladoArea = Tools.getGraphPanel().getHeight();
		/**
		 * faz a estancia da linsta onde ira conter todos os nos
		 */
		myNodes = new ArrayList<>();
		MyNode newNode;
		Random random = new Random();
		/**
		 * remove todos os componente da tela
		 */
		Tools.removeAllNodes();
		/**
		 * esse loop é respnsavel por identificar quantos nos vai ter e em
		 * seguinda realiza o teste de posicao de cada um
		 */
		for (int id = 0; id < 10; id++) {
			newNode = new MyNode();
			/**
			 * Determina aleatoriamente as posições o +10 é para nao adicionar
			 * nenhum no na borda da area
			 */

			double posicaoX = (random.nextDouble() * ladoArea) + 10;
			double posicaoY = (random.nextDouble() * ladoArea) + 10;

			// Determina um posição única no plano
			boolean achou = false;
			while (!achou) {
				// Procura por vértices que estejam nas mesmas posições
				for (int v = 0; v < myNodes.size() && !achou; v++) {
					if (posicaoX == myNodes.get(v).getPosition().xCoord
							&& posicaoY == myNodes.get(v).getPosition().yCoord)
						achou = true;
				}

				// Se achar, determina uma nova e analisa-se novamente
				if (achou) {
					posicaoX = (random.nextDouble() * ladoArea) + 10;
					posicaoY = (random.nextDouble() * ladoArea) + 10;
					achou = false;
				} else
					achou = true;
			}
			// System.out.println(posicaoX + " - " + posicaoY);
			newNode.setPosition(posicaoX, posicaoY, 0);
			newNode.ID = id;
			// Cria um vértice e adiciona-o na lista

			newNode.finishInitializationWithDefaultModels(true);
			myNodes.add(newNode);

		}
		/**
		 * metodo responsavel por ordenar e determinar que a prioridade seria o
		 * no que tem a maior quantidade de vizinhos
		 */
		determinaPrioridade();
		/**
		 * esse vetor é o (delta*4) ou seja, o vetor tem o tamanho da quantide
		 * de vizinhos do no com a maior prioridade
		 */
		cores = (myNodes.get(0).getVizinhos().size() > 0) ? new Color[myNodes.get(0).getVizinhos().size() * 4]
				: new Color[1];
		/**
		 * metodo respnsavel por realizar a adição de todas as cores no vetor
		 */
		CriarVetorDeCores();
		/**
		 * determina as cores de cada no
		 */
		determiarCor();
		Tools.getGraphPanel().forceDrawInNextPaint();
		Tools.getGraphPanel().repaint();

		/**
		 * imprimo cada no com seus respsctivos vizinhos
		 * ============================================= NÓ ATUAL ID: 14 COR:
		 * [r=6,g=155,b=5] OU COR: 16344315
		 * _____________________________________________ VIZINHO ID: 23 COR:
		 * [r=208,g=39,b=74] OU COR: 3135670
		 * =============================================
		 */
		ImprimirNo();

	}

	/**
	 * metodo responsavel por imprimir todos os vizinhos de cada no que pasasr
	 * pelo parametro
	 * 
	 * @param node
	 */
	private void imprimirVizinho(MyNode node) {

		for (int vizinho : node.getVizinhos().keySet()) {

			System.out.print(String.format("%-50s", "_____________________________________________"));
			System.out.println();
			System.out.print(String.format("%10s", "VIZINHO"));
			System.out.println();
			for (MyNode nodeTwo : myNodes) {
				if (vizinho == nodeTwo.ID) {
					System.out.print(String.format("%10s", "ID: "));
					System.out.print(String.format("%-5s", vizinho));
					System.out.println();
					System.out.print(String.format("%10s", "COR: "));
					System.out
							.print(String.format("%-10s", nodeTwo.getColor().toString().replace("java.awt.Color", "")));
					System.out.println();
					System.out.print(String.format("%10s", "OU"));
					System.out.println();
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
		for (int i = 0; i < myNodes.size(); i++) {
			System.out.print(String.format("%-10s", "NÓ ATUAL"));
			System.out.println();
			System.out.print(String.format("%-5s", "ID: "));
			System.out.print(String.format("%-5s", myNodes.get(i).ID));
			System.out.println();
			System.out.print(String.format("%-5s", "COR: "));

			System.out
					.print(String.format("%-10s", myNodes.get(i).getColor().toString().replace("java.awt.Color", "")));
			System.out.println();
			System.out.print(String.format("%-5s", "OU"));
			System.out.println();
			System.out.print(String.format("%-5s", "COR: "));
			System.out.print(String.format("%-10s", myNodes.get(i).getColor().getRGB() * (-1)));
			System.out.println();
			System.out.print(String.format("%-5s", "POSICAO X: "));
			System.out.print(String.format("%-5s", myNodes.get(i).getPosition().xCoord));
			System.out.println();
			System.out.print(String.format("%-5s", "POSICAO Y: "));
			System.out.print(String.format("%-5s", myNodes.get(i).getPosition().yCoord));
			System.out.println();
			// System.out.println("ID: " + mn.get(i).ID + " Cor: " +
			// (mn.get(i).getColor().getRGB()* (-1)));

			imprimirVizinho(myNodes.get(i));
			System.out.print(String.format("%-50s", "============================================="));
			System.out.println();
		}
	}

	/**
	 * metodo responsavel por pintar os nos e seus vizinhos
	 */
	private void determiarCor() {
		/**
		 * atributo o qual serve de posicao para recuperar a cor no array de
		 * cores
		 */
		int corSelecionada;
		/**
		 * loop varre cada no realizando a pintura do tal e dos seus vizinhos
		 */
		for (MyNode node : myNodes) {
			/**
			 * atribuindo uma posicao aleatoria para recuperar uma cor do array
			 * de cores
			 */
			corSelecionada = randomDeCores();

			/**
			 * metodo responsavel por realizar a pintura do no atual "node"
			 */
			pintarNoAtual(corSelecionada, node);

			/**
			 * metodo responsavel por realizar a pintura dos vizinhos do no
			 * atual "node"
			 */
			PintarVizinhoNew(node, corSelecionada);

		}

	}

	private void PintarVizinhoNew(MyNode node, int corSelecionada) {

		/**
		 * atributo serve de teste para saber se tem alguma cor igual
		 */
		boolean corIgual = true;
		/**
		 * for responsavel por varrer todos os vizinhos de cada noo
		 */
		for (int vizinho : node.getVizinhos().keySet()) {
			/**
			 * atributo passado por parametro que serve de ponteiro para
			 * recuperar a cor
			 */
			corSelecionada = randomDeCores();
			/**
			 * atribuindo true para garantir que cada vizinho ira entrar no
			 * while
			 */
			corIgual = true;
			/**
			 * o while garante que enquanto não adicionar uma cor que não seja a
			 * cor iguail aos seus vizinhos ou até mesmo a seu no atual
			 */
			while (corIgual) {

				/**
				 * realiza o teste para saber se a cor que foi recuperada é
				 * igual a alguma cor que já tenha em algum no se não tiver
				 * nenhuma cor igual a selecionada ele retorna false
				 */
				corIgual = conflitoCores(node, corSelecionada);
				/**
				 * relaiza o teste se essa cor não tem conflito com alguma já
				 * inserida nos vizinhos
				 */
				if (!corIgual) {
					/**
					 * realiza o processo de coloração dos vizinhos
					 */
					for (MyNode nodeTwo : myNodes) {

						if (nodeTwo.ID == vizinho) {
							/**
							 * testa se o no vizinho atual está pitado caso não
							 * esteja ele realiza a pintura
							 */
							if (!nodeTwo.isColored()) {
								/**
								 * adiciona a cor ao no
								 */
								nodeTwo.setColor(cores[corSelecionada]);
								/**
								 * informa que o no já esta colorido
								 */
								nodeTwo.setColored(true);
								// /**
								// * adiciona ao vetor de cores o id da cor
								// pintada
								// * #desnecessario#
								// */
								// cores[corSelecionada].setId(nodeTwo.ID);
								// /**
								// * informa que a cor já foi usada
								// * #desnecessario#
								// */
								// cores[corSelecionada].setUsada(true);
								/**
								 * sai do while e vai para o proximo vizinho
								 */
								corIgual = false;
								break;
							}

						}

					}

				}

				/**
				 * caso a cor esteja em uso ja ele gera uma nova cor e volta
				 * para o
				 */
				corSelecionada = randomDeCores();

			}

		}
	}

	/**
	 * realiza o teste de validação de cada vizinho, ou seja, ele testa todos os
	 * viziho e o no de atual desse vizinho para saber se tem algum com a mesma
	 * com que foi gerada
	 * 
	 * @param node
	 * @param corSelecionada
	 * @return
	 */
	private boolean conflitoCores(MyNode node, int corSelecionada) {
		/**
		 * pega todos os vizinhos do no atual
		 */
		for (int vizinho : node.getVizinhos().keySet()) {
			/**
			 * realiza o teste para saber se tem algum vizinho com a cor que foi
			 * sorteada se tiver alguma cor igual retorna true
			 */
			if (cores[corSelecionada].getRGB() == node.getColor().getRGB()
					| myNodes.get(vizinho).getColor().getRGB() == cores[corSelecionada].getRGB()) {
				return true;
			}
		}
		/**
		 * caso não tenha nenhum no com cor igual a atual ele retorna false
		 */
		return false;
	}
	/**
	 * realiza o teste de validação de cada vizinho, ou seja, ele testa todos os
	 * viziho e o no de atual desse vizinho para saber se tem algum com a mesma
	 * com que foi gerada
	 * 
	 * @param node
	 * @param corSelecionada
	 * @return
	 */
	private boolean conflitoCoresNoAtual(MyNode node, int corSelecionada) {
		/**
		 * pega todos os vizinhos do no atual
		 */
		for (int vizinho : node.getVizinhos().keySet()) {
			/**
			 * realiza o teste para saber se tem algum vizinho com a cor que foi
			 * sorteada se tiver alguma cor igual retorna true
			 */
			if (myNodes.get(vizinho).getColor().getRGB() == cores[corSelecionada].getRGB()) {
				return true;
			}
		}
		/**
		 * caso não tenha nenhum no com cor igual a atual ele retorna false
		 */
		return false;
	}

	/**
	 * realiza a pintura do no atual
	 * 
	 * @param corSelecionada
	 * @param node
	 */

	private void pintarNoAtual(int corSelecionada, MyNode node) {
		boolean existe = true;
		/**
		 * testa para saber se esse no atual ja esta pintado
		 */
		if (!node.isColored()) {
			
			while (existe) {

				/**
				 * testa ver se tem algum vizinho com a cor que foi selecionada para
				 * pintar o no atual
				 */
				existe = conflitoCoresNoAtual(node, corSelecionada);

				if (!existe) {
					/**
					 * pinta o no atual
					 */
					node.setColor(cores[corSelecionada]);
					/**
					 * informa que o no já foi pintado
					 */
					node.setColored(true);				
					
					break;
				}

				corSelecionada = randomDeCores();
				
			}
		}
	}

	/**
	 * metodo responsavel por retornar aleatoriamente a posição da cor
	 * 
	 * @return
	 */
	private int randomDeCores() {
		Random aleatorio = new Random();
		int posicao = aleatorio.nextInt(cores.length);
		return posicao;
	}

	/**
	 * criar vetores de cores de delta * 4
	 * 
	 * @param cores
	 */
	private void CriarVetorDeCores() {

		Random aleatorio = new Random();

		int r;
		int g;
		int b;
		Color cor;
		/**
		 * adiciona a cor em todas as posiçoes do vetor de cores
		 */
		for (int i = 0; i < cores.length; i++) {

			r = aleatorio.nextInt(256);
			g = aleatorio.nextInt(256);
			b = aleatorio.nextInt(256);
			cor = new Color(r, g, b);

			cores[i] = cor;

		}
		
		System.out.print(String.format("%-5s", "Quantidade de Cores Utilizadas: "));
		System.out.print(String.format("%-5s", cores.length));
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
