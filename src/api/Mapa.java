package api;

/**
 * Classe responsável por gerar um para auto-organizável utilizando o algoritmo de KOHONEN.
 * 
 * @author Leonardo Thizon Waterkemper
 *
 */
public class Mapa {

	private double taxaAprendizagem = 0.5;	
	private byte raio = 0;
	private double reducaoLinear;
	
	private short numNeuroniosEntrada, numNeuroniosSaida;
	public double[][] pesosW;
	
	public byte[] entradaX, grupos;
	
	public Mapa(short numNeuroniosEntrada, short numeroNeuroniosSaida, double taxaAprendizagem, byte raio, double reducaoLinear) {
		
		this.numNeuroniosEntrada = numNeuroniosEntrada;
		this.numNeuroniosSaida = numeroNeuroniosSaida;
		this.taxaAprendizagem = taxaAprendizagem;
		this.raio = raio;
		this.reducaoLinear = reducaoLinear;
		
		pesosW = new double[numNeuroniosEntrada][numeroNeuroniosSaida];
		
		pesosW = new double[][]{ {0.2,0.8}, {0.6,0.4}, {0.5,0.7}, {0.9,0.3}};
		
//		Random random = new Random();
//		// Inicializa pesos w
//		for (int i = 0; i < numNeuroniosEntrada; i++) {
//			for (int j = 0; j < numNeuroniosSaida; j++) {
//				pesosW[i][j] = (double) Math.round( (random.nextDouble() * 2 - 1) * 100) / 100;
//			}
//		}
		
	}
	
	public void treinar(byte[] entradaX, byte[] grupos) {
		
		this.grupos = grupos;
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
		
		for (int x = 0; x < numNeuroniosEntrada; x++) {

			pesosW[x][grupo] += taxaAprendizagem * (entradaX[x] - pesosW[x][grupo]);

		}
		
		
	}
	
	public void ajustarTaxa() {
		taxaAprendizagem = reducaoLinear * taxaAprendizagem;
	}
	
	public int testar(int[] entradaX) {
		
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
