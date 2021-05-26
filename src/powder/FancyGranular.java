package powder;

import java.awt.Color;

/**
* FancyGranular
*
* Extension of the Particle class that is affected by gravity + weight of surrounding particles
*
*/

public class FancyGranular extends Particle {

	FancyGranular(int x, int y) {
		super(x, y);
	}

	FancyGranular(int x, int y, Color color) {
		super(x, y, color);
	}

	@Override
	public void update() {
		if (!updated) {
			updated = true;

			double totalWeightFromAbove = 0.0;
			Particle p = getParticleAbove();

			while (p != null) {
				totalWeightFromAbove += p.downPush;
				p = p.getParticleAbove();
			}


			if (totalWeightFromAbove > 20) {

				velX += 0.6;
				
				color = Color.RED;
				
			}

			velY += gravity;
			

			downPush = gravity;
			
			double targetX = preciseX + velX;
			double targetY = preciseY + velY;

			// normalized velocity vector
			double normVelX = velX / Math.hypot(velX, velY);
			double normVelY = velY / Math.hypot(velX, velY);
			

			//if (getParticleAbove() != null) {
				//System.out.println("Above");
				//velX += 0.2;
			//}

			
			//double normVelX = 1.0;
			//double normVelY = 2.0;

			//if (!moving) {
				//targetX = preciseX + 1;
	//            
		//}

			System.out.println(preciseX+normVelX);

			while (preciseX < targetX || preciseY < targetY) {

				if( preciseX + normVelX > 599 || preciseY + normVelY > 599 )
					break;

				// If target coord is already occupied

				if (getParticleBelow() != null)
					break;
				
				//if (grid[(int) (preciseX + normVelX)][(int) (preciseY + normVelY)] != null)
					//break;

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
