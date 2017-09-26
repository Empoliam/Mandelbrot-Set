public class Imaginary {

	public double r;
	public double i;
	
	public Imaginary(double r, double i) {
		
		this.r = r;
		this.i = i;		
	}
	
	public Imaginary multiply(Imaginary zA) {
			
		Imaginary zB = new Imaginary(this.r, this.i);
		
		double a = zA.r * zB.r;
		double b = zA.r * zB.i;
		double c = zA.i * zB.r;
		double d = (zA.i * zB.i) * -1;
		
		zB.r = a + d;
		zB.i = b + c;
		
		return zB;	
	}
	
	public Imaginary add(Imaginary zA) {
	
		Imaginary zB = new Imaginary(this.r, this.i);
		
		zB.r = this.r + zA.r;
		zB.i = this.i + zA.i;
		
		return zB;
		
	}
}
