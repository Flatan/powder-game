package ui;

/**
 * UIEvent
 */
public interface UIEvent {
  public boolean sendingSignal();

  public void eventOn(boolean justStarted);

  public void eventOff(boolean justEnded);
}
