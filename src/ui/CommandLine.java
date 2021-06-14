package ui;

import core.Application;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.awt.Color;
import java.awt.Graphics;

/**
 * CommandLine
 */
public class CommandLine extends Label {

  boolean triggered = false;
  StringBuffer userInput = new StringBuffer();
  Color commandNotFound = new Color(150, 150, 150);
  Color commandFound = new Color(0, 150, 250);
  ArrayDeque<String> historyBuff = new ArrayDeque<>();

  CommandLine() {
    super(20, 560, 400, 20, "");
    setFocusable(true);
    addKeyListener(new Key());
  }

  class Key implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {

        // <Enter>
        case 10:
          if (!Application.board.testCommand(userInput.toString()))
            break;

          Application.board.invokeCommand(userInput.toString());
          EventLoader.fx.print(userInput.toString(), r.x, r.y - 20);
          historyBuff.push(userInput.toString());
          userInput.delete(0, userInput.length());
          break;

        // <Backspace>
        case 8:
          userInput.deleteCharAt(userInput.length() - 1);
          break;

        // <Up>
        case 38:
          userInput.delete(0, userInput.length());

          if (!historyBuff.isEmpty()) {
            userInput.append(historyBuff.pop());
          }

          break;

        default:
          userInput.append(Character.toString(e.getKeyChar()));

          ;
      }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
  }

  //
  @Override
  public void paint(Graphics g2) {
    LABEL = userInput.toString();
    g2.setColor(commandNotFound);

    if (Application.board.testCommand(userInput.toString())) {
      g2.setColor(commandFound);
    }

    super.paint(g2);
    // Requires focus for KeyListener to work
    requestFocus();
  }

}
