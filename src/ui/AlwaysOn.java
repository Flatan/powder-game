
package ui;

import java.awt.Color;

import core.*;
import math.Vector2D;
import powder.*;

import java.awt.Graphics2D;
import ui.UI.TextBuffer;

/**
 * AlwaysOn
 */
public class AlwaysOn implements UIEvent {

  @Override
  public void draw(TextBuffer t, Graphics2D g) {

    Board B = Application.getBoard();

    t.add("q - quit");
    t.add("p - powder");
    t.add("s - solid");
    t.flush(0, 0);

    if (B.getFPS() < 40)
      g.setColor(Color.red);
    else
      g.setColor(Color.white);

    t.addPair("FPS: ", B.getFPS());
    t.flush(B.getWidth() - 200, 0);

    g.setColor(Color.white);

    t.addPair("Spawn count: ", Board.runtimeParticleCount);
    t.flush();

    int cursorSize = UI.mouse.getCursorSize();
    g.drawOval(UI.mouse.windowX() - cursorSize / 2, UI.mouse.windowY() - cursorSize / 2, cursorSize, cursorSize);

  }

  public void testCollison() {
    ParticleGrid grid = Particle.getGrid();
    Particle.gravity = new Vector2D();
    Granular p1 = (Granular) grid.spawn(0, 5, Color.WHITE, Granular.class);
    p1.vel = new Vector2D(0.1, 0);
    Granular p2 = (Granular) grid.spawn(9, 5, Color.WHITE, Granular.class);
    p2.vel = new Vector2D(-0.1, 0);
  }

  @Override
  public void on(boolean once) {

    Board B = Application.getBoard();

    switch (UI.keyboard.keyPressed()) {
      case 'p':
        B.setSelectedElement(Granular.class);
        B.setSelectedColor(Color.white);
        break;
      case 's':
        B.setSelectedElement(Solid.class);
        B.setSelectedColor(Color.gray);
        break;
      case 'q':
        System.exit(0);
      case ' ':
        testCollison();
        break;
    }
  }

  @Override
  public void off(boolean once) {
  }

  @Override
  public boolean trigger() {
    return true;
  }

}
