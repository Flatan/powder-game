package powder;

import java.awt.Color;

import math.Vector2D;

/**
 * Granular
 *
 * Extension of the Particle class that is affected by gravity
 *
 */

public class Granular extends Particle {

	Granular(int x, int y) {
		super(x, y, Color.white);
		vel.y = -1;
		giveVel = new Vector2D(0.5, 0.5);
	}

	// Good ole particle movin method
	@Override
	public void update() {

		if (!updated) {
			updated = true;

		}
	}
}
