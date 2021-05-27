package powder;

import java.awt.Color;


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
		
		
		if (!updated) {
			updated = true;
			velY += gravity;

			double[] nextPos = getNextPos();
			setNewPosition(nextPos[0], nextPos[1]);
			
			if		(relParticleExists(0, -1)&&
					!relParticleExists(-1, 0)&&
					!relParticleExists(-1, 1)&&
					(relParticleExists(1, 0)||
					 relParticleExists(1, 1))) {
						setNewPosition(getX()-1, getY());
					 }

			else if (relParticleExists(0, -1)&&
					!relParticleExists(1, 0)&&
					!relParticleExists(1, 1)&&
					(relParticleExists(-1, 0)||
					 relParticleExists(-1, 1))) {
						setNewPosition(getX()+1, getY());
					}
			else
				color = Color.white;
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

		while (newX < targetX || newY < targetY) {

			if( newX + normVelX >= 599 ) {
				velX=0;
				break;
			}
			if (newY + normVelY >= 599) {
				velY=0;
				break;
			}

			// If target coord is already occupied
			Particle obstacle = grid[(int) (newX + normVelX)][(int) (newY + normVelY)];
			if (obstacle != null ) {
				velX = obstacle.velX;
				velY = obstacle.velY;
				break;
			}

			newX += normVelX;
			newY += normVelY;

			if (newY >= targetY)
				newY = targetY;
			if (newX >= targetX)
				newX = targetX;
		}
		double[] nextPos = {newX,newY};
		return nextPos;
	}
}
