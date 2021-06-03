package ui;

import java.awt.MouseInfo;

import java.awt.IllegalComponentStateException;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import core.*;

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

  /**
   * MouseWheelListener object that attaches to the Board
   */
  public MouseWheelListener wheelControls = new MouseWheelListener() {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      cursorSize = (cursorSize - 2 * e.getWheelRotation());
    }

  };

  /**
   * MouseAdapter object that attaches to the Board
   */
  public MouseAdapter adapter = new MouseAdapter() {

    public void mousePressed(MouseEvent e) {
      isDown = true;
    }

    public void mouseReleased(MouseEvent e) {
      isDown = false;
    }

  };

  /**
   * Returns the mouse's X value on the window divided by the current scale factor
   * 
   * @return int
   */
  public static int X() {

    Board B = Application.getBoard();
    return (int) (windowX() / B.getScale());

  }

  /**
   * Returns the mouse's Y value on the window divided by the current scale factor
   *
   * @return int
   */
  public static int Y() {
    Board B = Application.getBoard();
    return (int) (windowY() / B.getScale());
  }

  /**
   * Returns the x position of the cursor on the window rather than the grid (i.e.
   * unadjusted for scale)
   * 
   * @return the x position
   */
  public static int windowX() {

    Board B = Application.getBoard();
    try {
      int mx = MouseInfo.getPointerInfo().getLocation().x - B.getLocationOnScreen().x;
      return mx;

    } catch (IllegalComponentStateException e) {
      return 0;
    }

  }

  /**
   * Returns the y position of the cursor on the window rather than the grid (i.e.
   * unadjusted for scale)
   * 
   * @return the y position
   */
  public static int windowY() {

    Board B = Application.getBoard();

    try {
      int my = MouseInfo.getPointerInfo().getLocation().y - B.getLocationOnScreen().y;
      return my;
    } catch (IllegalComponentStateException e) {
      return 0;
    }
  }

}
