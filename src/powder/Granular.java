package powder;

import java.awt.Color;

import static powder.ParticleOR.*;
import static powder.ParticleAND.*;

/**
* Granular
*
* Extension of the Particle class that is affected by gravity
*
*/

public class Granular extends Particle {

	Granular(int x, int y) {
		super(x, y);
	}

	Granular(int x, int y, Color color) {
		super(x, y, color);
	}


	// Good ole particle movin method 
	@Override
	public void update() {
		/*if (Math.abs(velY)==0)
			color = Color.red;
		else
			color = Color.white;
		*/
		

		ParticleAND[][] A = {
			{AFALSE, null,  null},
			{AFALSE, ATHIS, null},
			{null,   ATRUE, null},
		};

		ParticleOR[][] B = {
			{null,  null,  OTRUE},
			{null, OTHIS,  OTRUE},
			{null,  null,   null},
		};
	

		
		if (!updated) {
			updated = true;
			velY += gravity;
			
			
			
			double[] nextPos = getNextPos();
			setNewPosition(nextPos[0], nextPos[1]);
			
			if		(relParticleExists(0, -1)&&
					!relParticleExists(-1, 0)&&
					!relParticleExists(-1, 1)&&
					(relParticleExists(1, 0)||
					 relParticleExists(1, 1))
					&& velY ==0) {
						velX = -1;
					 }

			else if (relParticleExists(0, -1)&&
					!relParticleExists(1, 0)&&
					!relParticleExists(1, 1)&&
					(relParticleExists(-1, 0)||
					 relParticleExists(-1, 1))
					&& velY ==0) {
						velX = 1;
					}
			else
				velX = 0;
			
		}
	}
	
	//Calculates the particle's next position
	public double[] getNextPos() {
		double currentX = getX();
		double currentY = getY();
		double targetX = currentX + velX;
		double targetY = currentY + velY;
		double newX = currentX;
		double newY = currentY;

		// normalized velocity vector
		double normVelX = velX / Math.hypot(velX, velY);
		double normVelY = velY / Math.hypot(velX, velY);
		
		double targetDistance = Math.hypot(targetX-currentX, targetY-currentY);

		while (Math.hypot(newX-currentX,newY-currentY) < targetDistance) {
			
			if( newX + normVelX >= grid[0].length-1 || newX + normVelX < 0) {
				velX=0;
				
				break;
			}
			if (newY + normVelY >= grid.length-1 || newY + normVelY < 0) {
				velY=0;
				
				break;
			}
			// Check if path is blocked by particle
			Particle obstacle = grid[(int) (newX + normVelX)][(int) (newY + normVelY)];
			
			//If so, updates blocking particle and checks again
			if (obstacle != null && !obstacle.updated && obstacle != this)
				obstacle.update();
			obstacle = grid[(int) (newX + normVelX)][(int) (newY + normVelY)];
			if (obstacle != null && obstacle != this) {
				velX = obstacle.velX;
				velY = obstacle.velY;
				break;
			}
			
			newX += normVelX;
			newY += normVelY;

			if (Math.hypot(newX-currentX,newY-currentY) >= targetDistance) {
				newY = targetY;
				newX = targetX;
			}
		}
		double[] nextPos = {newX,newY};
		return nextPos;
	}
}
