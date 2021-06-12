package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import core.UI.Printer;
import core.UI.UIEvent;
import core.UI;
import powder.*;
import java.awt.Point;
//import core.Mouse;
import color.ColorGradientMap;
import java.awt.geom.Area;

/**
 * Button
 */
public class Button implements UIEvent {

  final int X = 610;
  final int Y = 300;
  final int W = 70;
  final int H = 20;
  final int XPAD = 2;
  final String LABEL = "Powder";

  boolean hover = false;
  boolean set = true;

  Color hoverFg = new Color(150, 150, 150);
  Color innerGradient = new Color(30, 30, 30);
  Color outerGradient = Color.BLACK;
  Rectangle r = new Rectangle(X, Y, W, H);
  BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_BGR);
  ColorGradientMap c = new ColorGradientMap().addColor(1.0, outerGradient).addColor(0.0, innerGradient);

  @Override
  public void draw(Printer p, Graphics2D g) {
    g.setColor(new Color(80, 80, 80));

    if (hover) {

      double mx = UI.mouse.X() - X;
      double my = UI.mouse.Y() - Y;

      img = c.applyRadialGradient(img, (int) mx, (int) my, r);

      g.drawImage(img, X, Y, null);
      g.setColor(hoverFg);

      g.drawString(LABEL, X + XPAD, Y + g.getFont().getSize() / 2 + H / 2);
    }

    if (set) {
      g.setColor(Color.WHITE);
      g.drawString(LABEL, X + XPAD, Y + g.getFont().getSize() / 2 + H / 2);
    } else {
      g.drawString(LABEL, X + XPAD, Y + g.getFont().getSize() / 2 + H / 2);
    }

    g.drawRect(X, Y, W, H);

  }

  @Override
  public void on(boolean once) {
    if (once) {
      hover = true;
      UI.mouse.setShape(new Area(), false);
    }

    if (!set && UI.mouse.clicked()) {
      set = true;
      ParticleFactory.element = Granular.class;
    } else if (set && UI.mouse.clicked()) {
      set = false;
      ParticleFactory.element = Solid.class;
    }

  }

  @Override
  public void off(boolean once) {
    if (once) {
      hover = false;
      UI.mouse.revertShape();
    }

  }

  @Override
  public boolean trigger() {

    return (UI.mouse.cursorInBounds(r));

  }
}
