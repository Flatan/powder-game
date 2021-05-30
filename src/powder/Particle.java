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
public class Particle {

	
	protected static ParticleGrid grid = new ParticleGrid(new Particle[600][600]);
	public static boolean showHeatMap = false;
	public static ColorGradientMap heatmap = new ColorGradientMap();
	// Only modified through setters after Particle initialization
	private int gridX, gridY;
	private double preciseX, preciseY;
	private int particleID;

	double gravity = -0.5;

	// velocity:
	double velX, velY = 0;

	public Color color = Color.white;
	public Color displayColor = color;

	double downPush = 0.0;
	public boolean updated = false;
	
	
	public double temperature=0;

	//Thermal diffusivity:
	public double thermDiff=0.47;

	Particle(int x, int y) {
		particleID = Board.runtimeParticleCount++;
		gridX = x;
		gridY = y;
		preciseX = gridX;
		preciseY = gridY;
	}

	Particle(int x, int y, Color color) {

		particleID = Board.runtimeParticleCount++;
		gridX = x;
		gridY = y;
		preciseX = gridX;
		preciseY = gridY;
		this.color = color;
		displayColor = color;
	}

	public double getX() {
		return preciseX;
	}

	public double getY() {
		return preciseY;
	}

	public int getGridY() {
		return gridY;
	}

	public int getGridX() {
		return gridX;
	}

	/**
	* getID returns a unique particle identifier that can be used for
	* logging
	* @return String particleID
	 */
	public String getID() {
		return String.format("P-%d", particleID);
	}
	
	// Returns a Particle relative to the position of this one
	
	/**
	* Returns a Particle relative to the position of this one
	* Takes regular Cartesian coordinates
	*
	* @param x Relative x position
	* @param y Relative y position
	* @return Particle
	 */
	public Particle getRelativeParticle(int x, int y) {
		if (!grid.outOfBounds(gridX + x,gridY + y)) 
			return grid.get(gridX +x, gridY+y);
		else
			return null;	
	}

	/**
	* 
	* Checks if a particle exists relative to this one and returns a boolean
	* Takes regular Cartesian coordinates
	* 
	* @param x Relative x position
	* @param y Relative y position
	* @return boolean true if the particle exists else false
	 */
	public boolean relParticleExists(int x, int y) {

		return getRelativeParticle(x, y) != null;
	}

	
	/**
	* Update the particle's position on the particle grid given precise coordinates
	* @param x X coordinate
	* @param y Y coordinate
	 */
	public void setNewPosition(double x, double y) {
		if (!grid.outOfBounds((int)x,(int)y)) {
			grid.move(this,(int)x,(int)y);
			preciseX = x;
			preciseY = y;
			gridX = (int) x;
			gridY = (int) y;	
		}
	}

	public static void setGridSize(int width, int height) {
		grid = new ParticleGrid(new Particle[width][height]);
	}

	public static ParticleGrid getGrid() {
		return grid;
	}
	
	/**
	 * Grid refresher that is looped through forever when application starts
	 * */
	public static void updateGrid() {

		// Iterate through the grid and update every pixel with a Particle
		grid.forEachParticle(x -> x.update());
		// Iterate again and reset the updated flag for each Particle at its new
		// position
		grid.forEachParticle(x -> x.updated = false);
	}


	public void update() {
		if (!updated) {
			updated = true;
			updateTemp();
		}
	}
	
	/**
	 * Updates the particle's temperature
	 */
	public void updateTemp() {
		double leftTemp, rightTemp, topTemp, bottomTemp;
		//if a side is not touching another particle, treats it as a particle of the same temperature
		leftTemp = relParticleExists(-1,0) ? getRelativeParticle(-1,0).temperature : temperature;
		rightTemp = relParticleExists(1,0) ? getRelativeParticle(1,0).temperature : temperature;
		bottomTemp = relParticleExists(0,-1) ? getRelativeParticle(0,-1).temperature : temperature;
		topTemp = relParticleExists(0,1) ? getRelativeParticle(0,1).temperature : temperature;
		
		double xDifference = (leftTemp-temperature)-(temperature-rightTemp);
		double yDifference = (bottomTemp-temperature)-(temperature-topTemp);
		
		
		temperature += (xDifference+yDifference)*thermDiff;
		

		if (showHeatMap) {

			displayColor = heatmap.getColor(temperature);
		}
		else
			displayColor = color;
	}
	
	
	/**
	 * Finds if a particle is on the ground or on another grounded particle
	 * @return boolean 
	 */
	public boolean supported() {
		return true;
	}
	
	/**
	 * Calculates the slope of the surface below the particle.
	 * 
	 * Also I hate this method and never want to mess with it again.
	 * I hope I never have to fix something with it.
	 * @return slope
	 */
	public double slope() {
		double slope = 0;
		int scope = 5;
		if(relParticleExists(0,1)&&relParticleExists(0,-1))
			return 0;
		
		int distToLeftEdge = 0;
		boolean leftGoesUp = false;
		for (int x = -1;x>=-scope;x--) {
			if (relParticleExists(x,0)) {
				distToLeftEdge = x+1;
				leftGoesUp = true;
				break;
			}
			if (!relParticleExists(x,-1)) {
				distToLeftEdge = x+1;
				leftGoesUp = false;
				break;
			}
		}
		distToLeftEdge = Math.abs(distToLeftEdge);
		
		int distToRightEdge = 0;
		boolean rightGoesUp = false;
		for (int x = 1;x<=scope;x++) {
			if (relParticleExists(x,0)) {
				distToRightEdge = x-1;
				rightGoesUp = true;
				break;
			}
			if (!relParticleExists(x,-1)) {
				distToRightEdge = x-1;
				rightGoesUp = false;
				break;
			}
		}
		distToRightEdge = Math.abs(distToRightEdge);
		
		if (leftGoesUp == rightGoesUp)
			return 0;
		
		int totalRun = distToLeftEdge+distToRightEdge+1;
		
		slope = 1./totalRun;
		if (!rightGoesUp)
			slope *= -1;
		
		
		if (Math.abs(slope) == 1 || !relParticleExists(0,-1)) {
			if(relParticleExists(-1,0)==relParticleExists(1,0))
				return 0;
			
			int distToBottomEdge = 0;
			boolean onRightSide = relParticleExists(1,0);
			for (int y = -1;y>=-scope;y--) {
				if (relParticleExists(0,y)) {
					distToBottomEdge = y+1;
					break;
				}
			}
			distToBottomEdge = Math.abs(distToBottomEdge);
			
			int distToTopEdge = 0;
			for (int y = 1;y<=scope;y++) {
				if (!relParticleExists(1,y)&&onRightSide) {
					distToTopEdge = y-1;
					break;
				}
				else if (!relParticleExists(-1,y)&&!onRightSide) {
					distToTopEdge = y-1;
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
