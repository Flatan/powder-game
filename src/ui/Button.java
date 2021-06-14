package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import core.UI;

import java.awt.event.MouseEvent;
//import core.Mouse;
//
import java.awt.event.MouseListener;
import color.ColorGradientMap;

import java.awt.Container;

/**
 * Button
 */
abstract public class Button extends Container implements MouseListener {

  final int XPAD = 2;

  private boolean hover = false;
  private boolean set;

  Color hoverFg = new Color(150, 150, 150);
  Color innerGradient = new Color(30, 30, 30);
  Color outerGradient = Color.BLACK;
  public String LABEL;
  final Rectangle r;
  final BufferedImage img;
  ColorGradientMap c = new ColorGradientMap().addColor(1.0, outerGradient).addColor(0.0, innerGradient);

  Button(int x, int y, int w, int h, String label) {

    this.set = false;
    this.r = new Rectangle(x, y, w, h);
    this.img = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_BGR);
    this.LABEL = label;
    setBounds(r);
    addMouseListener(this);
    setPreferredSize(new Dimension(r.width, r.height));
    setVisible(true);
  }

  public void disable() {
    this.set = false;
  }

  public void enable() {
    this.set = true;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    hover = false;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    if (set) {
      set = false;
      return;
    }
    set = true;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    hover = true;
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void paint(Graphics g) {

    super.paint(g);

    setLocation(r.x, r.y);
    g.setColor(new Color(80, 80, 80));

    if (hover) {

      double mx = UI.mouse.X() - r.x;
      double my = UI.mouse.Y() - r.y;

      BufferedImage tempimg = c.applyRadialGradient(img, (int) mx, (int) my, r);

      g.drawImage(tempimg, 0, 0, null);
      g.setColor(hoverFg);
    }

    if (set) {
      g.setColor(Color.WHITE);
      g.drawString(LABEL, XPAD, 0 + g.getFont().getSize() / 2 + (int) r.getHeight() / 2);
      g.drawRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
    } else {
      g.drawString(LABEL, XPAD, 0 + g.getFont().getSize() / 2 + (int) r.getHeight() / 2);
    }

  }
}
