package ui;

import powder.Board;
import powder.*;

/**
 * UIEvent
 */
public interface UIEvent {
  public boolean sendingSignal();

  public void eventOn();

  public void eventOff();
}
