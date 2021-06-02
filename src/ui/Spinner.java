package ui;

import powder.*;
import core.*;

/**
 * Spinner
 *
 * For running super quick collision tests with many particles
 */
public class Spinner implements UIEvent {
  int iteration = 0;

  @Override
  public void eventOff(boolean justEnded) {
  }

  @Override
  public void eventOn(boolean justStarted) {

    Board B = Application.getBoard();
    KeyAction K = B.getKeyboard();

    ParticleGrid grid = Particle.getGrid();

    iteration++;

    int size = 5;

    int factor = 150;

    double pos = Math.sin(iteration * 5) * factor;

    double pos2 = Math.cos(iteration * 5) * factor;

    int posx = (int) pos;
    int posy = (int) pos2;

    // draw square
    for (int x = B.getWidth() / 2 - 10 + posx; x < B.getWidth() / 2 + 10 + posx; x++) {

      for (int y = B.getHeight() / 2 - 10 + posy; y < B.getHeight() / 2 + 10 + posy; y++) {

        Particle p = grid.spawnParticle(x, y, B.getSelectedColor(), B.getSelectedElement());
        p.temperature = B.getSelectedTemp();
      }
    }

  }

  @Override
  public boolean sendingSignal() {

    KeyAction K = Application.getBoard().getKeyboard();

    return K.keyToggled('r');

  }

}
