package powder;

import java.awt.Color;

/**
 * Solid
 */

public class Solid extends Particle {

  Solid(int x, int y) {
    super(x, y, Color.GRAY);
    dynamic = false;
  }

  @Override
  public void update() {

    if (!updated) {
      updated = true;
      updateTemp();

    }

  }

}
