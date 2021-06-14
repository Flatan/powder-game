package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//import core.Mouse;
//
import color.ColorGradientMap;

import java.awt.Container;

/**
 * Label
 */
abstract public class Label extends Container {

  final int XPAD = 2;

  Color hoverFg = new Color(150, 150, 150);
  Color innerGradient = new Color(30, 30, 30);
  Color outerGradient = Color.BLACK;
  public String LABEL;
  final Rectangle r;
  final BufferedImage img;
  ColorGradientMap c = new ColorGradientMap().addColor(1.0, outerGradient).addColor(0.0, innerGradient);

  Label(int x, int y, int w, int h, String label) {

    this.r = new Rectangle(x, y, w, h);
    this.img = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_BGR);
    this.LABEL = label;
    setBounds(r);
    setPreferredSize(new Dimension(r.width, r.height));
    setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    setLocation(r.x, r.y);
    g.drawString(LABEL, XPAD, 0 + g.getFont().getSize() / 2 + (int) r.getHeight() / 2);

  }
}
