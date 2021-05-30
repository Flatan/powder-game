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

			if (!testRel(-1, 0) && !testRel(-1, -1) && (testRel(1, 0) || testRel(1, -1)) && supported()) {
				velX = -1;
			}

			else if (!testRel(1, 0) && !testRel(1, -1) && (testRel(-1, 0) || testRel(-1, -1)) && supported()) {
				velX = 1;
			} else
				velX = 0;

		}
	}

	// Calculates the particle's next position
	public double[] getNextPos() {
		double targetX = realX() + velX;
		double targetY = realY() + velY;
		double newX = realX();
		double newY = realY();

		// normalized velocity vector
		double normVelX = velX / Math.hypot(velX, velY);
		double normVelY = velY / Math.hypot(velX, velY);

		double targetDistance = distanceFrom(targetX, targetY);

		while (distanceFrom(newX, newY) < targetDistance) {

			if (grid.outOfBoundsX(newX + normVelX)) {
				velX = 0;
				break;
			}
			if (grid.outOfBoundsY(newY + normVelY)) {
				velY = 0;
				break;
			}
			// Check if path is blocked by particle

			if (grid.testAbs((int) (newX + normVelX), (int) (newY + normVelY))) {
				Particle obstacle = grid.getReal(newX + normVelX, newY + normVelY);
				if (!obstacle.updated && obstacle != this) {
					obstacle.update();
				}
			}
			if (grid.testAbs((int) (newX + normVelX), (int) (newY + normVelY))) {
				Particle obstacle = grid.getReal(newX + normVelX, newY + normVelY);
				if (obstacle != this) {
					collide(obstacle, 1);
					break;
				}
			}

			// Particle obstacle = grid.getReal(newX + normVelX, newY + normVelY);
			// if (obstacle != null && !obstacle.updated && obstacle != this) {
			// obstacle.update();
			// }
			// obstacle = grid.getClosest(newX + normVelX, newY + normVelY);
			// if (obstacle != null && obstacle != this) {
			// collide(obstacle, 1);
			// break;
			// }

			newX += normVelX;
			newY += normVelY;

			if (distanceFrom(newX, newY) >= targetDistance) {
				newY = targetY;
				newX = targetX;
			}
		}
		double[] nextPos = { newX, newY };
		return nextPos;
	}

	/**
	 * Calculates collision between two particles
	 * 
	 * @param other The other particle
	 * @param cR    Coefficient of Restitution. Should be between 0-1. Or SLIGHTLY
	 *              higher for fun bouncy effect
	 */
	public void collide(Particle other, double cR) {
		BiFunction<Double, Double, Double> newVel = (a, b) -> (cR * (b - a) + a + b) / 2;

		velX = newVel.apply(velX, other.velX);
		velY = newVel.apply(velY, other.velY);

		other.velX = newVel.apply(other.velX, velX);
		other.velY = newVel.apply(other.velY, velY);

	}

	@Override
	public boolean supported() {
		if (Y() <= 0)
			return true;
		else if (!testRel(0, -1))
			return false;
		else
			return getRel(0, -1).supported();
	}

}
