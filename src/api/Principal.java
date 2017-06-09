package api;

import java.util.ArrayList;
import java.util.List;

public class Principal {
	
	private List<byte[]> entradas = new ArrayList<>();
	
	private static byte[] t0 = new byte[]{1,1,0,0};
	private static byte[] t1 = new byte[]{0,0,0,1};
	private static byte[] t2 = new byte[]{1,0,0,0};
	private static byte[] t3 = new byte[]{0,0,1,1};
	
	private Mapa mapa = null;
	
	public static void main(String[] args) {
		new Principal().iniciar();
	}
	
	public void iniciar() {
		
		entradas.add(t0);
		entradas.add(t1);
		entradas.add(t2);
		entradas.add(t3);
		
		// inicializa informando o n�m de neuronios de entrada, sa�da, taxa de aprendizagem, raio e redu��o linear
		mapa = new Mapa((short) 4, (short) 2, 0.6, (byte) 0, 0.5);		
		treinar();		
		
	}
	
	private void treinar()  {
		
		try {
			
			
			int epocas = 0;
			// condi��o de sa�da, considerando a taxa de acerto e um m�ximo de �pocas
			while(epocas < 10) {
			
					
				// percorre os n�meros de entrada por coluna
				for (int i = 0; i < 4; i++) {
					
					byte[] grupos = new byte[]{1,2};
					
					mapa.treinar(entradas.get(i), grupos);
					mapa.exibirPesos();
					
				}
				
				mapa.ajustarTaxa();
				
				// ao final do clico de treinamento, realiza o teste para verificar a taxa de acerto
				testar();
				
				
				epocas++;
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void testar() {
		
		try {
		
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
