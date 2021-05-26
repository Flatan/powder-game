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
	
	int gridX,gridY;
	double preciseX, preciseY;
	double gravity = 0.5;
	
	//velocity:
	double velX, velY = 0;
	
	public Color color = Color.white;

	boolean moving = true;
	double downPush = 0.0;
	public boolean updated = false;
	
	
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
	
	protected void moveToCell(int x, int y) {
		
		grid[gridX][gridY] = null;
		gridX = x;
		gridY = y;	
		grid[gridX][gridY] = this;

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
