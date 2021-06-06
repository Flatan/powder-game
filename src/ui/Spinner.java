package ui;

import powder.*;
import ui.UI.TextBuffer;
import java.awt.Graphics2D;
import core.*;

/**
 * Spinner
 *
 * For running super quick collision tests with many particles
 */
public class Spinner implements UIEvent {
  int iteration = 0;

  @Override
  public void draw(TextBuffer t, Graphics2D g) {
  }

  @Override
  public void off(boolean once) {
  }

  @Override
  public void on(boolean once) {

    Board B = Application.getBoard();

    ParticleGrid grid = Particle.getGrid();

    iteration++;

    int factor = 150;

    double pos = Math.sin(iteration * 5) * factor;

    double pos2 = Math.cos(iteration * 5) * factor;

    int posx = (int) pos;
    int posy = (int) pos2;

    // draw square
    for (int x = B.getWidth() / 2 - 10 + posx; x < B.getWidth() / 2 + 10 + posx; x++) {

      for (int y = B.getHeight() / 2 - 10 + posy; y < B.getHeight() / 2 + 10 + posy; y++) {

        Particle p = grid.spawn(x, y, B.getSelectedColor(), B.getSelectedElement());
        p.temperature = B.getSelectedTemp();
      }
    }

  }

  @Override
  public boolean trigger() {

    return UI.keyboard.keyToggled('s');

  }

}
