package ui;

import powder.Board;
import powder.Application;
import powder.*;
import powder.ParticleGrid;

public class UIEvent {

  private boolean active = false;
  private Mouse M;
  private Board B;

  public UIEvent() {

  }

  public boolean isActive() {
    return active;
  }

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  static public void toggleHeatMap() {

    if (Particle.showHeatMap) {
      Particle.showHeatMap = false;
    } else {
      Particle.showHeatMap = true;
    }
  }

  /**
   * Draw a cluster of particles on the screen given (x, y) coords and a diameter
   * 
   * @param mx
   * @param my
   * @param diameter
   */
  public void paintParticleCluster() {

    if (!active) {
      return;
    }

    B = Application.getBoard();
    M = B.getMouse();

    int diameter = M.getCursorSize();
    int my = Mouse.Y();
    int mx = Mouse.X();

    ParticleGrid grid = Particle.getGrid();

    my = B.getHeight() - 1 - my;

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
}
