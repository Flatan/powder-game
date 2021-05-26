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
	private int gridX,gridY;
	private double preciseX, preciseY;

	double gravity = 0.5;
	
	//velocity:
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

	public double getGridY() {
		return gridY;
	}

	public double getGridX() {
		return gridX;
	}

	//  Update the particle position, only taking the preciseX and Y as input
	public void setNewPosition(double x, double y) {
		grid[gridX][gridY] = null;
		preciseX = x;
		preciseY = y;
		gridX = (int) x;
		gridY = (int) y;
		grid[gridX][gridY] = this;
	}

	
	public static void setGridSize(int width, int height) {
		grid = new Particle[width][height];
	}
	
	public static Particle[][] getGrid() {
		return grid;
	}

	// Grid refresher that is looped through forever when application starts
	public static void updateGrid() {

		// Iterate through the grid and update every pixel with a Particle
		for ( int x = 0; x < grid[0].length; x++ ) {
		for ( int y = 0; y < grid.length; y++ ) {
		  			if (grid[x][y] != null ) 
		  				grid[x][y].update();
		  	}}

		// Iterate again and reset the updated flag for each Particle at its new position
		for ( int x = 0; x < grid[0].length; x++ ) {
  		for ( int y = 0; y < grid.length; y++ ) {
  			if (grid[x][y] != null ) 
  				grid[x][y].updated = false;
  		}}
	}
	
	Particle(int x, int y){
		grid[x][y] = this;
		gridX = x;
		gridY = y;
		preciseX = gridX;
		preciseY = gridY;
	}
	
	Particle(int x, int y, Color color){
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
