package powder;

import java.awt.Color;
import color.ColorGradientMap;
import core.Application;
import core.Board;
import math.Vector2D;
import math.Point;

/**
 * Particle
 *
 * This is the base particle class that represents a single pixel on the screen
 *
 *
 */
public abstract class Particle {

  public static boolean showHeatMap = false;
  public static ColorGradientMap heatmap = new ColorGradientMap();
  public static ColorGradientMap slopemap = new ColorGradientMap();

  public ParticleGrid grid = Application.grid;
  public static Vector2D gravity = new Vector2D(0, -0.5);

  // velocity:
  public Vector2D vel = new Vector2D(0, 0);
  public Point loc;

  public Vector2D giveVel = new Vector2D(0, 0);
  public Point oloc;
  public double targetX, targetY;
  public Color color;
  public Color displayColor = color;

  boolean dynamic = true;

  public double temperature = 0;

  public double mass = 1;

  // Thermal diffusivity:
  public double thermDiff = 0.47;

  Particle(int x, int y, Color color) {
    Board.runtimeParticleCount++;
    this.loc = new Point((double) x, (double) y);
    this.color = color;
    displayColor = color;
  }

  static public void setGravity(Vector2D g) {
    Particle.gravity = g;
  }

  static public Vector2D getGravity() {
    return Particle.gravity;
  }

  static public void toggleHeatMap() {

    if (showHeatMap) {
      showHeatMap = false;
    } else {
      showHeatMap = true;
    }
  }

  /**
   * Returns a Particle relative to the position of this one Takes regular
   * Cartesian coordinates
   *
   * @param x Relative x position
   * @param y Relative y position
   * @return Particle
   */
  public Particle getRel(int x, int y) {

    return grid.get(new Point(x, y));
  }

  /**
   * 
   * Checks if a particle exists relative to this one and returns a boolean Takes
   * regular Cartesian coordinates
   * 
   * @param x Relative x position
   * @param y Relative y position
   * @return boolean true if the particle exists else false
   */
  public boolean testRel(int x, int y) {

    return getRel(x, y) != null;
  }

  /**
   * Update Particle properties not directly related to position or velocity. This
   * is a temporary container. We might eventually want to have properties that
   * can actually change pos/vel.
   */
  public void updateProperties() {
    updateTemp();
  }

  /**
   * Updates the particle's temperature
   */
  private void updateTemp() {
    double leftTemp, rightTemp, topTemp, bottomTemp;
    // if a side is not touching another particle, treats it as a particle of the
    // same temperature
    leftTemp = testRel(-1, 0) ? getRel(-1, 0).temperature : temperature;
    rightTemp = testRel(1, 0) ? getRel(1, 0).temperature : temperature;
    bottomTemp = testRel(0, -1) ? getRel(0, -1).temperature : temperature;
    topTemp = testRel(0, 1) ? getRel(0, 1).temperature : temperature;

    double xDifference = (leftTemp - temperature) - (temperature - rightTemp);
    double yDifference = (bottomTemp - temperature) - (temperature - topTemp);

    temperature += (xDifference + yDifference) * thermDiff;

    if (showHeatMap) {

      displayColor = heatmap.getColor(temperature);
    } else
      displayColor = color;
  }

  /**
   * Finds if a particle is on the ground or on another grounded particle
   * 
   * @return boolean
   */
  public boolean supported() {
    return true;
  }

}
