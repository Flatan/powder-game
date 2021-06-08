
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
    super(x, y, Color.GRAY);
    vel.x = 0;
    vel.y = 0;
  }

  @Override
  public void update() {

    if (!updated) {
      updated = true;

    }
  }
}
