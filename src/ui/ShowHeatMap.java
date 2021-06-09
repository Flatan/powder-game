package ui;

import powder.*;
import ui.UI.Printer;
import java.awt.Graphics2D;

/**
 * ToggleHeatMap
 */
public class ShowHeatMap implements UIEvent {

  @Override
  public void draw(Printer p, Graphics2D g) {
    p.println("");
    p.println("t - toggle heat map display");
    p.println("c - cold particles");
    p.println("w - warm particles");
    p.println("h - hot particles");
  }

  @Override
  public void off(boolean once) {
    Particle.showHeatMap = false;
  }

  @Override
  public void on(boolean once) {

    switch (UI.keyboard.keyPressed()) {
      case 'c':
        ParticleFactory.temp = 0;
        break;
      case 'w':
        ParticleFactory.temp = 50;
        break;
      case 'h':
        ParticleFactory.temp = 100;
        break;
    }

    Particle.showHeatMap = true;
  }

  @Override
  public boolean trigger() {

    return UI.keyboard.keyToggled('t');

  }

}
