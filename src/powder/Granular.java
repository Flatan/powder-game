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
		super(x, y, Color.white);
	}

	// Good ole particle movin method
	@Override
	public void update() {

		if (!updated) {
			updated = true;

		}
	}
}
