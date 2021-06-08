package ui;

import core.Application;
import core.Board;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

import ui.UI.TextBuffer;

public class Resolution implements UIEvent {

  @Override
  public void draw(TextBuffer t, Graphics2D g) {
    t.add("r - toggle resolution");
    t.flush(0, 70);

  }

  @Override
  public void on(boolean once) {

    Board B = Application.board;

    Application.scale = 2;
    B.setDelay(25);

    if (once) {
      B.setPreferredSize(new Dimension(300 * 2, 300 * 2));
      B.setBackground(Color.BLACK);
      Application.grid.reset(300, 300);
    }

  }

  @Override
  public void off(boolean once) {

    Board B = Application.board;

    Application.scale = 1;
    B.setDelay(25);

    if (once) {
      B.setPreferredSize(new Dimension(600, 600));
      B.setBackground(Color.BLACK);
      Application.grid.reset(600, 600);
    }

  }

  @Override
  public boolean trigger() {

    return UI.keyboard.keyToggled('r');

  }

}
