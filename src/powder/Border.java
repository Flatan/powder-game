package powder;

import java.awt.Color;

/**
 * Border
 *
 * Extension of the Particle class that is affected by gravity
 *
 */

public class Border extends Particle {

	Border(int x, int y) {
		super(x, y);
	}

	Border(int x, int y, Color color) {
		super(x, y, color);
	}

	@Override
	public void update() {

		if (!updated) {
			updated = true;

		}
	}
}
