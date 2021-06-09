package powder;

import java.awt.Color;

import math.Vector2D;

/**
 * Solid
 */

public class Solid extends Particle {

  Solid(int x, int y) {
    super(x, y, Color.GRAY);
    dynamic = false;
    giveVel = new Vector2D(0, 0);
  }

  @Override
  public void update() {

    if (!updated) {
      updated = true;

    }

  }

}
