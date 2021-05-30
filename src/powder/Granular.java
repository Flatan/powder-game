package powder;

import java.awt.Color;
import java.util.function.BiFunction;


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
		


		if (!updated) {
			updated = true;
			velY += gravity;

			
			updateTemp();
			double[] nextPos = getNextPos();
			setNewPosition(nextPos[0], nextPos[1]);
			
			
			if(!relParticleExists(-1,0)&&
			   !relParticleExists(-1,-1)&&
			   (relParticleExists(1,0)||
				relParticleExists(1,-1))
			   &&supported()
			  ) {
						velX = -1;
					 }

			else if(!relParticleExists(1,0)&&
					   !relParticleExists(1,-1)&&
					   (relParticleExists(-1,0)||
						relParticleExists(-1,-1))
					   &&supported()) {
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
			
			if( newX + normVelX >= grid.W || newX + normVelX < 0) {
				velX=0;
				
				break;
			}
			if (newY + normVelY >= grid.H || newY + normVelY < 0) {
				velY=0;
				
				break;
			}
			// Check if path is blocked by particle
			Particle obstacle = grid.get((int) (newX + normVelX),(int) (newY + normVelY));
			if (obstacle != null && !obstacle.updated && obstacle != this) {

				obstacle.update();
			}
			obstacle = grid.get ((int) (newX + normVelX), (int) (newY + normVelY));
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
	
	@Override
	public boolean supported() {
		if (getGridY() <= 0)
			return true;
		else if (!relParticleExists(0,-1))
			return false;
		else
			return getRelativeParticle(0,-1).supported();
	}
	

}
