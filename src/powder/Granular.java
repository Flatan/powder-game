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
		vel.y += -1;
	}

	@Override
	public void update() {

		if (!updated) {
			updated = true;

		}
	}
}
