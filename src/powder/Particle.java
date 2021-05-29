package powder;

import java.awt.Color;
import java.util.function.Consumer;


/**
 * Particle
 *
 * This is the base particle class that represents a single pixel on the screen
 *
 *
 */
public class Particle {


	protected static CartesianGrid<Particle> grid = new CartesianGrid<Particle>(new Particle[600][600]);
	// Only modified through setters after Particle initialization
	private int gridX, gridY;
	private double preciseX, preciseY;
	private int particleID;

	double gravity = -0.5;

	// velocity:
	double velX, velY = 0;

	public Color color = Color.white;

	double downPush = 0.0;
	public boolean updated = false;

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
		if (!outOfBounds(gridX + x,gridY + y)) 
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
	* 
	* relGate takes a 3x3 array of ParticleGates (booleans) of surrounding particles
	* relative to this one. Use null if the value does not matter. Use null or "____"
	* for 'this' Particle.
	*
	* @param ANDGate 3x3 2D array of ParticleGate enum / null values
	* @param type GateType ALL / ANY (AND / OR)
	* @return boolean true if real conditions match gate conditions else false
	 */
	public boolean relGate(ParticleGate[][] Gate, GateType type) {

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {

				// null values are skipped
				// can use null or "____" to refer to 'this' particle in the array
				if (Gate[i][j] == null || Gate[i][j] == ParticleGate.____) {
					continue;
				}

				// map iteration counters to relative coordinates
				int y = (i * -1)+1;
				int x = j -1;
				
				// check the value of the current cell 
				switch (Gate[i][j]) {
					case TRUE:
					// Particle must exist
						if (type == GateType.ALL && !relParticleExists(x, y))
						// ALL returns early if it finds a bad match
							return false;

						if (type == GateType.ANY && relParticleExists(x, y)) {
						// ANY returns early if it finds a good match
							return true;
						}
						continue;

					case FALSE:
					// Particle must not exist
						if (type == GateType.ALL && relParticleExists(x, y)) 
							return false;
							
						if (type == GateType.ANY && !relParticleExists(x, y)) {
							return true;
						}
						continue;

					default:
						continue;
				}
				}
			}

		// ALL - no bad matches, return true
		if (type == GateType.ALL) 
			return true;

		// ANY - no good matches, return false
		return false;
	}

	
	/**
	* Update the particle's position on the particle grid given precise coordinates
	* @param x X coordinate
	* @param y Y coordinate
	 */
	public void setNewPosition(double x, double y) {
		if (!outOfBounds((int)x,(int)y)) {
			grid.set(gridX, gridY, null);
			preciseX = x;
			preciseY = y;
			gridX = (int) x;
			gridY = (int) y;
			grid.set(gridX, gridY, this);
			
		}
	}

	public static void setGridSize(int width, int height) {
		grid = new CartesianGrid<Particle>(new Particle[width][height]);
	}

	public static CartesianGrid<Particle> getGrid() {
		return grid;
	}
	
	/**
	* Checks if arbitrary (x,y) coordinates fall within the bounds of the particle grid
	* @param x X coordinate
	* @param y Y coordinate
	* @return boolean true if coordinates fall outside else false
	 */
    public boolean outOfBounds(int x, int y) {

		return (x >= grid.W || y >= grid.H || x < 0 || y < 0);
    }
	
	/**
	 * Grid refresher that is looped through forever when application starts
	 * */
	public static void updateGrid() {

		// Iterate through the grid and update every pixel with a Particle
		forEachParticle(x -> x.update());

		// Iterate again and reset the updated flag for each Particle at its new
		// position
		forEachParticle(x -> x.updated = false);
	}


	public void update() {

	}
	
	/**
	* Helper method for mapping a function to every particle on the grid
	* @param action The lambda expression to use
	 */
	public static void forEachParticle(Consumer<Particle> action) {
		for (int x = 0; x < grid.W; x++) {
			for (int y = 0; y < grid.H; y++) {
				if (grid.get(x, y) != null)
					action.accept(grid.get(x,y));
			}
		}
	}
	
	/**
	 * Finds if a particle is on the ground or on another grounded particle
	 * @return boolean 
	 */
	public boolean supported() {
		if (gridY <= 0)
			return true;
		else if (!relParticleExists(0,-1))
			return false;
		else
			return getRelativeParticle(0,-1).supported();
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
