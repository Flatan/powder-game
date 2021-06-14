package ui;

import powder.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import core.*;

import java.awt.Graphics;
import java.awt.Component;
import java.awt.Dimension;

/**
 * ParticleFactory
 */
public class ParticleFactory extends Component implements MouseListener {
  final boolean onlyOne = false;
  public static int temp = 50;
  public static Class<? extends Particle> element = Granular.class;

  public static void spawnCircle(int x, int y, int diameter) {

    for (int x_ = x - diameter / 2; x_ < x + diameter / 2; x_++) {
      for (int y_ = y - diameter / 2; y_ < y + diameter / 2; y_++) {
        if (Math.hypot(x_ - x, y_ - y) <= diameter / 2) {
          Particle p = Application.grid.spawn(x_, y_, element);
          p.temperature = temp;
        }
      }
    }
  }

  public static void spawnRect(int x1, int y1, int x2, int y2, int stroke) {

    for (int i = x1; i < x2; i++) {
      for (int j = y1; j < y2; j++) {
        if (i > stroke && i < x2 - stroke - 1) {
          if (j < stroke || j > y2 - stroke - 1) {
          } else {
            continue;
          }
        }
        Particle p = Application.grid.spawn(i, j, element);
        p.temperature = temp;
      }
    }
  }

  ParticleFactory() {
    setPreferredSize(new Dimension(600, 600));
    addMouseListener(this);
  }

  @Override
  public void paint(Graphics g) {

  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mousePressed(MouseEvent e) {

    int diameter = (int) (UI.mouse.getCursorSize() / Application.scale);
    int my;

    my = (int) (Application.board.getHeight() / Application.scale - 1 - e.getY());

    spawnCircle(e.getX(), my, diameter);

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

}
