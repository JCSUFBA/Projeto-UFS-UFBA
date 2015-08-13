package utils;

import model.VerticeCent;
import model.VerticeDist;

/**
 * Classe respons�vel pela ordena��o do vetor de v�rtices.
 */
public class Utilidade {
	
	/**
	 * M�todo Quicksort que ordena um vetor de v�rtices em ordem descrescente pela prioridade
	 * e em ordem crescente pelo identificador.
	 * @param vertices - vetor de v�rtices a ser ordenado.
	 * @param inicio - posi��o inicial do vetor.
	 * @param fim - posi��o final do vetor.
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
	 * M�todo Quicksort que ordena um vetor de v�rtices em ordem descrescente pela prioridade
	 * e em ordem crescente pelo identificador.
	 * @param vertices - vetor de v�rtices a ser ordenado.
	 * @param inicio - posi��o inicial do vetor.
	 * @param fim - posi��o final do vetor.
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
