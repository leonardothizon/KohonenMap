package api;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class PrincipalImagens {
	
	private List<short[]> entradas = new ArrayList<>();
	
	List<String> persons = new ArrayList<>();
	
	private String person = "";
	private String imageName = "";
	private byte imageIndex = 1;
	
	private Mapa mapa = null;
	
	public static void main(String[] args) {
		new PrincipalImagens().iniciar();
	}
	
	public void iniciar() {
		
		persons.add("adhast");
		persons.add("awjsud");
		persons.add("cadugd");
		
		// inicializa informando o núm de neuronios de entrada, saída, taxa de aprendizagem, raio e redução linear
		mapa = new Mapa(108000, 3, 0.6, (byte) 0, 0.7);		
		treinar();		
		
	}
	
	private void treinar()  {
		
		try {
			
			
			int epocas = 0;
			// condição de saída, considerando a taxa de acerto e um máximo de épocas
			while(epocas < 20) {
			
				for(int j = 0; j < persons.size(); j++) {
					
					person = persons.get(j);
					imageIndex = 1;
						
					// percorre os números de entrada por coluna
					for (int i = 1; i <= 20; i++) {
						
						BufferedImage imgTreinamento = carregarImagemTreinamento();
						short[] vetorEntrada = criarVetorEntrada(imgTreinamento);
						mapa.treinar(vetorEntrada);
						
					}
					
				}
				
				mapa.ajustarTaxa();
				
				epocas++;
				
			}
			
			// ao final do ciclo de treinamento, realiza o teste para verificar a taxa de acerto
			testar();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void testar() {
		
		try {
			
			for(int j = 0; j < persons.size(); j++) {
				
				person = persons.get(j);
				imageIndex = 1;
				
				// percorre os números de entrada por coluna
				for (int i = 0; i <= 20; i++) {
					
					BufferedImage imgTreinamento = carregarImagemTreinamento();
					short[] vetorEntrada = criarVetorEntrada(imgTreinamento);
					
					int grupo = mapa.testar(vetorEntrada);
					
					File f = new File("resultado/"+grupo+"/"+imageName);
					f.mkdirs();
					
					ImageIO.write(imgTreinamento, "jpg", f);
					
				}
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	private BufferedImage carregarImagemTreinamento() throws Exception {
		
		try {
			
			String imgName = person+"."+ imageIndex++ +".jpg";
			imageName = imgName;
			File f = new File("C:\\Desenvolvimento\\workspace\\KohonenMap\\faces95\\"+ person +"\\"+imgName);
			return ImageIO.read(f);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(imageName);
			JOptionPane.showMessageDialog(null, "Erro carregando imagem de treinamento");
			System.exit(0);
			throw e;
		}
		
	}
	
	private short[] criarVetorEntrada(BufferedImage imgTreinamento) throws Exception {
		
		try {
			
			int numeroPixels = (imgTreinamento.getWidth() * imgTreinamento.getHeight()) * 3;
			
			short[] entradaX = new short[numeroPixels];
			
			WritableRaster raster = imgTreinamento.getRaster();
			int pixel[] = new int[4];
			int k = 0;
			for (int j = 0; j < imgTreinamento.getHeight(); j++) {
				for (int i = 0; i < imgTreinamento.getWidth(); i++) {
				
					raster.getPixel(i, j, pixel);
					short valorPixelR = (short) pixel[0];
					short valorPixelG = (short) pixel[1];
					short valorPixelB = (short) pixel[2];
					
					entradaX[k++] = valorPixelR;
					entradaX[k++] = valorPixelG;
					entradaX[k++] = valorPixelB;
					
				}			
			}
			
			return entradaX;
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao carregar vetor de entrada");
			throw e;
		}
		
	}

}
