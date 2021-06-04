package ui;

import powder.*;
import core.*;

/**
 * PaintParticleCluster
 */
public class PaintParticleCluster implements UIEvent {

  @Override
  public void eventOff(boolean justEnded) {

  }

  @Override
  public void eventOn(boolean justStarted) {

    Board B = Application.getBoard();
    Mouse M = B.getMouse();

    int diameter = (int) (M.getCursorSize() / B.getScale());
    int my = Mouse.Y();
    int mx = Mouse.X();

    ParticleGrid grid = Particle.getGrid();

    my = (int) (B.getHeight() / B.getScale() - 1 - my);

    for (int x = mx - diameter / 2; x < mx + diameter / 2; x++) {
      for (int y = my - diameter / 2; y < my + diameter / 2; y++) {

        if (!grid.outOfBounds(x, y))
          if (grid.get(x, y) == null)
            if (Math.hypot(x - mx, y - my) <= diameter / 2) {
              Particle p = grid.spawnParticle(x, y, B.getSelectedColor(), B.getSelectedElement());
              p.temperature = B.getSelectedTemp();
            }
      }
    }
  }

  @Override
  public boolean sendingSignal() {
    Board B = Application.getBoard();
    Mouse M = B.getMouse();

    return M.isDown;
  }

}
