package powder;

import java.awt.Color;
import java.util.Random;
import java.util.function.BiFunction;

import math.SurfacePoint;
import math.Vector2D;

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
			
			//dispSlope();
			
			
			vel.add(gravity);
			updateTemp();
			
			double[] nextPos = getNextPos();
			setNewPosition(nextPos[0], nextPos[1]);
			
			//Vector2D netForce = new Vector2D();
			
			if (!testRel(0,1) && supported()) {

			double m = slope();
			SurfacePoint surf = new SurfacePoint(m,SurfacePoint.Side.BELOW);
			Vector2D normalForce = surf.getNormal();
			normalForce.multiply(Math.abs(gravity.dotProduct(surf.getNormal())));
			//System.out.println(m);
			//System.out.println(normalForce);
			vel.add(normalForce);
			}
			
			Random rnd = new Random();
			if (!testRel(-1,0) && !testRel(1,0) && supported()) {
				if (rnd.nextBoolean())
					setNewPosition(X()+1, Y());
				else
					setNewPosition(X()-1, Y());
			}

			/*if (!testRel(-1, 0) && !testRel(-1, -1) && (testRel(1, 0) || testRel(1, -1)) && supported()) {
				vel = new Vector2D(-1,0);
				
			}

			else if (!testRel(1, 0) && !testRel(1, -1) && (testRel(-1, 0) || testRel(-1, -1)) && supported()) {
				vel = new Vector2D(1,0);

			}*/

		}
	}

	// Calculates the particle's next position
	public double[] getNextPos() {

		double targetX = realX() + vel.x;
		double targetY = realY() + vel.y;
		double newX = realX();
		double newY = realY();


		// normalized velocity vector
		Vector2D normVel = vel.normalizedVect();

		double targetDistance = distanceFrom(targetX, targetY);

		while (distanceFrom(newX, newY) < targetDistance) {

			if (grid.outOfBoundsX(newX + normVel.x)) {
				vel.x = 0;
				break;
			}
			if (grid.outOfBoundsY(newY + normVel.y)) {
				vel.y = 0;
				break;
			}
			// Check if path is blocked by particle

			
			if (grid.test((int) (newX + normVel.x), (int) (newY + normVel.y))) {
				Particle obstacle = grid.get(newX + normVel.x, newY + normVel.y);
				if (!obstacle.updated && obstacle != this) {
					obstacle.update();
				}
			}

			if (grid.test((int) (newX + normVel.x), (int) (newY + normVel.y))) {
				Particle obstacle = grid.get(newX + normVel.x, newY + normVel.y);

				if (obstacle != this) {
					collide(obstacle, 1);
					obstacle.update();
					break;
				}
			}

			// Particle obstacle = grid.get(newX + normVelX, newY + normVelY);
			// if (obstacle != null && !obstacle.updated && obstacle != this) {
			// obstacle.update();
			// }
			// obstacle = grid.getClosest(newX + normVelX, newY + normVelY);
			// if (obstacle != null && obstacle != this) {
			// collide(obstacle, 1);
			// break;
			// }

			newX += normVel.x;
			newY += normVel.y;

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

		double tempVelX = vel.x;
		double tempVelY = vel.y;

		vel.x = newVel.apply(vel.x, other.vel.x);
		vel.y = newVel.apply(vel.y, other.vel.y);

		other.vel.x = newVel.apply(other.vel.x, tempVelX);
		other.vel.y = newVel.apply(other.vel.y, tempVelY);

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
