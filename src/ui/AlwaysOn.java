
package ui;

import java.awt.Color;

import core.*;
import math.Vector2D;
import powder.*;

import java.awt.Graphics2D;

import ui.ParticleFactory.Shape;
import ui.UI.TextBuffer;

/**
 * AlwaysOn
 */
public class AlwaysOn implements UIEvent {

  @Override
  public void draw(TextBuffer t, Graphics2D g) {

    Board B = Application.board;
    int cursorSize = UI.mouse.getCursorSize();

    t.add("q - quit");
    t.add("p - powder");
    t.add("s - solid");
    t.add("a - cycle spawn shapes");
    t.flush(0, 0);

    if (B.getFPS() < 40)
      g.setColor(Color.red);

    t.addPair("FPS: ", B.getFPS()).flush(B.getWidth() - 200, 0);

    g.setColor(Color.white);

    t.addPair("Spawn count: ", Board.runtimeParticleCount).flush();

    g.drawOval(UI.mouse.windowX() - cursorSize / 2, UI.mouse.windowY() - cursorSize / 2, cursorSize, cursorSize);

  }

  public void testCollison() {
    ParticleGrid grid = Application.grid;
    Particle.gravity = new Vector2D();
    Granular p1 = (Granular) grid.spawn(0, 5, Color.WHITE, Granular.class);
    p1.vel = new Vector2D(0.1, 0);
    Granular p2 = (Granular) grid.spawn(9, 5, Color.WHITE, Granular.class);
    p2.vel = new Vector2D(-0.1, 0);
  }

  @Override
  public void on(boolean once) {

    if (once) {
      ParticleFactory.element = Border.class;
      ParticleFactory.color = Color.gray;
      ParticleFactory.spawnRect(0, 0, 600, 600, 3);
      ParticleFactory.element = Granular.class;
      ParticleFactory.color = Color.white;
    }

    if (UI.keyboard.keyRepeated('a') % 2 == 1) {
      ParticleFactory.shape = Shape.ONE;
      UI.mouse.setCursorSize(3);
    } else if (UI.keyboard.keyPressed() == 'a') {
      UI.mouse.setCursorSize(25);
      ParticleFactory.shape = Shape.CIRCLE;
    }

    switch (UI.keyboard.keyPressed()) {
      case 'p':
        ParticleFactory.element = Granular.class;
        ParticleFactory.color = Color.white;
        break;
      case 's':
        ParticleFactory.element = Solid.class;
        ParticleFactory.color = Color.gray;
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
