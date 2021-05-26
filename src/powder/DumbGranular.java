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
				newX += Math.random();
				newY += Math.random();
				return;
				// normVelX = 1.0;

			}

			newX += normVelX;
			newY += normVelY;

			if (newY >= targetY)
				newY = targetY;
			if (newX >= targetX)
				newX = targetX;

			setNewPosition(newX, newY);

		}

	}
}
