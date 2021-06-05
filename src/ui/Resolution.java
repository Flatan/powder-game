package ui;

import core.Application;
import core.Board;
import java.awt.Graphics2D;
import ui.UI.TextBuffer;

public class Resolution implements UIEvent {

  @Override
  public void draw(TextBuffer t, Graphics2D g) {
    t.add("r - toggle resolution");
    t.flush(0, 60);

  }

  @Override
  public void on(boolean once) {

    Board B = Application.getBoard();

    B.setScale(2);
    B.setWidth(300);
    B.setHeight(300);
    B.setDelay(25);

    if (once) {
      B.reset();
    }

  }

  @Override
  public void off(boolean once) {

    Board B = Application.getBoard();

    B.setScale(1);
    B.setWidth(600);
    B.setHeight(600);
    B.setDelay(25);

    if (once) {
      B.reset();
    }

  }

  @Override
  public boolean trigger() {

    return UI.keyboard.keyToggled('r');

  }

}
