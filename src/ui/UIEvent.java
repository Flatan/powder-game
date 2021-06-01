package ui;

import powder.Board;
import powder.*;

abstract public class UIEvent {

  public UIEvent() {

  }

  public abstract boolean sendingSignal();

  public abstract void eventOn();

  public abstract void eventOff();

}
