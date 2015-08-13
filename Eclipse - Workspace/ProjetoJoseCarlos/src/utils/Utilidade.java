package utils;

import model.VerticeCent;
import model.VerticeDist;

/**
 * Classe responsável pela ordenação do vetor de vértices.
 */
public class Utilidade {
	
	/**
	 * Método Quicksort que ordena um vetor de vértices em ordem descrescente pela prioridade
	 * e em ordem crescente pelo identificador.
	 * @param vertices - vetor de vértices a ser ordenado.
	 * @param inicio - posição inicial do vetor.
	 * @param fim - posição final do vetor.
	 */
	public static void ordenaListaVertices(VerticeCent[] vertices, int inicio, int fim)
	{
		int i = inicio, f = fim;
		VerticeCent pivo = vertices[fim];
		while (i <= f)
		{
			while (vertices[i].getPrioridade() > pivo.getPrioridade()
					|| (vertices[i].getPrioridade() == pivo.getPrioridade() && vertices[i].getIdentificador() < pivo.getIdentificador())
					)
				i++;
			while (vertices[f].getPrioridade() < pivo.getPrioridade()
					|| (vertices[f].getPrioridade() == pivo.getPrioridade() && vertices[f].getIdentificador() > pivo.getIdentificador())
					)
				f--;

			if (i <= f)
			{
				VerticeCent aux = vertices[i];
				vertices[i] = vertices[f];
				vertices[f] = aux;
				i++;
				f--;
			}
		}
		if (f > inicio)
			ordenaListaVertices(vertices, inicio, f);
		if (i < fim)
			ordenaListaVertices(vertices, i, fim);
	}
	
	/**
	 * Método Quicksort que ordena um vetor de vértices em ordem descrescente pela prioridade
	 * e em ordem crescente pelo identificador.
	 * @param vertices - vetor de vértices a ser ordenado.
	 * @param inicio - posição inicial do vetor.
	 * @param fim - posição final do vetor.
	 */
	public static void ordenaListaVertices(VerticeDist[] vertices, int inicio, int fim)
	{
		int i = inicio, f = fim;
		VerticeDist pivo = vertices[fim];
		while (i <= f)
		{
			while (vertices[i].getPrioridade() > pivo.getPrioridade()
					|| (vertices[i].getPrioridade() == pivo.getPrioridade() && vertices[i].getIdentificador() < pivo.getIdentificador())
					)
				i++;
			while (vertices[f].getPrioridade() < pivo.getPrioridade()
					|| (vertices[f].getPrioridade() == pivo.getPrioridade() && vertices[f].getIdentificador() > pivo.getIdentificador())
					)
				f--;

			if (i <= f)
			{
				VerticeDist aux = vertices[i];
				vertices[i] = vertices[f];
				vertices[f] = aux;
				i++;
				f--;
			}
		}
		if (f > inicio)
			ordenaListaVertices(vertices, inicio, f);
		if (i < fim)
			ordenaListaVertices(vertices, i, fim);
	}
}
