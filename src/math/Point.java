
package math;

import java.awt.geom.Point2D;

/**
 * Point2D
 */
public class Point extends Point2D.Double {

  public Point(double x, double y) {
    super(x, y);
  }

  public Point add(Vector2D v) {

    return new Point(v.x + this.x, v.y + this.y);

  }

  @Override
  public String toString() {
    return String.format("(%.2f, %.2f)", x, y);
  }
}
