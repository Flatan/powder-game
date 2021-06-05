package ui;

import powder.*;
import ui.UI.TextBuffer;
import java.awt.Graphics2D;
import core.*;

/**
 * PaintParticleCluster
 */
public class PaintParticleCluster implements UIEvent {
  final boolean onlyOne = false;

  @Override
  public void draw(TextBuffer t, Graphics2D g) {
  }

  @Override
  public void off(boolean once) {

  }

  @Override
  public void on(boolean once) {

    Board B = Application.getBoard();

    int diameter = (int) (UI.mouse.getCursorSize() / B.getScale());
    int my;
    int mx = UI.mouse.X();

    ParticleGrid grid = Particle.getGrid();

    my = (int) (B.getHeight() / B.getScale() - 1 - UI.mouse.Y());

    if (onlyOne) {
      Particle p = grid.spawn(mx, my, B.getSelectedColor(), B.getSelectedElement());
      p.temperature = B.getSelectedTemp();
    } else

      for (int x = mx - diameter / 2; x < mx + diameter / 2; x++) {
        for (int y = my - diameter / 2; y < my + diameter / 2; y++) {

          if (Math.hypot(x - mx, y - my) <= diameter / 2) {
            Particle p = grid.spawn(x, y, B.getSelectedColor(), B.getSelectedElement());
            p.temperature = B.getSelectedTemp();
          }
        }
      }
  }

  @Override
  public boolean trigger() {

    return UI.mouse.isDown;
  }

}
