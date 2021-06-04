
package ui;

import java.awt.Color;

import core.*;
import math.Vector2D;
import powder.*;

/**
 * GlobalSettings
 */
public class GlobalSettings implements UIEvent {

  public void testCollison() {
    ParticleGrid grid = Particle.getGrid();
    Particle.gravity = new Vector2D();
    Granular p1 = (Granular) grid.spawnParticle(0, 5, Color.WHITE, Granular.class);
    p1.vel = new Vector2D(0.1,0);
    Granular p2 = (Granular) grid.spawnParticle(9, 5, Color.WHITE, Granular.class);
    p2.vel = new Vector2D(-0.1,0);
  }

  @Override
  public void eventOn(boolean justStarted) {

    Board B = Application.getBoard();
    KeyAction K = B.getKeyboard();

    switch (K.keyPressed()) {
      case 'p':
        B.setSelectedElement(Granular.class);
        B.setSelectedColor(Color.white);
        break;
      case 's':
        B.setSelectedElement(Solid.class);
        B.setSelectedColor(Color.gray);
        break;
      case '0':
        B.setScale(60);
        B.setWidth(10);
        B.setHeight(10);
        B.setDelay(100);
        B.reset();
        break;
      case '1':
        B.setScale(2);
        B.setWidth(300);
        B.setHeight(300);
        B.setDelay(25);
        B.reset();
        break;
      case '2':
        B.setScale(1);
        B.setWidth(600);
        B.setHeight(600);
        B.setDelay(25);
        B.reset();
        break;
      case ' ':
        testCollison();
        break;
    }
  }

  @Override
  public void eventOff(boolean justEnded) {
  }

  @Override
  public boolean sendingSignal() {
    return true;
  }

}
