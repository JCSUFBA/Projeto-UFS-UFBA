import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;

public class ColoracaoGrafo {
	
	private int numVertices, numCoresDesejado;
	private int [] cores;
	private int [][] matrizAdjac;
	
	private static File arquivo;
	private static FileReader fr;
	private static BufferedReader br;
	private static LineNumberReader ln;
	
	// Permite colorir um grafo
	public void coloraGrafo(int [][] matriz, int numCoresDesejado){
		numVertices = matriz.length;
		this.numCoresDesejado = numCoresDesejado;
		cores = new int[numVertices];
		matrizAdjac = matriz;
		
		try {
			encontraSolucao(0);
			System.out.println("Não existe solução!!!");
		} catch (Exception e) {
			System.out.println("\nSolução existe!!!");
			mostraCores();
		}
	}
	
	// Encontra uma solução para a coloração do grafo
	public void encontraSolucao(int vertice) throws Exception {
		if(vertice == numVertices)
			throw new Exception("Solução encontrada!!!");
		
		for(int c = 1; c <= numCoresDesejado; c++) {
			if(ePossivel(vertice, c)){
				cores[vertice] = c;
				encontraSolucao(vertice + 1);
				cores[vertice] = 0;
			}
		}
	}
	
	// Verifica se é possível colorir um vértice com uma determinada cor
	public boolean ePossivel(int vertice, int cor) {
		for(int i = 0; i < numVertices; i++)
			if(matrizAdjac[vertice][i] == 1 && cor == cores[i])
				return false;
		return true;
	}
	
	// Mostra a coloração do grafo
	public void mostraCores() {
		System.out.print("\nCores: ");
		for (int i = 0; i < numVertices; i++)
			System.out.print(cores[i] + " ");
		System.out.println();
	}
	
	// Mostra grafo
	public void mostraGrafo() {
		System.out.print("\nMatriz:\n");
		for (int i = 0; i < numVertices; i++){
			for (int j = 0; j < numVertices; j++)
				System.out.print(matrizAdjac[i][j] + " ");
			System.out.println();
		}
	}
	
	public static void main (String [] args) {
		
		// Criando um objeto do tipo Scanner para ler entrada do teclado
		Scanner leitor = new Scanner(System.in);
		System.out.println("Algoritmo de Coloração de Grafo");
		
		// Instanciando objeto cg para colorir o grafo
		ColoracaoGrafo cg = new ColoracaoGrafo();
		
		// Lendo o caminho do arquivo
		System.out.println("Informe o caminho do arquivo: \n");
		String caminhoArquivo = leitor.nextLine();
		
		int[][] grafo = null;
		
		try{
			// Criando um objeto File para apontar para o arquivo
			arquivo = new File(caminhoArquivo);

			// Verificando se o arquivo existe 
			if(arquivo.exists()){
				
				// Obtendo a quantidade de Linhas / Vértices
				fr = new FileReader(arquivo);
				ln = new LineNumberReader(fr);
				ln.skip(arquivo.length());
				int numVertices = ln.getLineNumber() + 1;
				ln.close();
				fr.close();
				
				// Preparando para ler o arquivo
				fr = new FileReader(arquivo);
				br = new BufferedReader(fr);
				
				// Se for possível ler o arquivo, executará os comandos dentro do if
				if(br.ready()){

					// Instanciando a matriz de adjacência
					grafo = new int [numVertices][numVertices];
					
					int i = 0;
					while(br.ready() && i < numVertices){
						
						// Lendo a linha
						String[] linha = br.readLine().split(";");
						
						/*
						   Lançando uma exceção porque o arquivo é mal formatado
						   Verificação da quantidade de colunas com a quantidade de linhas
						*/
						if(numVertices != linha.length)
							throw new Exception("Arquivo Mal Formatado!!!");
						
						// Inicializando a matriz de adjacência
						for (int j = 0; j < numVertices; j++)
							grafo[i][j] = Integer.parseInt(linha[j]);
						i++;
					}
				}
				
				// Fechando os objetos de manipulação de arquivo
				br.close();
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Entre com o número de cores: ");
		int c = leitor.nextInt();
		leitor.close();
		cg.coloraGrafo(grafo, c);
	}
}