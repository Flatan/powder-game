
package ui;

import java.awt.Color;
import java.awt.Graphics;

import color.ColorGradientMap;

import java.awt.Dimension;
import java.awt.Container;

/**
 * Button
 */
public class TextFX extends Container {

  final int XPAD = 2;

  int x;
  int y;
  float iter;
  public String str;
  ColorGradientMap cmap = new ColorGradientMap();

  TextFX(int x, int y, String str) {
    this.x = x;
    this.y = y;
    this.str = str;
    cmap.addColor(1.0, Color.BLACK);
    cmap.addColor(0.0, Color.WHITE);
    setPreferredSize(new Dimension(600, 600));
    setVisible(true);
    iter = 0;
  }

  public void print(String str, int x, int y) {
    this.str = str;
    this.x = x;
    this.y = y;
    iter = 0;
  }

  @Override
  public void paint(Graphics g) {
    setLocation(0, 0);
    g.setColor(cmap.getColor(iter));
    g.drawString(str, x, y);
    iter += 0.02;

  }
}
