package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ui.UI.Printer;
import color.ColorGradientMap;
import java.awt.geom.Area;

/**
 * Button
 */
public class Button implements UIEvent {

  boolean triggered = false;
  int X = 610;
  int Y = 300;
  int W = 70;
  int H = 20;
  Color innerGradient = new Color(30, 30, 30);
  Color outerGradient = Color.BLACK;
  Rectangle r = new Rectangle(X, Y, W, H);
  BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_BGR);
  ColorGradientMap c = new ColorGradientMap();

  @Override
  public void draw(Printer p, Graphics2D g) {

    c.addColor(1.0, outerGradient);
    c.addColor(0.0, innerGradient);
    g.setColor(new Color(80, 80, 80));

    if (triggered) {
      double mx = UI.mouse.X() - X;
      double my = UI.mouse.Y() - Y;

      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          double val = Math.sqrt(Math.pow(Math.abs(mx - i), 2) + Math.pow(Math.abs(my - j), 2)) / ((W + H) / 2);
          img.setRGB(i, j, c.getColor(0.0).getRGB());
          img.setRGB(i, j, c.getColor(val).getRGB());
        }
      }
      g.drawImage(img, X, Y, null);
    }
    g.drawRect(X, Y, W, H);
  }

  @Override
  public void on(boolean once) {
    if (once) {
      triggered = true;
      UI.mouse.setShape(new Area(), false);
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
