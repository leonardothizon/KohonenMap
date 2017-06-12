package api;

import java.util.Random;

/**
 * Classe responsável por gerar um para auto-organizável utilizando o algoritmo de KOHONEN.
 * 
 * @author Leonardo Thizon Waterkemper
 *
 */
public class Mapa {

	private double taxaAprendizagem = 0.5;	
	private byte raio = 0;
	private double reducaoLinear = 0;
	
	private int numNeuroniosEntrada, numNeuroniosSaida;
	public double[][] pesosW;
	
	public byte[] entradaX, grupos;
	
	public Mapa(int numNeuroniosEntrada, int numeroNeuroniosSaida, double taxaAprendizagem, byte raio, double reducaoLinear) {
		
		this.numNeuroniosEntrada = numNeuroniosEntrada;
		this.numNeuroniosSaida = numeroNeuroniosSaida;
		this.taxaAprendizagem = taxaAprendizagem;
		this.raio = raio;
		this.reducaoLinear = reducaoLinear;
		
// 		pesos do exemplo dos slides
//		pesosW = new double[][]{ {0.2,0.8}, {0.6,0.4}, {0.5,0.7}, {0.9,0.3}};
		
		pesosW = new double[numNeuroniosEntrada][numeroNeuroniosSaida];
		
		// Inicializa pesos w
		Random random = new Random();
		for (int i = 0; i < numNeuroniosEntrada; i++) {
			for (int j = 0; j < numNeuroniosSaida; j++) {
				pesosW[i][j] = (double) Math.round( (random.nextDouble() * 2 - 1) * 100) / 100;
			}	
		}
		
		// inicializa grupos conforme o número de neurônios de saída
		this.grupos = new byte[numNeuroniosEntrada];
		for(int i = 0; i < numNeuroniosSaida; i++) {
			grupos[i] = (byte) i;
		}
		
	}
	
	public void treinar(short[] entradaX) {
		
		int grupo = -1;
		double menorDistancia = -1;
		
		for (int z = 0; z < numNeuroniosSaida; z++) {

			double distancia = 0;

			for (int x = 0; x < numNeuroniosEntrada; x++) {

				double w = pesosW[x][z];
				distancia += Math.pow((w - entradaX[x]), 2);

			}
			
			if(menorDistancia == -1) {
				menorDistancia = distancia;
				grupo = z;
			} else if (distancia < menorDistancia) {
				menorDistancia = distancia;
				grupo = z;
			}
			

		}
		
		System.out.println(grupo);
		for (int x = 0; x < numNeuroniosEntrada; x++) {
			
			// atualiza pesos vizinhança conforme o raio. Será atualizado também o peso do grupo vencedor
			for(int j = (grupo - raio); j <= (grupo + raio); j++) {
				
				if(j >= 0 && j < numNeuroniosSaida ) {
					pesosW[x][j] += taxaAprendizagem * (entradaX[x] - pesosW[x][j]);
				}
				else if (j < 0) {
					int idxJ = numNeuroniosSaida + j;
					pesosW[x][idxJ] += taxaAprendizagem * (entradaX[x] - pesosW[x][idxJ]);
				}
				else if (j >= numNeuroniosSaida) {
					int idxJ = j - numNeuroniosSaida;
					pesosW[x][idxJ] += taxaAprendizagem * (entradaX[x] - pesosW[x][idxJ]);
				}
				
			}

		}
		
		
	}
	
	public void ajustarTaxa() {
		if(reducaoLinear > 0)
			taxaAprendizagem = reducaoLinear * taxaAprendizagem;
	}
	
	public int testar(short[] entradaX) {
		
		int grupo = -1;
		double menorDistancia = -1;
		
		for (int z = 0; z < numNeuroniosSaida; z++) {

			double distancia = 0;

			for (int x = 0; x < numNeuroniosEntrada; x++) {

				double w = pesosW[x][z];
				distancia += Math.pow((w * entradaX[x]), 2);

			}
			
			if(menorDistancia == -1) {
				menorDistancia = distancia;
				grupo = z;
			} else if (distancia < menorDistancia) {
				menorDistancia = distancia;
				grupo = z;
			}

		}
		
		return grupos[grupo];
		
	}
	
	// método para debug
	public void exibirPesos() {
		
		for (int x = 0; x < numNeuroniosEntrada; x++) {

			for (int z = 0; z < numNeuroniosSaida; z++) {

				double w = pesosW[x][z];
				System.out.print(w + "     ");

			}
			
			System.out.println();

		}
		
	}
	
}
