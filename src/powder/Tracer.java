package powder;

import java.awt.Color;

/**
 * Tracer
 *
 * Extension of the Particle class that is affected by gravity
 *
 */

public class Tracer extends Particle {

  Tracer(int x, int y) {
    super(x, y, Color.RED);
    vel.y -= 1;
  }

}
