import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;

public class Main {

	static int ITERATIONS = 255;
	static double IMAGE_X = 4000;
	static double IMAGE_Y = 4000;
	static double ZOOM = 750.0;
	static double CENTRE_X = -0.747;
	static double CENTRE_Y = 0.0996;

	static BufferedImage output = new BufferedImage((int)IMAGE_X,(int)IMAGE_Y,BufferedImage.TYPE_INT_RGB);

	public static void main(String args[])
	{
		
		for(int x = 0; x < (int)IMAGE_X; x ++){

			for (int y = 0; y < (int)IMAGE_Y; y++){

				double real = ((x - ( IMAGE_X/2)) * (3 / IMAGE_X) / ZOOM) + CENTRE_X;
				double img =  ((y - (IMAGE_Y/2)) * (3 / IMAGE_Y) / ZOOM) - CENTRE_Y;

				int escape = check(new Complex(real,img));

				if(escape == ITERATIONS+1) output.setRGB(x,y,0x000000);
				else output.setRGB(x, y, new Color(escape%255,escape%255,0).getRGB());

				if(x == IMAGE_X/2) output.setRGB(x, y, Color.WHITE.getRGB());
				if(y == IMAGE_Y/2) output.setRGB(x, y, Color.WHITE.getRGB());

			}

		}

		File f = new File("mandelbrot.png");
		try { ImageIO.write(output, "PNG", f); }
		catch(IOException e){System.out.println("Failed to print");};

	}

	static int check(Complex c)
	{

		Complex z = new Complex(0.0, 0.0);
		int escape = ITERATIONS+1;

		for(int k = 1; k <= ITERATIONS; k++){

			z = z.multiply(z);
			z = z.add(c);

			if((z.getReal()*z.getReal()) + (z.getImaginary()*z.getImaginary()) > 2){
				escape = k;
				break;
			}

		}

		return escape;
	}

}