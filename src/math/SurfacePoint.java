package math;
/**
 * Represents a point along a surface
 */
public class SurfacePoint {
	enum Side {ABOVE, BELOW, LEFT, RIGHT}
	private double slope = 0;
	
	//The side of the surface that is solid
	private Side solidOn = Side.ABOVE;
	
	SurfacePoint(double slope, Side side){
		this.slope = slope;
		solidOn = side;
	}
	
	
	/**
	 * Creates a surface with the specified slope and which is solid in the direction of the specified vector.
	 * <Code>solidOn</Code> is <Code>null</Code> if magnitude is 0.
	 * @param slope
	 * @param side
	 */
	SurfacePoint(double slope, Vector2D side){
		this.slope = slope;
		if (side.y > 0)
			solidOn = Side.ABOVE;
		else if (side.y < 0)
			solidOn = Side.BELOW;
		else if (side.x > 0)
			solidOn = Side.RIGHT;
		else if (side.x < 0)
			solidOn = Side.LEFT;
		else solidOn = null;
	}
	
	/**
	 * Gets the 2D unit vector that is normal to the surface at this point
	 * @return the normal vector
	 */
	public Vector2D getNormal() {
		double angle;
		switch (solidOn) {
		case ABOVE:
			angle = Math.atan(-1/slope)+Math.PI/2;
			break;
		case BELOW:
			angle = Math.atan(-1/slope)-Math.PI/2;
			break;
		case RIGHT:
			angle = Math.atan(slope);
			break;
		case LEFT:
			angle = Math.atan(slope)-Math.PI;
			break;
		default:
			return new Vector2D();
		}
		return new Vector2D(Math.cos(angle),Math.sin(angle));
		
	}

	
	public double getSlope() {
		return slope;
	}

	public Side getSolidOn() {
		return solidOn;
	}

	
}
