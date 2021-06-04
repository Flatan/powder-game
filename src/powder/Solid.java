package powder;

import java.awt.Color;

/**
 * Solid
 */

public class Solid extends Particle {

  Solid(int x, int y, Color color) {
    super(x, y, color);
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
