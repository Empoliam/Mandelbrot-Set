import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	static int ITERATIONS = 768;
	static double IMAGE_X = 3200;
	static double IMAGE_Y = 3200;
	static double ZOOM = 2000000000.0;
	static double CENTRE_X = -0.3739983244;
	static double CENTRE_Y = -0.65974973925;
	static boolean CROSSHAIR = false;
	
	static int THREADS = Runtime.getRuntime().availableProcessors();

	static BufferedImage output = new BufferedImage((int)IMAGE_X,(int)IMAGE_Y,BufferedImage.TYPE_INT_RGB);

	public static void main(String args[]) {

		long START = System.nanoTime();

		int yMin = 0;
		int yMax = (int) (IMAGE_Y/THREADS);

		Thread[] threads = new Thread[THREADS];

		for(int k = 0; k < THREADS; k++) {

			if( k == THREADS - 1) { yMax = (int) IMAGE_Y; }
			
			threads[k] = new Thread(new CalcThread(yMin, yMax));
			threads[k].start();
			
			yMin += IMAGE_Y/THREADS;
			yMax += IMAGE_Y/THREADS;

		}

		for(int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}

		long CALC = System.nanoTime();
		System.out.println((CALC-START)/1000000);
		
		File f = new File("mandelbrot.png");
		try { ImageIO.write(output, "PNG", f); }
		catch(IOException e){System.out.println("Failed to print"); };

		long END = System.nanoTime();
		
		System.out.println((END - CALC) / 1000000);
		System.out.println((END-START)/1000000);

	}

	static class CalcThread implements Runnable {

		private int yMax;
		private int yMin;

		public CalcThread(int yMin, int yMax) {
			this.yMin = yMin;
			this.yMax = yMax;
		}

		@Override
		public void run() {

			for(int x = 0; x < (int)IMAGE_X; x ++){

				for (int y = yMin; y < (int)yMax; y++){

					double real = ((x - ( IMAGE_X/2)) * (3 / IMAGE_X) / ZOOM) + CENTRE_X;
					double img =  ((y - (IMAGE_Y/2)) * (3 / IMAGE_Y) / ZOOM) - CENTRE_Y;

					int escape = check(new Imaginary(real,img));

					if(escape == ITERATIONS+1) output.setRGB(x,y,0x000000);
					else if(escape >= 512) output.setRGB(x, y, new Color(255,escape%256,255).getRGB());
					else if(escape >= 256) output.setRGB(x, y, new Color(escape%256,0,255).getRGB());
					else output.setRGB(x, y, new Color(0,0,escape%256).getRGB());

					if(CROSSHAIR){
						if(x == IMAGE_X/2) output.setRGB(x, y, Color.WHITE.getRGB());
						if(y == IMAGE_Y/2) output.setRGB(x, y, Color.WHITE.getRGB());
					}

				}

			}

		}
		
	}

	static int check(Imaginary c)
	{

		Imaginary z = new Imaginary(0.0, 0.0);
		int escape = ITERATIONS+1;

		for(int k = 1; k <= ITERATIONS; k++){

			z = z.multiply(z);			
			z = z.add(c);

			if((z.r > 2.0 || z.i > 2.0)){
				escape = k;
				break;
			}

		}

		return escape;
	}

}
