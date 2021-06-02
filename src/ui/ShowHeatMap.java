package ui;

import powder.*;
import core.*;

/**
 * ToggleHeatMap
 */
public class ShowHeatMap implements UIEvent {

  @Override
  public void eventOff(boolean justEnded) {
    Particle.showHeatMap = false;
  }

  @Override
  public void eventOn(boolean justStarted) {

    Board B = Application.getBoard();
    KeyAction K = B.getKeyboard();

    switch (K.keyPressed()) {
      case 'c':
        B.setSelectedTemp(0);
        break;
      case 'w':
        B.setSelectedTemp(50);
        break;
      case 'h':
        B.setSelectedTemp(100);
        break;
    }

    Particle.showHeatMap = true;
  }

  @Override
  public boolean sendingSignal() {

    KeyAction K = Application.getBoard().getKeyboard();

    return K.keyToggled('t');

  }

}
