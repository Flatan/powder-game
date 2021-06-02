package ui;

import powder.*;

/**
 * ToggleHeatMap
 */
public class ShowHeatMap implements UIEvent {

  @Override
  public void eventOff() {
    Particle.showHeatMap = false;
  }

  @Override
  public void eventOn() {
    Particle.showHeatMap = true;
  }

  @Override
  public boolean sendingSignal() {

    Board B = Application.getBoard();
    KeyAction K = B.getKeyboard();

    return K.keyToggled('t');

  }

}
