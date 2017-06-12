package api;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalImagensPB {
	
	private List<Integer> persons = new ArrayList<>();
	
	private int person;
	private byte imageIndex = 1;
	
	private Mapa mapa = null;
	
	public static void main(String[] args) {
		new PrincipalImagensPB().iniciar();
	}
	
	public void iniciar() {
		
		persons.add(1);
		persons.add(2);
		persons.add(3);
		persons.add(4);
		persons.add(5);
		persons.add(6);
		persons.add(7);
		persons.add(8);
		persons.add(9);
		persons.add(10);
		persons.add(11);
		persons.add(12);
		persons.add(13);
		persons.add(14);
		persons.add(15);
		
		// inicializa informando o núm de neuronios de entrada, saída (num grupos), taxa de aprendizagem, raio e redução linear
		mapa = new Mapa(10304, 15, 0.2, (byte) 0, 0);		
		treinar();		
		
	}
	
	private void treinar()  {
		
		try {
			
			int epocas = 0;
			// condição de saída, considerando um máximo de épocas
			while(epocas < 50) {
			
				for(int j = 0; j < persons.size(); j++) {
					
					person = persons.get(j);
					imageIndex = 1;
						
					// percorre os números de entrada por coluna
					for (int i = 1; i <= 10; i++) {
						
						PGMImage pgm = carregarImagemTreinamentoPGM(person);
						mapa.treinar(pgm.criarVetorEntrada());
						
					}
					
				}
				
				mapa.ajustarTaxa();
				
				epocas++;
				
			}
			
			// ao final do treinamento, realiza o teste
			testar();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void testar() {
		
		try {
			
			int imgIndex = 1;
			
			for(int j = 0; j < persons.size(); j++) {
				
				person = persons.get(j);
				imageIndex = 1;
				
				// percorre os números de entrada por coluna
				for (int i = 20; i <= 20; i++) {
					
					PGMImage pgm = carregarImagemTreinamentoPGM(person);
					
					int grupo = mapa.testar(pgm.criarVetorEntrada());
					
					File f = new File("resultado/"+grupo+"/");
					f.mkdirs();
					
					f = new File("resultado/"+grupo+"/"+ imgIndex++ +".pgm");
					File fSource = new File(pgm.caminhoImagem);
					
					Files.copy(fSource.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
					
				}
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	private PGMImage carregarImagemTreinamentoPGM(int person) {
		
		try {
			String caminhoImagem = "orl_faces/s"+person+"/"+imageIndex+".pgm";
            InputStream f = new FileInputStream(caminhoImagem);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();    // first line contains P2 or P5
            String line = d.readLine();     // second line contains height and width
            while (line.startsWith("#")) {
                line = d.readLine();
            }
            Scanner s = new Scanner(line);
            int width = s.nextInt();
            int height = s.nextInt();
            line = d.readLine();// third line contains maxVal
            s.close();
            s = new Scanner(line);
            int maxVal = s.nextInt();
            s.close();
            short[][] im = new short[height][width];

            int count = 0;
            int b = 0;
            try {
                while (count < height*width) {
                    b = d.read() ;
                    if ( b < 0 ) 
                        break ;

                    if (b == '\n') { // do nothing if new line encountered
                    } 
//                  else if (b == '#') {
//                      d.readLine();
//                  } 
//                  else if (Character.isWhitespace(b)) { // do nothing if whitespace encountered
//                  } 
                    else {
                        if ( "P5".equals(magic) ) { // Binary format
                            im[count / width][count % width] = (byte)((b >> 8) & 0xFF);
                            count++;
                            im[count / width][count % width] = (byte)(b & 0xFF);
                            count++;
                        }
                        else {  // ASCII format
                            im[count / width][count % width] = (byte)b ;
                            count++;
                        }
                    }
                }
            } catch (EOFException eof) {
                eof.printStackTrace(System.out) ;
            }
            d.close();
            
            PGMImage pgm = new PGMImage();
            pgm.magic = magic;
            pgm.width = width;
            pgm.height = height;
            pgm.maxVal = maxVal;
            pgm.image = im;
            pgm.caminhoImagem = caminhoImagem;
            return pgm;
            
        }
        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
		
	}
	
//	private BufferedImage pgmToJPG(PGMImage pgm) {
//		
//		short[] myImage = pgm.criarVetorEntrada();
//
//		int width = pgm.width;
//		int height = pgm.height;
//		
//		BufferedImage im = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
//		WritableRaster raster = im.getRaster();
//		for(int h=0;h<height;h++)
//		{
//		    for(int w=0;w<width;w++)
//		    {
//		    	int valorPx = myImage[h * width + w];
//		    	int[] pxArray = new int[]{valorPx,valorPx,valorPx,0};
//		    	raster.setPixel(w, h, pxArray);
////		        raster.setSample(w,h,0, valorPx); 
//		    }
//		}
//		im.setData(raster);
//		return im;
//		
////		ByteArrayOutputStream myJpg = new ByteArrayOutputStream();
////		javax.imageio.ImageIO.write(im, "jpg", myJpg);
//		
//	}

}
