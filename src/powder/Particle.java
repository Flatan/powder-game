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
	
	// We should hide these variables after Particle initialization, because if you think about it,
	// once a particle is created it can only ever move up,down.left or right from its relative position
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

	public double getgridY() {
		return gridY;
	}
	// This method should throw an error if the new coords dont make sense 
	// (like more than 1 pix away from relative position) but I'm too lazy
	// to do that right now
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
	
	public static void updateGrid() {
		for ( int x = 0; x < grid[0].length; x++ ) {
		for ( int y = 0; y < grid.length; y++ ) {
		  			if (grid[x][y] != null ) 
		  				grid[x][y].update();
		  	}}
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
	
	
	public Particle getParticleToLeft() {
		
		return grid[gridX -1][gridY];
	}
	public Particle getParticleToRight() {
		
		return grid[gridX +1][gridY];
	}
	public Particle getParticleAbove() {
		
		return grid[gridX][gridY - 1];
	}
	public Particle getParticleBelow() {
		
		return grid[gridX][gridY + 1];
	}

	public void update() {

	}
	
}
