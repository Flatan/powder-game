package powder;

import java.awt.Color;

/**
 * Particle
 *
 * This is the base particle class that represents a single pixel on the screen
 *
 *
 */
public class Particle {

	protected static Particle[][] grid = new Particle[600][600];

	// Only modified through setters after Particle initialization
	private int gridX, gridY;
	private double preciseX, preciseY;

	double gravity = 0.5;

	// velocity:
	double velX, velY = 0;

	public Color color = Color.white;

	double downPush = 0.0;
	public boolean updated = false;

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
		if (!outOfBounds(gridX + x,gridY - y)) 
			return grid[gridX + x][gridY - y];
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
		if (!outOfBounds((int)x,(int)y)) {
			grid[gridX][gridY] = null;
			preciseX = x;
			preciseY = y;
			gridX = (int) x;
			gridY = (int) y;
			grid[gridX][gridY] = this;
		}
	}

	public static void setGridSize(int width, int height) {
		grid = new Particle[width][height];
	}

	public static Particle[][] getGrid() {
		return grid;
	}
	
	/**
	* Checks if arbitrary (x,y) coordinates fall within the bounds of the particle grid
	* @param x X coordinate
	* @param y Y coordinate
	* @return boolean true if coordinates fall outside else false
	 */
    public boolean outOfBounds(int x, int y) {

        return !(x < grid[0].length && y < grid.length && x >= 0 && y >= 0);

    }
	
	// Grid refresher that is looped through forever when application starts
	public static void updateGrid() {

		// Iterate through the grid and update every pixel with a Particle
		for (int x = 0; x < grid[0].length; x++) {
			for (int y = 0; y < grid.length; y++) {
				if (grid[x][y] != null)
					grid[x][y].update();
			}
		}

		// Iterate again and reset the updated flag for each Particle at its new
		// position
		for (int x = 0; x < grid[0].length; x++) {
			for (int y = 0; y < grid.length; y++) {
				if (grid[x][y] != null)
					grid[x][y].updated = false;
			}
		}
	}

	Particle(int x, int y) {
		grid[x][y] = this;
		gridX = x;
		gridY = y;
		preciseX = gridX;
		preciseY = gridY;
	}

	Particle(int x, int y, Color color) {
		grid[x][y] = this;
		gridX = x;
		gridY = y;
		preciseX = gridX;
		preciseY = gridY;
		this.color = color;
	}

	public void update() {

	}

}
