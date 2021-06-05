package ui;

import ui.UI.DrawQueue;

/**
 * UIEvent
 */
public interface UIEvent {
  public boolean sendingSignal();

  public void eventOn(boolean justStarted);

  public void eventOff(boolean justEnded);

  public void draw(DrawQueue Q);
}
