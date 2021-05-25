package powder;

import java.awt.Color;

public class Particle {

	private static Particle[][] grid = new Particle[600][600];
	
	int gridX,gridY;
	double preciseX, preciseY;
	double gravity = 0.5;
	
	//velocity:
	double velX, velY = 0;
	
	public Color color = Color.white;

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
	
	private void moveToCell(int x, int y) {
		
		
		grid[gridX][gridY] = null;
		gridX = x;
		gridY = y;	
		
		grid[gridX][gridY] = this;

	}
	
	public void update() {
		if (!updated) {
			updated = true;
			
			velY += gravity;
			
			//preciseX += velX;
			//preciseY += velY;
			double targetX = preciseX+velX;
			double targetY = preciseY+velY;
			
			//normalized velocity vector
			double normVelX = velX/Math.hypot(velX,velY);
			double normVelY = velY/Math.hypot(velX,velY);
			
			while(preciseX<targetX || preciseY<targetY) {
				try {
					if (grid[(int) (preciseX+normVelX)][(int) (preciseY+normVelY)] != null)
						break;
				}
				catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
				preciseX += normVelX;
				preciseY += normVelY;
				if (preciseY >= targetY)
					preciseY = targetY;
				if (preciseX >= targetX)
					preciseX = targetX;
			}

			
			int newGridX = (int)preciseX;
			int newGridY = (int)preciseY;
			
			int width = grid[0].length;
			int height = grid.length;
			
			if (newGridX<0)
				newGridX = 0;
			else if (newGridX>=width)
				newGridX = width-1;
			if (newGridY<0)
				newGridY = 0;
			else if (newGridY>=width)
				newGridY = height-1;
			
			moveToCell(newGridX,newGridY);

			
		}
	}
	
}
