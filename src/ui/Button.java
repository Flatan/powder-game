package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ui.UI.Printer;
import java.awt.geom.Area;

/**
 * Button
 */
public class Button implements UIEvent {

  boolean triggered = false;
  Rectangle r = new Rectangle(600, 300, 50, 50);

  @Override
  public void draw(Printer p, Graphics2D g) {

    g.setColor(Color.white);
    g.drawRect(r.x, r.y, r.width, r.height);

    if (triggered) {
      g.setColor(Color.red);
      g.drawRect(r.x, r.y, r.width, r.height);
    }
  }

  @Override
  public void on(boolean once) {
    if (once) {
      triggered = true;
      UI.mouse.setShape(new Area(), false);

      // UI.mouse.setCursorFunc((Graphics2D g2) -> {
      // });
    }

  }

  @Override
  public void off(boolean once) {
    if (once) {
      triggered = false;
      UI.mouse.revertShape();
    }

  }

  @Override
  public boolean trigger() {

    return (UI.mouse.cursorInBounds(r));

  }
}
