package ui;

import java.awt.MouseInfo;

import java.awt.IllegalComponentStateException;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import powder.Application;
import powder.Board;

/**
 * Collect the current mouse coordinates on the window
 */

public class Mouse {

  private int cursorSize = 20;
  public boolean isDown = false;
  private Board B = Application.getBoard();

  public Mouse() {
  }

  /**
   * Returns the cursor size
   * 
   * @return
   */
  public int getCursorSize() {
    return cursorSize;
  }

  /**
   * Sets the cursor size
   * 
   * @param cursorSize
   */
  public void setCursorSize(int cursorSize) {
    this.cursorSize = cursorSize;
  }

  public MouseWheelListener wheelControls = new MouseWheelListener() {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      cursorSize = (cursorSize - 2 * e.getWheelRotation());
    }

  };

  public MouseAdapter adapter = new MouseAdapter() {

    public void mousePressed(MouseEvent e) {
      isDown = true;
    }

    public void mouseReleased(MouseEvent e) {
      isDown = false;
    }

  };

  public static int X() {

    Board B = Application.getBoard();

    try {
      int mx = MouseInfo.getPointerInfo().getLocation().x - B.getLocationOnScreen().x;
      mx /= B.getScale();
      return mx;

    } catch (IllegalComponentStateException e) {
      return 0;
    }

  }

  public static int Y() {

    Board B = Application.getBoard();

    try {
      int my = MouseInfo.getPointerInfo().getLocation().y - B.getLocationOnScreen().y;
      my /= B.getScale();
      return my;
    } catch (IllegalComponentStateException e) {
      return 0;
    }
  }

}
