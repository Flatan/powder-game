package ui;

import powder.*;

/**
 * PaintParticleCluster
 */
public class PaintParticleCluster extends UIEvent {

  @Override
  public void eventOff() {

  }

  @Override
  public void eventOn() {

    Board B = Application.getBoard();
    Mouse M = B.getMouse();

    int diameter = (int) (M.getCursorSize()/B.getScale());
    int my = Mouse.Y();
    int mx = Mouse.X();
    System.out.println(my);

    ParticleGrid grid = Particle.getGrid();

    my = (int) (B.getHeight()/B.getScale() - 1 - my);
    
    
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
