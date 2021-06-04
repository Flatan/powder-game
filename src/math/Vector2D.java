package math;

public class Vector2D {
	public double x,y;
	
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2D addend) {
		x+=addend.x;
		y+=addend.y;
	}
	
	public double dotProduct(Vector2D multiplier) {
		return x*multiplier.x + y*multiplier.y;
	}
	
	public Vector2D normalizedVect() {
		return new Vector2D(x/magnitude(),y/magnitude());
	}
	
	public double magnitude() {
		return Math.hypot(x, y);
	}
	
	/**
	 * @return the angle in radians
	 */
	public double angle() {
		return Math.atan2(x, y);
	}
}
