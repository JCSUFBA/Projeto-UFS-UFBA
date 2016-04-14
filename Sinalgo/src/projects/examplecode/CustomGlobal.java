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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


// Metodo de ordenação que compara o nó com maior numero de vizinhos
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
	String imprimir = String.format("%-50s", "#================ RESULTADO ================# \n");
	// define o meu raio de alcance
	private final double raio = 1000.0;

	//array onde será guardado o vetor com Delta*4 cores
	ArrayList<Color> cores;

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

		// Obtém a lista das distâncias dos vizinhos de um salto de cada vértice
		for (MyNode node : myNodes) {
			node.setVizinhos(retornaListaDistancias(node.ID));

		}

		// Calcular a prioridade de cada vértice ou seja ele ordena o vetor pela
		// quantidade de vizinhos de um salto (sort)

		myNodes.sort(new ComparadorDeNos());

	}

	///
	private ArrayList<Integer> retornaListaDistancias(int vertice) {
		ArrayList<Integer> vizinhos = new ArrayList<>();

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

		// esse loop é responsavel por identificar quantos nós terão na execução
		// e em seguinda realiza o teste de posicao de cada um dos nós
		for (int id = 0; id < 70; id++) {
			newNode = new MyNode();

			// Determina aleatoriamente as posições x, y. O +10 é para nao
			// adicionar nenhum no na borda da area
			double posicaoX = (random.nextDouble() * ladoArea) + 10;
			double posicaoY = (random.nextDouble() * ladoArea) + 10;

			// Determina um posição única no plano, ou seja, para não haver dois
			// nó na mesma posição
			boolean achou = false;
			while (!achou) {

				// Procura por vértices que estejam nas mesmas posições
				for (int v = 0; v < myNodes.size() && !achou; v++) {
					if (posicaoX == myNodes.get(v).getPosition().xCoord
							&& posicaoY == myNodes.get(v).getPosition().yCoord)
						achou = true;
				}

				// Se achar, determina uma nova rodada de nós randomicamente e
				// analisar novamente
				if (achou) {
					posicaoX = (random.nextDouble() * ladoArea) + 10;
					posicaoY = (random.nextDouble() * ladoArea) + 10;
					achou = false;
				} else
					achou = true;
			}

			// seta a posição para o no
			newNode.setPosition(posicaoX, posicaoY, 0);
			newNode.ID = id;

			// Cria um vértice e adiciona-o na lista
			newNode.finishInitializationWithDefaultModels(true);
			myNodes.add(newNode);

		}

		// metodo responsavel por ordenar e determinar que a prioridade é do no
		// que tem a maior quantidade de vizinhos
		determinaPrioridade();

		// esse vetor é o (delta*4) ou seja, o vetor que tem o tamanho da
		// quantidade de vizinhos do no com a maior prioridade
		
		int quantidadeCores = (myNodes.get(0).getVizinhos().size() > 0) ? myNodes.get(0).getVizinhos().size() * 4 : 1;
		// metodo responsavel por realizar a adição de todas as cores no vetor
		CriarVetorDeCores(quantidadeCores);
		setarTamanhoArray();
		// determina as cores de cada no
		determinarCor();

		Tools.getGraphPanel().forceDrawInNextPaint();
		Tools.getGraphPanel().repaint();

		ImprimirNo();

	}
	
	
