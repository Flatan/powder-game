package ui;

import core.Application;
import core.UI.Printer;
import core.UI.UIEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Hashtable;

/**
 * ToggleHeatMap
 */
public class Logger implements UIEvent {

  private static Hashtable<Long, String> text = new Hashtable<>();

  @Override
  public void draw(Printer p, Graphics2D g) {

    g.setColor(Color.WHITE);
    p.setLocation(Application.board.getWidth() - 200, Application.board.getHeight() - 200);

  }

  public static void log(String str) {
    text.put(System.currentTimeMillis(), str);
  }

  @Override
  public void off(boolean once) {
  }

  @Override
  public void on(boolean once) {
  }

  @Override
  public boolean trigger() {
    return false;
  }

}
