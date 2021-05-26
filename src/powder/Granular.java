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

	@Override
	public void update() {
		if (!updated) {
			updated = true;

			velY += gravity;

			double targetX = preciseX + velX;
			double targetY = preciseY + velY;

			// normalized velocity vector
			double normVelX = velX / Math.hypot(velX, velY);
			double normVelY = velY / Math.hypot(velX, velY);

			while (preciseX < targetX || preciseY < targetY) {
				try {
					if (grid[(int) (preciseX + normVelX)][(int) (preciseY + normVelY)] != null)
						break;
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
				preciseX += normVelX;
				preciseY += normVelY;
				if (preciseY >= targetY)
					preciseY = targetY;
				if (preciseX >= targetX)
					preciseX = targetX;
			}

			int newGridX = (int) preciseX;
			int newGridY = (int) preciseY;

			int width = grid[0].length;
			int height = grid.length;

			// if (newGridX<0)
			// newGridX = 0;
			// else if (newGridX>=width)
			// newGridX = width-1;
			// if (newGridY<0)
			// newGridY = 0;
			// else if (newGridY>=width)
			// newGridY = height-1;

			moveToCell(newGridX, newGridY);
		}
	}

}
