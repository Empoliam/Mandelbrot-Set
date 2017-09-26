import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	static int ITERATIONS = 512;
	static double IMAGE_X = 3000;
	static double IMAGE_Y = 3000;
	static double ZOOM = 1000.0;
	static double CENTRE_X = -0.747;
	static double CENTRE_Y = 0.1005;
	static boolean CROSSHAIR = true;

	static BufferedImage output = new BufferedImage((int)IMAGE_X,(int)IMAGE_Y,BufferedImage.TYPE_INT_RGB);

	public static void main(String args[])
	{

		long START = System.nanoTime();

		for(int x = 0; x < (int)IMAGE_X; x ++){

			for (int y = 0; y < (int)IMAGE_Y; y++){

				double real = ((x - ( IMAGE_X/2)) * (3 / IMAGE_X) / ZOOM) + CENTRE_X;
				double img =  ((y - (IMAGE_Y/2)) * (3 / IMAGE_Y) / ZOOM) - CENTRE_Y;

				int escape = check(new Imaginary(real,img));

				if(escape == ITERATIONS+1) output.setRGB(x,y,0x000000);
				else if(escape >= 256) output.setRGB(x, y, new Color(escape%256,0,255).getRGB());
				else output.setRGB(x, y, new Color(0,0,escape%256).getRGB());

				if(CROSSHAIR){
					if(x == IMAGE_X/2) output.setRGB(x, y, Color.WHITE.getRGB());
					if(y == IMAGE_Y/2) output.setRGB(x, y, Color.WHITE.getRGB());
				}

			}

		}

		File f = new File("mandelbrot.png");
		try { ImageIO.write(output, "PNG", f); }
		catch(IOException e){System.out.println("Failed to print"); };

		long END = System.nanoTime();

		System.out.println((END - START) / 1000000000);

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
