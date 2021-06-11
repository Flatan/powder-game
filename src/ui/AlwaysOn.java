
package ui;

import java.awt.Color;

import core.*;
import math.Vector2D;
import powder.*;

import java.awt.Graphics2D;

import ui.ParticleFactory.Shape;
import ui.UI.Printer;

import java.awt.geom.Ellipse2D;

/**
 * AlwaysOn
 */
public class AlwaysOn implements UIEvent {
  Particle tracer;

  @Override
  public void draw(Printer p, Graphics2D g) {

    Board B = Application.board;

    p.setLocation(0, 0);
    p.println("q - quit");
    p.println("p - powder");
    p.println("s - solid");
    p.println("a - cycle spawn shapes");

    p.setLocation(Application.board.getWidth() - 200, 0);

    if (B.getFPS() < 40) {
      p.println(String.format("FPS: %.2f", B.getFPS()), Color.RED);
    } else {
      p.println(String.format("FPS: %.2f", B.getFPS()), Color.WHITE);
    }

    p.println(String.format("Spawn count: %d", Board.runtimeParticleCount));

    if (tracer != null) {
      p.println(String.format("Tracer: (%d, %d)", tracer.x, tracer.y));
      p.println(String.format("Tracer vel: %.2f | %.2f", tracer.vel.x, tracer.vel.y));
    }

    UI.mouse.draw(g);

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
      Logger.log("test!");

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
