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
	
	/**
	 * multiplies the vector by a scalar
	 * @param multiplier
	 */
	public void multiply(double multiplier) {
		x *= multiplier;
		y *= multiplier;
	}
	
	public double dotProduct(Vector2D multiplier) {
		return x*multiplier.x + y*multiplier.y;
	}
	
	/**
	 * Returns the unit vector in the same direction as this one
	 * @return the normalized vector
	 */
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

	@Override
	public String toString() {
		return String.format("[%.3f, %.3f]", x,y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2D other = (Vector2D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
	public Vector2D clone() {
		return new Vector2D(x,y);
		}
	
}
