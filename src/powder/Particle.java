package powder;

import java.awt.Color;
import color.ColorGradientMap;

/**
 * Particle
 *
 * This is the base particle class that represents a single pixel on the screen
 *
 *
 */
public abstract class Particle {

	protected static ParticleGrid grid = new ParticleGrid(new Particle[600][600]);
	public static boolean showHeatMap = false;
	public static ColorGradientMap heatmap = new ColorGradientMap();
	public static ColorGradientMap slopemap = new ColorGradientMap();

	private int x, y;
	private double realx, realy;
	private int particleID;

	static double gravity = -0.5;

	// velocity:
	double velX, velY = 0;

	public Color color = Color.white;
	public Color displayColor = color;

	double downPush = 0.0;
	public boolean updated = false;

	public double temperature = 0;

	// Thermal diffusivity:
	public double thermDiff = 0.47;

	Particle(int x, int y) {
		particleID = Board.runtimeParticleCount++;
		this.x = x;
		this.y = y;
		this.realx = x;
		this.realy = y;
	}

	Particle(int x, int y, Color color) {

		particleID = Board.runtimeParticleCount++;
		this.x = x;
		this.y = y;
		this.realx = x;
		this.realy = y;
		this.color = color;
		displayColor = color;
	}

	static public void setGravity(double g) {
		Particle.gravity = g;
	}

	static public double getGravity() {
		return Particle.gravity;
	}

	static public void toggleHeatMap() {

		if (showHeatMap) {
			showHeatMap = false;
		} else {
			showHeatMap = true;
		}
	}

	public double realX() {
		return realx;
	}

	public double realY() {
		return realy;
	}

	public int Y() {
		return y;
	}

	public int X() {
		return x;
	}

	/**
	 * getID returns a unique particle identifier that can be used for logging
	 * 
	 * @return String particleID
	 */
	public String getID() {
		return String.format("P-%d", particleID);
	}

	// Returns a Particle relative to the position of this one

	/**
	 * Returns a Particle relative to the position of this one Takes regular
	 * Cartesian coordinates
	 *
	 * @param x Relative x position
	 * @param y Relative y position
	 * @return Particle
	 */
	public Particle getRel(int x, int y) {
		if (!grid.outOfBounds(X() + x, Y() + y))
			return grid.get(X() + x, Y() + y);
		else
			return null;
	}

	public double distanceFrom(double x, double y) {
		return Math.hypot(x - realX(), y - realY());

	}

	/**
	 * 
	 * Checks if a particle exists relative to this one and returns a boolean Takes
	 * regular Cartesian coordinates
	 * 
	 * @param x Relative x position
	 * @param y Relative y position
	 * @return boolean true if the particle exists else false
	 */
	public boolean testRel(int x, int y) {

		return getRel(x, y) != null;
	}

	/**
	 * Update the particle's position on the particle grid given precise coordinates
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setNewPosition(double x, double y) {
		if (!grid.outOfBounds((int) x, (int) y)) {
			grid.move(this, (int) x, (int) y);
			this.realx = x;
			this.realy = y;
			this.x = (int) x;
			this.y = (int) y;
		}
	}

	public static void setGridSize(int width, int height) {
		grid = new ParticleGrid(new Particle[width][height]);
	}

	public static ParticleGrid getGrid() {
		return grid;
	}

	abstract public void update();

	/**
	 * Updates the particle's temperature
	 */
	public void updateTemp() {
		double leftTemp, rightTemp, topTemp, bottomTemp;
		// if a side is not touching another particle, treats it as a particle of the
		// same temperature
		leftTemp = testRel(-1, 0) ? getRel(-1, 0).temperature : temperature;
		rightTemp = testRel(1, 0) ? getRel(1, 0).temperature : temperature;
		bottomTemp = testRel(0, -1) ? getRel(0, -1).temperature : temperature;
		topTemp = testRel(0, 1) ? getRel(0, 1).temperature : temperature;

		double xDifference = (leftTemp - temperature) - (temperature - rightTemp);
		double yDifference = (bottomTemp - temperature) - (temperature - topTemp);

		temperature += (xDifference + yDifference) * thermDiff;

		if (showHeatMap) {

			displayColor = heatmap.getColor(temperature);
		} else
			displayColor = color;
	}

	/**
	 * Finds if a particle is on the ground or on another grounded particle
	 * 
	 * @return boolean
	 */
	public boolean supported() {
		return true;
	}
	
	public void dispSlope() {
		double m = getAboveSlope();
		color = slopemap.getColor(m);
	}
	
	public double getAboveSlope() {
		if (testRel(0,1))
			return getRel(0,1).getAboveSlope();
		else
			return slope();
	}
	
	/**
	 * Calculates the slope of the surface below the particle.
	 * 
	 * Also I hate this method and never want to mess with it again. I hope I never
	 * have to fix something with it.
	 * 
	 * @return slope
	 */
	public double slope() {
		double slope = 0;
		int scope = 5;
		if (testRel(0, 1) && testRel(0, -1))
			return 0;

		int distToLeftEdge = 0;
		boolean leftGoesUp = false;
		for (int x = -1; x >= -scope; x--) {
			if (testRel(x, 0)) {
				distToLeftEdge = x + 1;
				leftGoesUp = true;
				break;
			}
			if (!testRel(x, -1)) {
				distToLeftEdge = x + 1;
				leftGoesUp = false;
				break;
			}
		}
		distToLeftEdge = Math.abs(distToLeftEdge);

		int distToRightEdge = 0;
		boolean rightGoesUp = false;
		for (int x = 1; x <= scope; x++) {
			if (testRel(x, 0)) {
				distToRightEdge = x - 1;
				rightGoesUp = true;
				break;
			}
			if (!testRel(x, -1)) {
				distToRightEdge = x - 1;
				rightGoesUp = false;
				break;
			}
		}
		distToRightEdge = Math.abs(distToRightEdge);

		if (leftGoesUp == rightGoesUp)
			return 0;

		int totalRun = distToLeftEdge + distToRightEdge + 1;

		slope = 1. / totalRun;
		if (!rightGoesUp)
			slope *= -1;

		if (Math.abs(slope) == 1 || !testRel(0, -1)) {
			if (testRel(-1, 0) == testRel(1, 0))
				return 0;

			int distToBottomEdge = 0;
			boolean onRightSide = testRel(1, 0);
			for (int y = -1; y >= -scope; y--) {
				if (testRel(0, y)) {
					distToBottomEdge = y + 1;
					break;
				}
			}
			distToBottomEdge = Math.abs(distToBottomEdge);

			int distToTopEdge = 0;
			for (int y = 1; y <= scope; y++) {
				if (!testRel(1, y) && onRightSide) {
					distToTopEdge = y - 1;
					break;
				} else if (!testRel(-1, y) && !onRightSide) {
					distToTopEdge = y - 1;
					break;
				}
			}
			distToTopEdge = Math.abs(distToTopEdge);

			int totalRise = distToBottomEdge + distToTopEdge + 1;
			slope = totalRise;
			if (!onRightSide)
				slope *= -1;
		}

		return slope;

	}

}
