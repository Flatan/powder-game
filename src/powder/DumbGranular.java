package powder;

import java.awt.Color;

/**
 * DumbGranular
 *
 * This particle doesn't listen to gravity - It falls at a constant rate but
 * will be good for testing things
 *
 */

public class DumbGranular extends Particle {

	DumbGranular(int x, int y) {
		super(x, y);
	}

	DumbGranular(int x, int y, Color color) {
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

			if (newX + normVelX > 599 || newY + normVelY > 599)
				return;

			if (grid[(int) (newX + normVelX)][(int) (newY + normVelY)] != null) {
				return;
				// normVelX = 1.0;
			}


			// Trying out some things

			//         _
			//////////|x| --->
			////////// _
			//////////|_| null
			//////////|_| null

			// If this condition is met it goes right

			//			 _
			//		<---|x|////
			//			 _
			//      null|_|////
			//      null|_|//// 
			
			// If this condition is met it goes left
			

			boolean doubleStackedNullRight = (getRelativeParticle(1,-1) == null && getRelativeParticle(1, -2) == null);
			boolean doubleStackedNonNullCenter = (getRelativeParticle(0,-1) != null && getRelativeParticle(0, -2) != null);
			boolean doubleStackedNullLeft = (getRelativeParticle(-1,-1) == null && getRelativeParticle(-1, -2) == null);


			if (doubleStackedNullRight && doubleStackedNonNullCenter) {
				System.out.println("Huzah");
				System.out.println(normVelY);
				
				normVelX = 5.0;
			}

			if (doubleStackedNullLeft && doubleStackedNonNullCenter) {
				System.out.println("Huzah");
				System.out.println(normVelY);
				
				normVelX = -5.0;
			}

			newX += normVelX;
			newY += normVelY;

			if (newY >= targetY)
				newY = targetY;
			//if (newX >= targetX)
				//newX = targetX;

			setNewPosition(newX, newY);

		}

	}
}
