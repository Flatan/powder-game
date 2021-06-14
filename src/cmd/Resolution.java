package cmd;

import core.Application;
import core.Board;
import core.Command;

import java.awt.Color;
import java.awt.Dimension;

public class Resolution implements Command {
  boolean on = false;

  @Override
  public void call(String... args) {

    Board B = Application.board;

    if (this.on) {
      Application.scale = 1;
      B.setDelay(25);
      B.setPreferredSize(new Dimension(600, 600));
      B.setBackground(Color.BLACK);
      Application.grid.reset(600, 600);
      return;
    }

    Application.scale = 2;
    B.setDelay(25);
    B.setPreferredSize(new Dimension(300 * 2, 300 * 2));
    B.setBackground(Color.BLACK);
    Application.grid.reset(300, 300);

  }

}
