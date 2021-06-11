package ui;

import java.awt.MouseInfo;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import core.*;

/**
 * Collect the current mouse coordinates on the window
 */

public class Mouse {

  private int cursorSize = 20;
  public boolean isDown = false;
  private AffineTransform at = AffineTransform.getTranslateInstance(0.0, 0.0);
  private Boolean dynamic = true;
  private Boolean prevDynamic;
  private Shape shape;
  private Shape prevShape;

  public Mouse() {
  }

  public int getCursorSize() {
    return cursorSize;
  }

  public void setCursorSize(int cursorSize) {
    this.cursorSize = cursorSize;
  }

  public void setShape(Shape shape, boolean dynamic) {

    if (this.shape != null) {
      prevShape = this.shape;
      prevDynamic = this.dynamic;
    }

    this.shape = shape;
    this.dynamic = dynamic;
  }

  public Shape getShape() {
    return this.shape;
  }

  public boolean revertShape() {

    if (prevShape != null) {
      setShape(prevShape, prevDynamic);
      return true;
    }
    return false;
  }

  public void draw(Graphics2D g2) {

    Rectangle2D bounds = this.shape.getBounds2D();
    double w = bounds.getWidth();
    double h = bounds.getHeight();

    if (dynamic) {
      w = cursorSize;
      h = cursorSize;
    }

    at.setTransform(w, 0, 0, h, windowX() - w / 2, windowY() - h / 2);
    g2.draw(at.createTransformedShape(this.shape));
  }

  public boolean cursorInBounds(Rectangle r) {
    if (r.contains(X(), Y())) {
      return true;
    }
    return false;
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
  public int X() {

    return (int) (windowX() / Application.scale);

  }

  /**
   * Returns the mouse's Y value on the window divided by the current scale factor
   *
   * @return int
   */
  public int Y() {
    return (int) (windowY() / Application.scale);
  }

  /**
   * Returns the x position of the cursor on the window rather than the grid (i.e.
   * unadjusted for scale)
   * 
   * @return the x position
   */
  public int windowX() {

    Board B = Application.board;
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
  public int windowY() {

    Board B = Application.board;

    try {
      int my = MouseInfo.getPointerInfo().getLocation().y - B.getLocationOnScreen().y;
      return my;
    } catch (IllegalComponentStateException e) {
      return 0;
    }
  }

}