//Array com o tamanho maximo da quantidade de vizinhos do nó (Delta)
	private void setarTamanhoArray() {

		for (MyNode no : myNodes) {
			no.setTamanhoVetorCores(myNodes.get(0).getVizinhos().size());
		}
	}

	/**
	 * metodo responsavel por imprimir todos os vizinhos de cada no que passar
	 * pelo parametro
	 * 
	 * @param node
	 */
	private void imprimirVizinho(MyNode node) {

		for (int vizinho : node.getVizinhos()) {

			imprimir += String.format("%-50s", "_____________________________________________ \n");

			imprimir += String.format("%10s", "VIZINHO \n");

			for (MyNode nodeTwo : myNodes) {
				if (vizinho == nodeTwo.ID) {
					imprimir += String.format("%10s", "ID: ");
					imprimir += String.format("%-5s", vizinho) + "\n";

					imprimir += String.format("%10s", "COR: ");
					imprimir += String.format("%-10s", nodeTwo.getColor().getRGB() * (-1)) + "\n";

					imprimir += String.format("%-5s", "POSICAO X: ");
					imprimir += String.format("%-5s", nodeTwo.getPosition().xCoord) + "\n";

					imprimir += String.format("%-5s", "POSICAO Y: ");
					imprimir += String.format("%-5s", nodeTwo.getPosition().yCoord) + "\n";

					break;
				}

			}

		}
	}

	/**
	 * metodo responsavel por realizar a impressao de cada no
	 */
	private void ImprimirNo() {

		imprimir += String.format("%-5s", "Quantidade de Cores Utilizadas para Colorir a Rede: ");
		imprimir += String.format("%-5s", cores.size()) + "\n";

		for (int i = 0; i < myNodes.size(); i++) {
			imprimir += String.format("%-10s", "NÓ ATUAL \n");

			imprimir += String.format("%-5s", "ID: ");
			imprimir += String.format("%-5s", myNodes.get(i).ID) + "\n";

			imprimir += String.format("%-5s", "COR: ");
			imprimir += String.format("%-10s", myNodes.get(i).getColor().getRGB() * (-1)) + "\n";
			imprimir += String.format("%-5s", "POSICAO X: ");
			imprimir += String.format("%-5s", myNodes.get(i).getPosition().xCoord) + "\n";

			imprimir += String.format("%-5s", "POSICAO Y: ");
			imprimir += String.format("%-5s", myNodes.get(i).getPosition().yCoord) + "\n";

			imprimirVizinho(myNodes.get(i));
			imprimir += String.format("%-50s", "============================================= \n");

		}
		imprimir += "Fim";
		escrever();

	}

	/**
	 * metodo responsavel por colorir os nos e seus vizinhos
	 */
	
	

	private void determinarCor() {

		// atributo o qual serve de posicao para recuperar a cor no array de
		// cores
		int corSelecionada;
		
		//Loop responsavel por realizar a coloração Delta cores
		for (int i = 0; i < myNodes.get(0).getCores().length; i++) {

			// loop varre cada no realizando a coloração atual e dos seus
			// vizinhos
			for (MyNode node : myNodes) {

				// atribuindo uma posicao aleatoria para recuperar uma cor do
				// array
				// de cores
				corSelecionada = randomDeCores();

				// metodo responsavel por realizar a coloração do no atual
				// "node"
				colorirNoAtual(corSelecionada, node);

				// metodo responsavel por realizar a coloração dos vizinhos do
				// no
				// atual "node"
				colorirVizinhoNew(node, corSelecionada);
			}
			resetarCor();
		}

	}

	//Reseta o atributo informando que não esta mais colorido
	private void resetarCor() {
		for (MyNode no : myNodes) {

			no.setColored(false);

		}
	}

	private void colorirVizinhoNew(MyNode node, int corSelecionada) {
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

			// o while garante que enquanto não adicionar uma cor que não seja a
			// cor igual aos seus vizinhos ou até mesmo a seu no atual
			while (corIgual) {

				// realiza o teste para saber se a cor que foi recuperada é
				// igual a alguma cor que já tenha em algum no. Se não tiver
				// nenhuma cor igual a cor selecionada ele retorna false
				corIgual = conflitoVizinho(vizinho, corSelecionada, node);

				// realiza o teste se essa cor não tem conflito com alguma cor
				// já inserida nos vizinhos
				if (!corIgual) {

					// realiza o processo de coloração dos vizinhos
					posicaoVizinho = AcharId(vizinho);

					// testa se o no vizinho atual está colorido caso não esteja
					// ele realiza a coloração
					if (posicaoVizinho >= 0) {
						if (!myNodes.get(posicaoVizinho).isColored()) {
							
								// adiciona a cor ao no
								
								myNodes.get(posicaoVizinho).setColor(cores.get(corSelecionada));

								// informa que o no já esta colorido
								myNodes.get(posicaoVizinho).setColored(true);
								myNodes.get(posicaoVizinho).setColorAtual(cores.get(corSelecionada));
								enviarMensagem(myNodes.get(posicaoVizinho));
								//cores.remove(corSelecionada);

							}
							break;
						
					}
				}

				// caso a cor esteja em uso ele gera uma nova cor e volta
				// para o
				corSelecionada = randomDeCores();

			}

		}
	}

	// Testa se existe conflito entre as cores dos vizinhos 
	 private boolean conflitoVizinho(int noVizinho, int corSelecionada, MyNode noAtualAovizinhoOrigem) {
		for (MyNode nodeAtual : myNodes) {
			
				if (noVizinho != nodeAtual.ID) {

					if (cores.get(corSelecionada).getRGB() == nodeAtual.getColor().getRGB()) {

						for (int vizinho : nodeAtual.getVizinhos()) {
							{
								if (vizinho == noVizinho) {
									return true;
								}
							}

						}
					}

				}

			
		}
		return false;

	}

	/**
	 * realiza o teste de validação de cada vizinho, ou seja, ele testa todos os
	 * vizinhos.
	 * @param node
	 * @param corSelecionada
	 * @return
	 */

	private boolean conflitoCoresNoAtualInteiro(MyNode node, int corSelecionada) {
		int posicaoNode = -1;
		
			// pega todos os vizinhos do no atual
			for (int vizinho : node.getVizinhos()) {

				// retorna a posição com o no que tenha o id igual
				posicaoNode = AcharId(vizinho);

				if (posicaoNode >= 0) {

					// realiza o teste para saber se tem algum vizinho com a cor
					// que
					// foi
					// sorteada se tiver alguma cor igual retorna true
					if (myNodes.get(posicaoNode).getColor().getRGB() == cores.get(corSelecionada).getRGB()) {
						return true;
					}
				}
			}
		
		// caso não tenha nenhum no com cor igual a atual ele retorna false
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
	 * realiza a coloração do no atual
	 * 
	 * @param corSelecionada
	 * @param node
	 */

	private void colorirNoAtual(int corSelecionada, MyNode node) {
		boolean existe = true;

		// testa para saber se esse nó atual ja esta colorido
		if (!node.isColored()) {

			while (existe) {

				// teste para ver se tem algum vizinho com a cor que foi
				// selecionada para colorir o no atual
				existe = conflitoCoresNoAtualInteiro(node, corSelecionada);

				if (!existe) {

					// colori o no atual
					// node.setColor(cores[corSelecionada]);
					if (cores.size() > 0) {
						node.setColor(cores.get(corSelecionada));

						// informa que o no já foi colorido
						node.setColored(true);
						node.setColorAtual(cores.get(corSelecionada));
						enviarMensagem(node);
						

					}
					break;
				}

				corSelecionada = randomDeCores();

			}
		}
	}
//Envia uma mensagem do no atual para todos os seus vizinhos
	private void enviarMensagem(MyNode noAtual) {

		for (int idVizinho : noAtual.getVizinhos()) {

			int posicao = AcharId(idVizinho);
			myNodes.get(posicao).sendColorMessage(noAtual.getColor(), noAtual);

		}

	}

	/**
	 * metodo responsavel por retornar aleatoriamente a posição da cor
	 * 
	 * @return
	 */
	private int randomDeCores() {
		Random aleatorio = new Random();
		int posicao = cores.size() > 0 ? aleatorio.nextInt(cores.size()) : 0;
		return posicao;
	}

	/**
	 * criar vetores de cores de delta * 4 
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

		// adiciona a cor em todas as posiçoes do vetor de cores
		for (int i = 0; i < quantidadeCores; i++) {
			r = aleatorio.nextInt(256);
			g = aleatorio.nextInt(256);
			b = aleatorio.nextInt(256);
			cor = new Color(r, g, b);

			// Determina uma cor única na lista, ou seja, para não haver duas
			// cores repetidas
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
		System.out.print(String.format("%-5s", "Quantidade de Nós na Rede: "));
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

	public void escrever() {

		try {
			File arquivo = new File("src/resultado.txt");
			FileWriter fw = new FileWriter(arquivo);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(imprimir);

			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {

		}

	}

	public void onExit() {

	}

}