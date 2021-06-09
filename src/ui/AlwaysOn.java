
package ui;

import java.awt.Color;

import core.*;
import math.Vector2D;
import powder.*;

import java.awt.Graphics2D;

import ui.ParticleFactory.Shape;
import ui.UI.TextBuffer;

import java.util.function.*;
import java.awt.geom.Ellipse2D;

/**
 * AlwaysOn
 */
public class AlwaysOn implements UIEvent {
  Particle tracer;
  Graphics2D aog;

  @Override
  public void draw(TextBuffer t, Graphics2D g) {

    this.aog = g;
    Board B = Application.board;

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

    if (tracer != null) {
      t.add(String.format("Tracer: (%d, %d)", tracer.x, tracer.y)).flush();
      t.add(String.format("Tracer vel: %.2f | %.2f", tracer.vel.x, tracer.vel.y)).flush();
      t.add(String.format("Tracer n: (%d, %d)", tracer.nx, tracer.ny)).flush();

    }

    Mouse m = UI.mouse;

    m.draw(g);

  }

  public void testCollison() {
    ParticleGrid grid = Application.grid;
    Particle.gravity = new Vector2D();
    Granular p1 = (Granular) grid.spawn(0, 5, Granular.class);
    p1.vel = new Vector2D(0.1, 0);
    Granular p2 = (Granular) grid.spawn(9, 5, Granular.class);
    p2.vel = new Vector2D(-0.1, 0);
  }

  @Override
  public void on(boolean once) {
    if (once) {

      UI.mouse.setShape(new Ellipse2D.Float(0, 0, 1, 1), true);
      Application.grid.reset(600, 600);
      this.tracer = Application.grid.spawn(300, 300, Tracer.class);

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
        break;
      case 's':
        ParticleFactory.element = Solid.class;
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
