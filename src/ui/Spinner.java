package ui;

import powder.*;
import core.*;

/**
 * Spinner
 *
 * For running super quick collision tests with many particles
 */
public class Spinner implements Command {
  int iteration = 1;

  @Override
  public void call(String... args) {

    Board B = Application.board;

    ParticleGrid grid = Application.grid;

    iteration++;

    int factor = 150;

    double pos = Math.sin(iteration * 5) * factor;

    double pos2 = Math.cos(iteration * 5) * factor;

    int posx = (int) pos;
    int posy = (int) pos2;

    // draw square
    for (int x = B.getWidth() / 2 - 10 + posx; x < B.getWidth() / 2 + 10 + posx; x++) {

      for (int y = B.getHeight() / 2 - 10 + posy; y < B.getHeight() / 2 + 10 + posy; y++) {

        Particle p = grid.spawn(x, y, ParticleFactory.element);
        p.temperature = 50;
      }
    }
  }

}
