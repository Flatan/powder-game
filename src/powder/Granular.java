package powder;

import java.awt.Color;
import java.util.Arrays;
import java.util.function.BiFunction;

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
		

		ParticleAND[][] Aa = {
			{AFALSE, null,  null},
			{AFALSE, null,  null},
			{null,   ATRUE, null},
		};

		ParticleOR[][] Ao = {
			{null,  null,  OTRUE},
			{null,	null,  OTRUE},
			{null,  null,   null},
		};

		ParticleAND[][] Ba = {
			{null, null,  AFALSE},
			{null, null,  AFALSE},
			{null, ATRUE,   null},
		};
		
		if (!updated) {
			updated = true;
			velY += gravity;
			

			
			double[] nextPos = getNextPos();
			setNewPosition(nextPos[0], nextPos[1]);
			
			//if (
			//if		(relParticleExists(0, -1)&&
					//!relParticleExists(-1, 0)&&
					//!relParticleExists(-1, 1)&&
				
			/*if (slope()>1) {
				color = Color.red;
			}
			else if (slope()<-1) {
				color = Color.green;
			}
			else
				color = Color.white;
			*/
			
			if(	relAND(Aa) &&
					(relParticleExists(1, 0)||
					 relParticleExists(1, 1))
					&& supported()) {
						velX = -1;
					 }

			//else if (relParticleExists(0, -1)&&
					//!relParticleExists(1, 0)&&
					//!relParticleExists(1, 1)&&

			else if ( relAND(Ba) &&
					(relParticleExists(-1, 0)||
					 relParticleExists(-1, 1))
					&& supported()) {
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
			
			if( newX + normVelX >= grid[0].length || newX + normVelX < 0) {
				velX=0;
				
				break;
			}
			if (newY + normVelY >= grid.length || newY + normVelY < 0) {
				velY=0;
				
				break;
			}
			// Check if path is blocked by particle
			Particle obstacle = grid[(int) (newX + normVelX)][(int) (newY + normVelY)];
			
			//If so, updates blocking particle and checks again
			if (obstacle != null && !obstacle.updated && obstacle != this) {
				
				obstacle.update();
			}
			obstacle = grid[(int) (newX + normVelX)][(int) (newY + normVelY)];
			if (obstacle != null && obstacle != this) {
				collide(obstacle, 1);
				//velX = obstacle.velX;
				//velY = obstacle.velY;
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
	
	/**
	 * Calculates collision between two particles
	 * @param other The other particle
	 * @param cR Coefficient of Restitution. Should be between 0-1. Or SLIGHTLY higher for fun bouncy effect
	 */
	public void collide(Particle other, double cR) {
		BiFunction<Double,Double,Double> newVel = (a,b) -> (cR*(b-a)+a+b)/2;
		
		velX = newVel.apply(velX, other.velX);
		velY = newVel.apply(velY, other.velY);
		
		other.velX = newVel.apply(other.velX, velX);
		other.velY = newVel.apply(other.velY, velY);
			
	}
	

}
