package ui;

import powder.*;
import ui.UI.TextBuffer;
import java.awt.Graphics2D;
import core.*;

/**
 * ToggleHeatMap
 */
public class ShowHeatMap implements UIEvent {

  @Override
  public void draw(TextBuffer t, Graphics2D g) {
    Board B = Application.getBoard();

    t.add("――――――――――――――――");
    t.add("t - toggle heat map display");
    t.add("c - cold particles");
    t.add("w - warm particles");
    t.add("h - hot particles");
    t.flush(B.getWidth() - 200, 60);
  }

  @Override
  public void off(boolean once) {
    Particle.showHeatMap = false;
  }

  @Override
  public void on(boolean once) {

    Board B = Application.getBoard();

    switch (UI.keyboard.keyPressed()) {
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
  public boolean trigger() {

    return UI.keyboard.keyToggled('t');

  }

}
