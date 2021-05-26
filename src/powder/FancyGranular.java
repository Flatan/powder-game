package powder;

import java.awt.Color;


/**
* FancyGranular
*
* This particle isn't working yet 
*
*/

public class FancyGranular extends Particle {

	FancyGranular(int x, int y) {
		super(x, y);
	}

	FancyGranular(int x, int y, Color color) {
		super(x, y, color);
	}


	// Good ole particle movin method 
	//
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
					return;


				System.out.println(getgridY());
				if (getgridY() == 599.0) {
					
					System.out.println("grounded");
				}
				// If target coord is already occupied
				if (grid[(int) (newX + normVelX)][(int) (newY + normVelY)] != null) {
					newX += Math.random();
					newY += Math.random();
					break;
						//normVelX = 1.0;

				}

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
