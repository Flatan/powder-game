package ui;

import ui.UI.TextBuffer;
import java.awt.Graphics2D;

/**
 * UIEvent
 */
public interface UIEvent {
  public boolean trigger();

  public void on(boolean once);

  public void off(boolean once);

  public void draw(TextBuffer t, Graphics2D g);
}
