package math;

/**
 * Useful math functions
 *
 */
public final class Funct {
	private Funct() {};
	
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
}
