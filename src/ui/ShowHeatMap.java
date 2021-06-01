package ui;

import powder.*;

/**
 * ToggleHeatMap
 */
public class ShowHeatMap extends UIEvent {

  @Override
  public void eventOff() {
    Particle.showHeatMap = false;
  }

  @Override
  public void eventOn() {
    Particle.showHeatMap = true;
  }

  @Override
  public boolean isActive() {

    Board B = Application.getBoard();
    KeyAction K = B.getKeyboard();

    return K.keyToggled('t');

  }

}
