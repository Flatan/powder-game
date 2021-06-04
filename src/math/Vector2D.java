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
	
	
	/**
	 * For two given point particles of specified constant velocities and initial positions, returns an array of length two containing:
	 * <ul>
	 * <li>the time after the start at which both points are nearest to one another
	 * <li>the distance between both particles at that time
	 * </ul>
	 * @param x1	the x position of particle 1
	 * @param y1	the y position of particle 1
	 * @param vel1	the velocity vector of particle 1
	 * @param x2	the x position of particle 2
	 * @param y2	the y position of particle 2
	 * @param vel2	the velocity vector of particle 2
	 * @return the array of doubles
	 */
	public static double[] timeClosest(double x1, double y1, Vector2D vel1, double x2, double y2, Vector2D vel2) {
		double[] output = new double[2];
		if (vel1.equals(vel2))
			output[0] = 0;
		else {
			double xDiff = x1-x2;
			double yDiff = y1-y2;
			double xVelDiff = vel1.x - vel2.x;
			double yVelDiff = vel1.y - vel2.y;
			output[0] = Math.max(0, -(xDiff*xVelDiff + yDiff*yVelDiff)/(xVelDiff*xVelDiff + yVelDiff*yVelDiff));
		}
		output[1] = Math.hypot((x1 + vel1.x*output[0])-(x2 + vel2.x*output[0]),
							   (y1 + vel1.y*output[0])-(y2 + vel2.y*output[0]));
		return output;
		
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
