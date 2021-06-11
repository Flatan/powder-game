package powder;

import java.awt.Color;

public class Air extends Particle {
  double pressure;
  double density;

  Air(int x, int y) {
    super(x, y, Color.BLACK);
  }

}
