package ui;

import java.awt.MouseInfo;

import java.awt.IllegalComponentStateException;
import powder.Application;
import powder.Board;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

/**
 * Collect the current mouse coordinates on the window
 */

public class Mouse {

  public static MouseWheelListener wheelControls = new MouseWheelListener() {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      Board board = Application.board;
      int cursorSize = board.getCursorSize();
      board.setCursorSize(cursorSize - 2 * e.getWheelRotation());

    }

  };

  public static int X() {

    Board board = Application.board;

    try {
      int mx = MouseInfo.getPointerInfo().getLocation().x - board.getLocationOnScreen().x;
      mx /= board.getScale();
      return mx;

    } catch (IllegalComponentStateException e) {
      return 0;
    }

  }

  public static int Y() {

    Board board = Application.board;

    try {
      int my = MouseInfo.getPointerInfo().getLocation().y - board.getLocationOnScreen().y;
      my /= board.getScale();
      return my;
    } catch (IllegalComponentStateException e) {
      return 0;
    }
  }

}
