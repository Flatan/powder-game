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
		if (!updated) {
			updated = true;

			velY += gravity;

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

				if( newX + normVelX > 599 || newY + normVelY > 599 )
					break;

				// If target coord is already occupied
				if (grid[(int) (newX + normVelX)][(int) (newY + normVelY)] != null)
					break;

				newX += normVelX;
				newY += normVelY;

				if (newY >= targetY)
					newY = targetY;
				if (newX >= targetX)
					newX = targetX;
			}

			setNewPosition(newX, newY);
		}
	}
}
