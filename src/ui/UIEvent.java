package ui;

import ui.UI.Printer;
import java.awt.Graphics2D;

/**
 * UIEvent
 */
public interface UIEvent {
  public boolean trigger();

  public void on(boolean once);

  public void off(boolean once);

  public void draw(Printer p, Graphics2D g);
}
