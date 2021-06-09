package ui;

import powder.*;
import ui.UI.Printer;
import java.awt.Graphics2D;
import core.*;

/**
 * ParticleFactory
 */
public class ParticleFactory implements UIEvent {
  final boolean onlyOne = false;
  public static int temp = 50;
  public static Class<? extends Particle> element = Granular.class;
  public static Shape shape = Shape.CIRCLE;

  enum Shape {
    CIRCLE, SQUARE, ONE
  }

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

  @Override
  public void draw(Printer p, Graphics2D g) {

  }

  @Override
  public void off(boolean once) {

  }

  @Override
  public void on(boolean once) {

    Board B = Application.board;

    int diameter = (int) (UI.mouse.getCursorSize() / Application.scale);
    int my;
    int mx = UI.mouse.X();

    ParticleGrid grid = Application.grid;

    my = (int) (B.getHeight() / Application.scale - 1 - UI.mouse.Y());

    switch (shape) {
      case CIRCLE:
        spawnCircle(mx, my, diameter);
        break;

      case SQUARE:
        // TODO
        break;

      case ONE:
        Particle p = grid.spawn(mx, my, element);
        p.temperature = temp;
        break;

      default:
        break;
    }
  }

  @Override
  public boolean trigger() {

    return UI.mouse.isDown;
  }

}
